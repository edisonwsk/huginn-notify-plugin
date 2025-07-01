@echo off

REM 自动设置 JAVA_HOME 为 JDK 11 路径
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-11.0.27.6-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Building Huginn Notify Plugin...

REM Clean and package the plugin
mvn clean package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful!
    echo Plugin file: target\huginn-notify.hpi
    echo.
    echo To install:
    echo 1. Go to Jenkins -> Manage Jenkins -> Manage Plugins
    echo 2. Click on "Advanced" tab
    echo 3. Upload the huginn-notify.hpi file
) else (
    echo.
    echo Build failed!
    exit /b 1
)

pause
