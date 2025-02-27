@echo off
chcp 65001 >nul
echo 正在检查 Java 安装...
call java -version
if %errorlevel% neq 0 (
    echo 未检测到 Java，请先安装 Java 并确保已将其加入系统环境变量
    pause
    exit /b %errorlevel%
)

echo 正在检查 Maven 安装...
call mvn -version
if %errorlevel% neq 0 (
    echo 未检测到 Maven，请先安装 Maven 并确保已将其加入系统环境变量
    pause
    exit /b %errorlevel%
)

echo 正在清理并打包项目...
call mvn clean package

if %errorlevel% neq 0 (
    echo 项目打包失败，请检查错误信息
    pause
    exit /b %errorlevel%
)

echo 项目打包成功，正在启动应用程序...
call java -jar target/personal-tax-calculator-1.0-SNAPSHOT.jar

if %errorlevel% neq 0 (
    echo 应用程序启动失败，请检查错误信息
    pause
    exit /b %errorlevel%
)

pause
