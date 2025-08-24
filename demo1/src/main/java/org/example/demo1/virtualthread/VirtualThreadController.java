package org.example.demo1.virtualthread;

import org.springframework.web.bind.annotation.*;
import java.util.concurrent.*;
import java.util.Map;
import java.util.HashMap;
import java.time.Instant;
import java.time.Duration;

/**
 * 虚拟线程Web API控制器
 * 提供HTTP接口来演示虚拟线程功能
 */
@RestController
@RequestMapping("/api/virtualthread")
public class VirtualThreadController {

    /**
     * 基本虚拟线程信息
     */
    @GetMapping("/info")
    public Map<String, Object> getVirtualThreadInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("virtualThreadSupported", isVirtualThreadSupported());
        info.put("currentThread", Thread.currentThread().toString());
        info.put("isVirtual", Thread.currentThread().isVirtual());
        return info;
    }

    /**
     * 创建单个虚拟线程演示
     */
    @GetMapping("/create-single")
    public Map<String, Object> createSingleVirtualThread() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                Thread currentThread = Thread.currentThread();
                try {
                    Thread.sleep(1000); // 模拟工作
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "线程被中断";
                }
                return String.format("虚拟线程完成工作 - %s, isVirtual: %s", 
                    currentThread.getName(), currentThread.isVirtual());
            }, createVirtualThreadExecutor());
            
            String threadResult = future.get(5, TimeUnit.SECONDS);
            result.put("success", true);
            result.put("result", threadResult);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * 性能测试：虚拟线程 vs 平台线程
     */
    @GetMapping("/performance-test")
    public Map<String, Object> performanceTest(@RequestParam(defaultValue = "1000") int taskCount) {
        Map<String, Object> result = new HashMap<>();
        
        // 限制任务数量以避免资源耗尽
        taskCount = Math.min(taskCount, 10000);
        
        try {
            // 测试虚拟线程
            long virtualTime = measureConcurrentTasks(taskCount, true);
            
            // 测试平台线程
            long platformTime = measureConcurrentTasks(taskCount, false);
            
            result.put("taskCount", taskCount);
            result.put("virtualThreadTime", virtualTime);
            result.put("platformThreadTime", platformTime);
            result.put("improvement", platformTime > 0 ? (double) platformTime / virtualTime : 0);
            result.put("success", true);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * 大规模并发测试
     */
    @GetMapping("/massive-concurrency")
    public Map<String, Object> massiveConcurrencyTest(@RequestParam(defaultValue = "10000") int threadCount) {
        Map<String, Object> result = new HashMap<>();
        
        // 限制线程数量
        threadCount = Math.min(threadCount, 50000);
        
        try {
            Instant start = Instant.now();
            
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                CountDownLatch latch = new CountDownLatch(threadCount);
                
                for (int i = 0; i < threadCount; i++) {
                    executor.submit(() -> {
                        try {
                            // 模拟轻量级I/O操作
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } finally {
                            latch.countDown();
                        }
                    });
                }
                
                boolean completed = latch.await(30, TimeUnit.SECONDS);
                Instant end = Instant.now();
                
                result.put("threadCount", threadCount);
                result.put("completed", completed);
                result.put("executionTime", Duration.between(start, end).toMillis());
                result.put("success", true);
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * 结构化并发演示
     */
    @GetMapping("/structured-concurrency")
    public Map<String, Object> structuredConcurrencyDemo() {
        Map<String, Object> result = new HashMap<>();
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // 提交多个相关任务
            Future<String> task1 = executor.submit(() -> {
                try {
                    Thread.sleep(500);
                    return "数据库查询完成";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "数据库查询被中断";
                }
            });
            
            Future<String> task2 = executor.submit(() -> {
                try {
                    Thread.sleep(800);
                    return "API调用完成";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "API调用被中断";
                }
            });
            
            Future<String> task3 = executor.submit(() -> {
                try {
                    Thread.sleep(300);
                    return "缓存更新完成";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "缓存更新被中断";
                }
            });
            
            // 收集结果
            result.put("task1", task1.get(2, TimeUnit.SECONDS));
            result.put("task2", task2.get(2, TimeUnit.SECONDS));
            result.put("task3", task3.get(2, TimeUnit.SECONDS));
            result.put("success", true);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * 检查虚拟线程支持
     */
    private boolean isVirtualThreadSupported() {
        try {
            // 尝试创建虚拟线程来检查支持
            Thread.ofVirtual().start(() -> {}).join();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建虚拟线程执行器
     */
    private ExecutorService createVirtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * 测量并发任务执行时间
     */
    private long measureConcurrentTasks(int taskCount, boolean useVirtualThreads) throws InterruptedException {
        Instant start = Instant.now();
        
        ExecutorService executor = useVirtualThreads ? 
            Executors.newVirtualThreadPerTaskExecutor() : 
            Executors.newFixedThreadPool(Math.min(200, taskCount));
        
        try {
            CountDownLatch latch = new CountDownLatch(taskCount);
            
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try {
                        // 模拟I/O操作
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            
            latch.await(30, TimeUnit.SECONDS);
            
        } finally {
            executor.shutdown();
        }
        
        Instant end = Instant.now();
        return Duration.between(start, end).toMillis();
    }
}
