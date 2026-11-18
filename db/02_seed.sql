-- ============================================================
-- 种子数据：1 用户 / 5 博主 / 12 篇文章 / 标签树 / 3 采集批次
-- 21 个快照版本 / 关注关系 / 检索任务 / 批注
-- ============================================================

INSERT INTO sys_user (username, password_hash) VALUES
('huliangjun', '$2a$10$demo_hash_demo_hash_demo_hash_');

INSERT INTO zh_author (author_id, name, headline) VALUES
('zhang-408',    '张码农',      '计算机考研辅导 / 408 系列'),
('db-deep',      '数据库那些事', 'PG / MySQL 原理向博主'),
('algo-cat',     '算法喵',      'LeetCode 与面试算法'),
('sys-design',   '系统设计笔记', '分布式与存储系统'),
('campus-life',  '川大生活家',   '校园 / 保研 / 日常');

INSERT INTO zh_article (url_token, author_id, title, content_type, publish_time) VALUES
('p-408-plan',   'zhang-408',   '408 全年复习规划：从数据结构到组成原理', 'article', now() - interval '300 days'),
('p-pg-index',   'db-deep',     'PostgreSQL 索引原理：B-tree 与 GiST 的区别', 'article', now() - interval '280 days'),
('p-exclude',    'db-deep',     '用 EXCLUDE 约束解决预约冲突问题', 'article', now() - interval '260 days'),
('p-lc-124',     'algo-cat',    'LeetCode 124 题解：二叉树中的最大路径和', 'article', now() - interval '240 days'),
('p-lc-dp',      'algo-cat',    '动态规划入门到进阶：状态定义的三个套路', 'article', now() - interval '220 days'),
('p-cap',        'sys-design',  'CAP 定理二十年：我们到底选 CP 还是 AP', 'article', now() - interval '200 days'),
('p-lsm',        'sys-design',  'LSM-Tree 与 B+Tree：存储引擎的分野', 'article', now() - interval '180 days'),
('p-scuta',      'campus-life', '川大保研时间线与材料准备清单', 'article', now() - interval '160 days'),
('p-408-mock',   'zhang-408',   '408 真题使用方法：三刷不是玄学', 'article', now() - interval '140 days'),
('p-mvcc',       'db-deep',     'MVCC 可视化：一条 UPDATE 在 PG 里发生了什么', 'article', now() - interval '120 days'),
('p-lc-graph',   'algo-cat',    '图论专题：拓扑排序的两种写法与坑', 'article', now() - interval '100 days'),
('p-kaoyan-mood','campus-life', '考研心态崩了怎么办：来自上岸者的十句话', 'pin',     now() - interval '80 days');

INSERT INTO tag (tag_name, parent_id) VALUES
('考研', NULL), ('计算机考研', 1), ('408', 2),
('数据库', NULL), ('PostgreSQL', 4), ('MySQL', 4),
('算法', NULL), ('系统设计', NULL), ('校园生活', NULL);

INSERT INTO article_tag (article_id, tag_id) VALUES
(1,2),(1,3), (2,5),(2,4), (3,5),(3,4), (4,7),(5,7),
(6,8),(7,8),(7,4), (8,9), (9,2),(9,3), (10,5),(10,4), (11,7), (12,1);

INSERT INTO crawl_batch (source_note) VALUES
('扩展导出 2026-09 批次'),('扩展导出 2026-10 批次'),('扩展导出 2026-11 批次');

INSERT INTO user_follow (user_id, author_id, follow_at, unfollow_at) VALUES
(1,'zhang-408',   now() - interval '310 days', NULL),
(1,'db-deep',     now() - interval '290 days', NULL),
(1,'algo-cat',    now() - interval '270 days', now() - interval '60 days'),
(1,'algo-cat',    now() - interval '30 days',  NULL),
(1,'sys-design',  now() - interval '210 days', NULL),
(1,'campus-life', now() - interval '150 days', NULL);

INSERT INTO article_snapshot (article_id, version_no, captured_at, batch_id, title, excerpt, content, voteup_count, comment_count)
SELECT a.article_id, 1, now() - interval '38 days', 1, a.title,
       '示例正文摘要……',
       '这是第' || a.article_id || '篇文章的正文内容示例。',
       10 * a.article_id, a.article_id % 5
FROM zh_article a;

INSERT INTO article_snapshot (article_id, version_no, captured_at, batch_id, title, excerpt, content, voteup_count, comment_count)
SELECT a.article_id, 2, now() - interval '8 days', 2, a.title,
       '示例正文摘要（作者修订版）……',
       '这是第' || a.article_id || '篇文章的正文内容示例（v2修订）。',
       10 * a.article_id + 15, a.article_id % 5 + 2
FROM zh_article a WHERE a.article_id IN (1,2,3,5,7,9,10);

INSERT INTO article_snapshot (article_id, version_no, captured_at, batch_id, title, excerpt, content, voteup_count, comment_count)
SELECT a.article_id, 3, now() - interval '1 day', 3, a.title,
       '示例正文摘要（最新版）……',
       '这是第' || a.article_id || '篇文章的正文内容示例（v3最新）。',
       10 * a.article_id + 40, a.article_id % 5 + 4
FROM zh_article a WHERE a.article_id IN (2,9);

UPDATE crawl_batch SET status='SUCCESS', finished_at = started_at + interval '2 hours' WHERE batch_id IN (1,2);
UPDATE crawl_batch SET status='SUCCESS', finished_at = started_at + interval '1 hour'  WHERE batch_id = 3;

INSERT INTO author_stat_daily (author_id, stat_date, follower_cnt, total_voteup)
SELECT author_id, current_date - 30, 1000 + row_number() OVER () * 37, 0 FROM zh_author;
INSERT INTO author_stat_daily (author_id, stat_date, follower_cnt, total_voteup)
SELECT author_id, current_date, 1000 + row_number() OVER () * 37 + 42, 0 FROM zh_author;

INSERT INTO search_task (user_id, keyword, executed_at, duration_ms, result_cnt) VALUES
(1, '索引', now() - interval '20 days', 12, 2),
(1, '排他', now() - interval '15 days', 9,  1),
(1, '408',  now() - interval '10 days', 11, 2),
(1, '缓存', now() - interval '5 days',  8,  0);

INSERT INTO article_note (user_id, article_id, created_at, note_text) VALUES
(1, 2, now() - interval '18 days', 'GiST 与 B-tree 的适用场景对比'),
(1, 3, now() - interval '14 days', 'EXCLUDE 约束思路可借到场地预约系统');
