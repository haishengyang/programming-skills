package org.example.graalvm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final long applicationStartTime;
    
    public PerformanceController() {
        this.applicationStartTime = System.currentTimeMillis();
    }

    @GetMapping("/info")
    public Map<String, Object> getPerformanceInfo() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        Map<String, Object> info = new HashMap<>();

        // JVM信息
        info.put("jvmName", runtimeBean.getVmName());
        info.put("jvmVersion", runtimeBean.getVmVersion());
        info.put("jvmVendor", runtimeBean.getVmVendor());

        // 启动时间信息 - 使用JVM uptime作为准确的启动时间
        long jvmUptime = runtimeBean.getUptime();
        info.put("jvmUptimeMs", jvmUptime);
        info.put("startupTimeMs", jvmUptime); // 主要显示这个

        // 内存信息 - 使用实际使用的内存
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();

        info.put("usedMemoryMB", usedMemory / (1024 * 1024));
        info.put("totalMemoryMB", totalMemory / (1024 * 1024));
        info.put("maxMemoryMB", maxMemory / (1024 * 1024));
        info.put("freeMemoryMB", freeMemory / (1024 * 1024));

        // 堆内存信息（更准确）
        long heapUsed = memoryBean.getHeapMemoryUsage().getUsed();
        long heapMax = memoryBean.getHeapMemoryUsage().getMax();
        long heapCommitted = memoryBean.getHeapMemoryUsage().getCommitted();

        info.put("heapUsedMB", heapUsed / (1024 * 1024));
        info.put("heapMaxMB", heapMax / (1024 * 1024));
        info.put("heapCommittedMB", heapCommitted / (1024 * 1024));

        // 系统信息
        info.put("availableProcessors", runtime.availableProcessors());

        // 运行时类型检测
        String vmName = runtimeBean.getVmName();
        boolean isNativeImage = vmName.contains("Substrate VM") || vmName.contains("GraalVM");
        info.put("isNativeImage", isNativeImage);
        info.put("runtimeType", isNativeImage ? "GraalVM Native Image" : "Traditional JVM");

        return info;
    }

    @GetMapping("/startup-time")
    public Map<String, Object> getStartupTime() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        
        Map<String, Object> result = new HashMap<>();
        result.put("jvmUptimeMs", runtimeBean.getUptime());
        result.put("message", "应用启动时间: " + runtimeBean.getUptime() + "ms");
        
        return result;
    }

    @GetMapping("/compute")
    public Map<String, Object> performComputation(@RequestParam(defaultValue = "1000000") int iterations) {
        long startTime = System.nanoTime();
        
        // 执行计算密集型任务
        double result = 0;
        for (int i = 0; i < iterations; i++) {
            result += Math.sqrt(ThreadLocalRandom.current().nextDouble() * 1000);
        }
        
        long endTime = System.nanoTime();
        long executionTimeMs = (endTime - startTime) / 1_000_000;
        
        Map<String, Object> response = new HashMap<>();
        response.put("iterations", iterations);
        response.put("result", result);
        response.put("executionTimeMs", executionTimeMs);
        response.put("message", String.format("执行 %d 次计算，耗时: %d ms", iterations, executionTimeMs));
        
        return response;
    }

    @GetMapping("/memory-test")
    public Map<String, Object> memoryTest(@RequestParam(defaultValue = "1000") int arraySize) {
        Runtime runtime = Runtime.getRuntime();
        
        // 记录测试前的内存状态
        long beforeUsed = runtime.totalMemory() - runtime.freeMemory();
        
        // 创建大量对象
        int[][] arrays = new int[arraySize][];
        for (int i = 0; i < arraySize; i++) {
            arrays[i] = new int[1000];
            for (int j = 0; j < 1000; j++) {
                arrays[i][j] = ThreadLocalRandom.current().nextInt();
            }
        }
        
        // 记录测试后的内存状态
        long afterUsed = runtime.totalMemory() - runtime.freeMemory();
        
        // 清理内存
        arrays = null;
        System.gc();
        
        long afterGC = runtime.totalMemory() - runtime.freeMemory();
        
        Map<String, Object> result = new HashMap<>();
        result.put("arraySize", arraySize);
        result.put("beforeUsedMB", beforeUsed / (1024 * 1024));
        result.put("afterUsedMB", afterUsed / (1024 * 1024));
        result.put("afterGCMB", afterGC / (1024 * 1024));
        result.put("memoryIncreaseMB", (afterUsed - beforeUsed) / (1024 * 1024));
        
        return result;
    }

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        long responseTime = System.nanoTime();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello from GraalVM Native Image!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("responseTimeNs", System.nanoTime() - responseTime);
        
        return response;
    }
}
