@echo off
chcp 65001 >nul
"C:\Users\华为\.workbuddy\binaries\pgsql\pgsql\bin\pg_isready.exe" -h localhost -p 5432 >nul 2>&1
if errorlevel 1 (
    start "postgres" /min "C:\Users\华为\.workbuddy\binaries\pgsql\pgsql\bin\postgres.exe" -D "D:\pgdata" -p 5432
)
echo PostgreSQL ready on 5432
