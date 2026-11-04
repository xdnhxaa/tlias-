@echo off
title tlias 员工管理系统 - 停止
echo 正在停止后端服务 ...
taskkill /FI "WINDOWTITLE eq tlias-backend*" /F >nul 2>nul
echo 正在停止 MySQL ...
"D:\shujuku\mysql-8.0.44-winx64\bin\mysqladmin.exe" -u root -p123456 shutdown 2>nul
echo.
echo 已全部停止。
pause
