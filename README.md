# 知乎知识库的设计与实现

> 一个带时间维度的个人知识库：把知乎文章的每次变化存成一份快照。
> 数据库课程设计 · PostgreSQL 16 + Spring Boot 3.3 + MyBatis + Vue 3 + Element Plus

## 项目结构
- db/ — 数据库脚本（12表/4视图/2触发器/23索引）
- backend/ — Spring Boot 3.3 + MyBatis（端口8080）
- frontend/ — Vue 3 + Element Plus + ECharts（端口5173）
- collector/ — Python采集器
- docs/ — 页面截图

## 启动
```bat
一键演示.bat
```

## 合规
只采集公开内容，仅本地研究用途。