@echo off
title tlias 前端开发模式
echo 开发模式：前端跑在 5173 端口，通过 Vite 代理访问后端 8080
echo 请确保后端与 MySQL 已启动（可先运行 启动系统.bat）
echo.
cd /d "%~dp0tlias-frontend"
if not exist node_modules (
    echo 首次运行，正在安装依赖 ...
    call npm install --registry=https://registry.npmmirror.com
)
call npm run dev
pause
