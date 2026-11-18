-- ============================================================
-- 《基于知乎关注数据的个人知识时序库》 PostgreSQL 16 版
-- 12 张基表 / 4 视图 / 2 触发器 / 排他约束+部分唯一索引+生成列
-- ============================================================
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS btree_gist;
CREATE EXTENSION IF NOT EXISTS plpgsql;

CREATE TABLE sys_user (
    user_id       bigserial PRIMARY KEY,
    username      varchar(50) NOT NULL UNIQUE,
    password_hash char(60)    NOT NULL,
    created_at    timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE zh_author (
    author_id            varchar(32) PRIMARY KEY,
    name                 varchar(100) NOT NULL,
    headline             varchar(200),
    article_cnt          int NOT NULL DEFAULT 0,
    latest_follower_cnt  int NOT NULL DEFAULT 0 CHECK (latest_follower_cnt >= 0),
    created_at           timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE zh_article (
    article_id    bigserial PRIMARY KEY,
    url_token     varchar(64) UNIQUE NOT NULL,
    author_id     varchar(32) NOT NULL REFERENCES zh_author(author_id) ON DELETE RESTRICT,
    title         varchar(300) NOT NULL,
    content_type  varchar(10) NOT NULL DEFAULT 'article'
                  CHECK (content_type IN ('article','answer','pin')),
    publish_time  timestamptz,
    first_seen_at timestamptz NOT NULL DEFAULT now(),
    last_seen_at  timestamptz,
    latest_version_no int NOT NULL DEFAULT 0,
    search_vec    tsvector GENERATED ALWAYS AS (
                      to_tsvector('simple', coalesce(title,''))
                  ) STORED
);

CREATE TABLE tag (
    tag_id    int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tag_name  varchar(50) NOT NULL UNIQUE,
    parent_id int REFERENCES tag(tag_id) ON DELETE SET NULL
);

CREATE TABLE article_tag (
    article_id bigint NOT NULL REFERENCES zh_article(article_id) ON DELETE CASCADE,
    tag_id     int    NOT NULL REFERENCES tag(tag_id),
    PRIMARY KEY (article_id, tag_id)
);

CREATE TABLE crawl_batch (
    batch_id     bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    source_note  varchar(200),
    started_at   timestamptz NOT NULL DEFAULT now(),
    finished_at  timestamptz,
    status       varchar(10) NOT NULL DEFAULT 'RUNNING'
                 CHECK (status IN ('RUNNING','SUCCESS','FAILED')),
    fetched_cnt  int NOT NULL DEFAULT 0,
    inserted_cnt int NOT NULL DEFAULT 0,
    updated_cnt  int NOT NULL DEFAULT 0,
    CONSTRAINT chk_batch_finish CHECK (
        (status = 'RUNNING' AND finished_at IS NULL)
        OR (status IN ('SUCCESS','FAILED') AND finished_at IS NOT NULL)
    )
);

CREATE TABLE article_snapshot (
    article_id    bigint NOT NULL REFERENCES zh_article(article_id) ON DELETE CASCADE,
    version_no    int    NOT NULL CHECK (version_no >= 1),
    captured_at   timestamptz NOT NULL,
    batch_id      bigint NOT NULL REFERENCES crawl_batch(batch_id),
    title         varchar(300),
    excerpt       varchar(500),
    content       text,
    content_html  text,
    voteup_count  int NOT NULL DEFAULT 0 CHECK (voteup_count  >= 0),
    comment_count int NOT NULL DEFAULT 0 CHECK (comment_count >= 0),
    content_hash  char(32) GENERATED ALWAYS AS (md5(coalesce(content,''))) STORED,
    PRIMARY KEY (article_id, version_no),
    CONSTRAINT uq_snap_hash UNIQUE (article_id, content_hash),
    CONSTRAINT chk_captured_in_batch CHECK (captured_at >= now() - interval '10 years')
);

CREATE UNIQUE INDEX uq_snap_batch_article
    ON article_snapshot (batch_id, article_id);

CREATE TABLE author_stat_daily (
    author_id    varchar(32) NOT NULL REFERENCES zh_author(author_id) ON DELETE CASCADE,
    stat_date    date NOT NULL,
    follower_cnt int NOT NULL CHECK (follower_cnt >= 0),
    total_voteup int NOT NULL CHECK (total_voteup >= 0),
    PRIMARY KEY (author_id, stat_date)
);

CREATE TABLE user_follow (
    user_id     bigint NOT NULL REFERENCES sys_user(user_id),
    author_id   varchar(32) NOT NULL REFERENCES zh_author(author_id) ON DELETE CASCADE,
    follow_at   timestamptz NOT NULL DEFAULT now(),
    unfollow_at timestamptz,
    PRIMARY KEY (user_id, author_id, follow_at),
    EXCLUDE USING gist (
        user_id WITH =,
        author_id WITH =,
        tstzrange(follow_at, COALESCE(unfollow_at, 'infinity')) WITH &&
    )
);

CREATE TABLE search_task (
    task_id     bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id     bigint NOT NULL REFERENCES sys_user(user_id),
    keyword     varchar(100) NOT NULL,
    executed_at timestamptz NOT NULL DEFAULT now(),
    duration_ms int NOT NULL CHECK (duration_ms >= 0),
    result_cnt  int NOT NULL DEFAULT 0 CHECK (result_cnt >= 0)
);

CREATE TABLE search_result (
    task_id    bigint NOT NULL REFERENCES search_task(task_id) ON DELETE CASCADE,
    article_id bigint NOT NULL REFERENCES zh_article(article_id),
    rank_no    int NOT NULL CHECK (rank_no >= 1),
    score      numeric(6,4),
    PRIMARY KEY (task_id, article_id)
);

CREATE TABLE article_note (
    user_id    bigint NOT NULL REFERENCES sys_user(user_id),
    article_id bigint NOT NULL REFERENCES zh_article(article_id) ON DELETE CASCADE,
    created_at timestamptz NOT NULL DEFAULT now(),
    note_text  text NOT NULL,
    PRIMARY KEY (user_id, article_id, created_at)
);

CREATE INDEX idx_article_author_time ON zh_article (author_id, publish_time DESC);
CREATE INDEX idx_snap_article_capt  ON article_snapshot (article_id, captured_at DESC);
CREATE INDEX idx_result_article     ON search_result (article_id);
CREATE INDEX idx_search_vec         ON zh_article USING gin (search_vec);
CREATE INDEX idx_tag_trgm           ON zh_article USING gin (title gin_trgm_ops);

CREATE OR REPLACE FUNCTION trg_snapshot_after_insert() RETURNS trigger AS $$
BEGIN
    UPDATE zh_article SET last_seen_at = NEW.captured_at, latest_version_no = NEW.version_no WHERE article_id = NEW.article_id;
    UPDATE zh_author SET article_cnt = article_cnt + 1 WHERE author_id = (SELECT author_id FROM zh_article WHERE article_id = NEW.article_id) AND NEW.version_no = 1;
    RETURN NEW;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER snapshot_after_insert AFTER INSERT ON article_snapshot FOR EACH ROW EXECUTE FUNCTION trg_snapshot_after_insert();

CREATE OR REPLACE FUNCTION trg_batch_close() RETURNS trigger AS $$
BEGIN
    IF NEW.status IN ('SUCCESS','FAILED') AND OLD.status = 'RUNNING' THEN
        SELECT count(*) INTO NEW.inserted_cnt FROM article_snapshot WHERE batch_id = NEW.batch_id;
        NEW.fetched_cnt := NEW.fetched_cnt + NEW.inserted_cnt;
    END IF;
    RETURN NEW;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER batch_close BEFORE UPDATE ON crawl_batch FOR EACH ROW EXECUTE FUNCTION trg_batch_close();

CREATE VIEW v_article_detail AS
SELECT a.article_id, a.title, a.url_token, au.name AS author_name,
       s.excerpt, s.voteup_count, s.comment_count, s.captured_at, s.version_no, a.publish_time
FROM zh_article a
JOIN zh_author au ON au.author_id = a.author_id
JOIN LATERAL (SELECT * FROM article_snapshot s2 WHERE s2.article_id = a.article_id ORDER BY version_no DESC LIMIT 1) s ON true;

CREATE VIEW v_author_rank AS
SELECT au.author_id, au.name, au.article_cnt,
       sum(s.voteup_count) AS total_voteup,
       round(avg(s.voteup_count), 1) AS avg_voteup,
       rank() OVER (ORDER BY sum(s.voteup_count) DESC) AS voteup_rank
FROM zh_author au
JOIN zh_article a ON a.author_id = au.author_id
JOIN article_snapshot s ON s.article_id = a.article_id AND s.version_no = a.latest_version_no
GROUP BY au.author_id, au.name, au.article_cnt;

CREATE VIEW v_tag_trend AS
SELECT t.tag_id, t.tag_name, date_trunc('month', a.publish_time) AS month,
       count(a.article_id) AS article_cnt,
       lag(count(a.article_id)) OVER (PARTITION BY t.tag_id ORDER BY date_trunc('month', a.publish_time)) AS prev_month_cnt
FROM tag t
JOIN article_tag at2 ON at2.tag_id = t.tag_id
JOIN zh_article a ON a.article_id = at2.article_id
GROUP BY t.tag_id, t.tag_name, date_trunc('month', a.publish_time);

CREATE VIEW v_my_unread AS
SELECT a.article_id, a.title, au.name AS author_name
FROM user_follow f
JOIN zh_article a ON a.author_id = f.author_id
JOIN zh_author au ON au.author_id = a.author_id
LEFT JOIN article_note n ON n.article_id = a.article_id AND n.user_id = f.user_id
WHERE f.unfollow_at IS NULL AND n.article_id IS NULL;
