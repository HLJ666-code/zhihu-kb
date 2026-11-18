@echo off
chcp 65001 >nul
cd /d "%~dp0backend"
"D:\新的\bin\java.exe" -jar target\zhihu-kb-1.0.0.jar --server.port=8080
