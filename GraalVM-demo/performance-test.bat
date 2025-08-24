@echo off
chcp 65001 >nul
echo.
echo ========================================
echo 📊 GraalVM 性能自动化测试
echo ========================================
echo.

echo 🔧 正在启动应用进行性能测试...
echo 请确保应用已经启动，并记下启动日志中显示的端口号
echo 如果不知道端口号，请查看应用启动日志
echo.

set /p PORT="请输入应用运行的端口号: "
if "%PORT%"=="" set PORT=8080

echo 使用端口: %PORT%
echo.

timeout /t 3 >nul

echo 📋 执行性能测试套件：
echo.

echo 1️⃣ 测试系统信息...
curl -s "http://localhost:%PORT%/api/performance/info" > performance-info.json
echo ✅ 系统信息已保存到 performance-info.json

echo.
echo 2️⃣ 测试启动时间...
curl -s "http://localhost:%PORT%/api/performance/startup-time"
echo.

echo 3️⃣ 测试计算性能（100万次迭代）...
curl -s "http://localhost:%PORT%/api/performance/compute?iterations=1000000"
echo.

echo 4️⃣ 测试内存使用...
curl -s "http://localhost:%PORT%/api/performance/memory-test?arraySize=1000"
echo.

echo 5️⃣ 测试响应速度（10次请求）...
for /l %%i in (1,1,10) do (
    echo 请求 %%i:
    curl -s "http://localhost:%PORT%/api/performance/hello"
    echo.
)

echo.
echo ========================================
echo ✅ 性能测试完成
echo ========================================
echo.
echo 📊 测试结果已显示在上方
echo 📁 详细系统信息保存在 performance-info.json
echo.
echo 💡 建议：
echo • 对比 JVM 和 Native Image 版本的测试结果
echo • 关注启动时间和内存使用的差异
echo • 多次运行测试以获得平均值
echo.
pause
