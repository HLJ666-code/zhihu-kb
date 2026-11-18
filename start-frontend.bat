@echo off
chcp 65001 >nul
cd /d "%~dp0frontend"
"C:\Users\华为\.workbuddy\binaries\node\versions\22.22.2-6\node.exe" node_modules\vite\bin\vite.js --port 5173
