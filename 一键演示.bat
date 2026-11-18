@echo off
chcp 65001 >nul
title 知乎知识库 - 一键演示
echo 启动中...
start-db.bat
start-backend.bat
timeout /t 10 >nul
start http://127.0.0.1:8080
