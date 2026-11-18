-- 03_html.sql 正文原文 HTML（可重复执行）
ALTER TABLE article_snapshot ADD COLUMN IF NOT EXISTS content_html text;
COMMENT ON COLUMN article_snapshot.content_html IS '知乎原文 HTML（保留标题/列表/代码块/公式/图片），仅用于展示';
