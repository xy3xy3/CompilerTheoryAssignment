@echo off
chcp 65001 >nul
echo 在启动应用程序...
call java -jar target/postfix-1.0-SNAPSHOT.jar

if %errorlevel% neq 0 (
    echo 应用程序启动失败，请检查错误信息
    pause
    exit /b %errorlevel%
)

pause