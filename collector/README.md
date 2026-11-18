# 真实采集器（collect.py）

把知乎文章/回答原文抓下来入库。

## 准备

1. 浏览器登录知乎，F12 复制 Cookie
2. 粘贴到 zhihu_cookie.txt

## 采集

```bat
python collect.py --search "PostgreSQL 索引" --limit 3
```
