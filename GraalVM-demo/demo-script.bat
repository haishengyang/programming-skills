@echo off
chcp 65001 >nul
echo.
echo ========================================
echo 🚀 GraalVM 性能演示脚本
echo ========================================
echo.

echo 📋 演示步骤：
echo 1. 构建并运行传统 JVM 版本
echo 2. 构建 Native Image
echo 3. 运行 Native Image 版本
echo 4. 性能对比
echo.

pause

echo.
echo ========================================
echo 📦 步骤 1: 构建项目
echo ========================================
call mvnw clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo ❌ 构建失败
    pause
    exit /b 1
)

echo.
echo ========================================
echo ☕ 步骤 2: 运行传统 JVM 版本
echo ========================================
echo 🕐 正在启动 JVM 版本，请观察启动时间...
echo 注意：应用使用随机端口，请查看启动日志中的实际端口号
echo 按 Ctrl+C 停止应用后继续下一步
echo.
java -jar target\GraalVM-demo-0.0.1-SNAPSHOT.jar

echo.
echo ========================================
echo 🔨 步骤 3: 构建 Native Image
echo ========================================
echo 🕐 正在构建 Native Image，这可能需要几分钟...
call mvnw native:compile -Pnative
if %ERRORLEVEL% neq 0 (
    echo ❌ Native Image 构建失败
    pause
    exit /b 1
)

echo.
echo ========================================
echo ⚡ 步骤 4: 运行 Native Image 版本
echo ========================================
echo 🕐 正在启动 Native Image 版本，请观察启动时间...
echo 注意：应用使用随机端口，请查看启动日志中的实际端口号
echo.
target\GraalVM-demo.exe

echo.
echo ========================================
echo 📊 性能对比总结
echo ========================================
echo ✅ 演示完成！
echo.
echo 🔍 主要对比点：
echo • 启动时间：Native Image 通常快 10-100 倍
echo • 内存占用：Native Image 通常减少 50-80%%
echo • 文件大小：Native Image 是单一可执行文件
echo • 响应速度：Native Image 无需预热即可达到最佳性能
echo.
pause
