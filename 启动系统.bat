@echo off
title tlias 员工管理系统 - 启动
echo ============================================================
echo            tlias 员工管理系统  一键启动
echo ============================================================
echo.

echo [1/4] 检查并启动 MySQL ...
tasklist /FI "IMAGENAME eq mysqld.exe" | find /I "mysqld.exe" >nul
if errorlevel 1 (
    echo       正在启动 MySQL 8.0.44 ...
    start "MySQL" /min "D:\shujuku\mysql-8.0.44-winx64\bin\mysqld.exe" --defaults-file="D:\shujuku\my.ini"
    timeout /t 8 >nul
) else (
    echo       MySQL 已在运行，跳过
)

echo [2/4] 检查数据库 tlias ...
"D:\shujuku\mysql-8.0.44-winx64\bin\mysql.exe" -u root -p123456 -e "use tlias;" 2>nul
if errorlevel 1 (
    echo       数据库不存在，正在导入 sql\tlias.sql ...
    "D:\shujuku\mysql-8.0.44-winx64\bin\mysql.exe" -u root -p123456 < "%~dp0sql\tlias.sql"
    echo       导入完成
) else (
    echo       数据库就绪
)

echo [3/4] 启动后端服务 ^(SpringBoot, 端口 8080^) ...
start "tlias-backend" /min "D:\1\tools\jdk-21\bin\java.exe" -Dfile.encoding=UTF-8 -jar "%~dp0dist\tlias-backend.jar"
echo       等待服务就绪 ...
timeout /t 12 >nul

echo [4/4] 打开浏览器 ...
start http://localhost:8080

echo.
echo ============================================================
echo   启动完成！
echo   访问地址： http://localhost:8080
echo   登录账号： zhangsan
echo   登录密码： 123456
echo ============================================================
echo.
pause
