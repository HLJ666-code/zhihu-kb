# 知乎知识库的设计与实现

> 一个带时间维度的个人知识库：把知乎文章的**每次变化存成一份快照**，让"当时是什么样"变成一条可以执行的 SQL。
> 数据库课程设计 · PostgreSQL 16 + Spring Boot 3.3 + MyBatis + Vue 3 + Element Plus

## 一、项目结构

```
zhihu-kb/
├── db/                    # 数据库脚本
│   ├── 00_reset.sql       # 复位：重建 zh_knowledge
│   ├── 01_schema.sql      # 12 表 / 4 视图 / 2 触发器 / 23 索引
│   └── 02_seed.sql        # 种子数据：5 博主 / 12 文章 / 21 份快照
├── backend/               # Spring Boot 3.3 + MyBatis（端口 8080）
├── frontend/              # Vue 3 + Element Plus + ECharts（端口 5173）
├── docs/                  # 页面截图
├── collector/             # 真实采集器
├── 一键演示.bat           # 答辩演示用
└── start-db / start-backend / start-frontend.bat
```

## 二、启动

```bat
一键演示.bat
```

## 三、技术栈

PostgreSQL 16 + Spring Boot 3.3 + MyBatis + Vue 3 + Element Plus + ECharts

## 四、合规口径

只采集本人账号可见的公开内容，仅本地研究用途；演示基于离线数据集，不现场联网。
