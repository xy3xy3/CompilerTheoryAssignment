@echo off
chcp 65001 >nul
echo 正在生成Javadoc文档...

rem 清理旧的文档
if exist docs rmdir /s /q docs

rem 使用javadoc命令生成文档
javadoc -d docs -sourcepath src/main/java -subpackages com.compiler -encoding UTF-8 -charset UTF-8 -docencoding UTF-8

if %errorlevel% neq 0 (
    echo Javadoc生成失败，请检查错误信息
    pause
    exit /b %errorlevel%
)

echo Javadoc文档已成功生成在docs目录中
pause
