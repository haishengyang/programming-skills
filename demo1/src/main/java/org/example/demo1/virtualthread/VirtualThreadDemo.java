package org.example.demo1.virtualthread;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Java 虚拟线程演示
 * 展示虚拟线程相比传统线程的优势
 * 
 * 虚拟线程是Java 19引入的轻量级线程，在Java 21中正式发布
 * 主要优势：
 * 1. 极低的内存占用（几KB vs 几MB）
 * 2. 快速创建和销毁
 * 3. 适合I/O密集型任务
 * 4. 可以创建数百万个虚拟线程
 */
public class VirtualThreadDemo {

    /**
     * 演示虚拟线程的基本使用
     */
    public static void basicVirtualThreadDemo() {
        System.out.println("=== 虚拟线程基本演示 ===");
        
        // 创建虚拟线程
        Thread virtualThread = Thread.ofVirtual()
                .name("virtual-thread-1")
                .start(() -> {
                    System.out.println("虚拟线程运行中: " + Thread.currentThread());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("虚拟线程完成");
                });

        try {
            virtualThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 性能对比：虚拟线程 vs 平台线程
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能对比演示 ===");
        
        int taskCount = 10000;
        
        // 测试虚拟线程性能
        System.out.println("测试虚拟线程性能...");
        long virtualThreadTime = measureExecutionTime(() -> {
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
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
                
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // 测试平台线程性能（使用线程池）
        System.out.println("测试平台线程性能...");
        long platformThreadTime = measureExecutionTime(() -> {
            try (ExecutorService executor = Executors.newFixedThreadPool(200)) {
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
                
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        System.out.printf("任务数量: %d%n", taskCount);
        System.out.printf("虚拟线程执行时间: %d ms%n", virtualThreadTime);
        System.out.printf("平台线程执行时间: %d ms%n", platformThreadTime);
        System.out.printf("性能提升: %.2fx%n", (double) platformThreadTime / virtualThreadTime);
    }

    /**
     * 大规模并发演示
     */
    public static void massiveConcurrencyDemo() {
        System.out.println("\n=== 大规模并发演示 ===");
        
        int threadCount = 100000;
        System.out.printf("创建 %d 个虚拟线程...%n", threadCount);
        
        long startTime = System.currentTimeMillis();
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CountDownLatch latch = new CountDownLatch(threadCount);
            
            for (int i = 0; i < threadCount; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try {
                        // 模拟轻量级工作
                        Thread.sleep(100);
                        if (taskId % 10000 == 0) {
                            System.out.printf("任务 %d 完成%n", taskId);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.printf("完成 %d 个虚拟线程，耗时: %d ms%n", threadCount, endTime - startTime);
    }

    /**
     * 结构化并发演示（Java 19+预览功能）
     */
    public static void structuredConcurrencyDemo() {
        System.out.println("\n=== 结构化并发演示 ===");
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // 提交多个相关任务
            Future<String> task1 = executor.submit(() -> {
                try {
                    Thread.sleep(1000);
                    return "任务1完成";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "任务1被中断";
                }
            });
            
            Future<String> task2 = executor.submit(() -> {
                try {
                    Thread.sleep(1500);
                    return "任务2完成";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "任务2被中断";
                }
            });
            
            Future<String> task3 = executor.submit(() -> {
                try {
                    Thread.sleep(800);
                    return "任务3完成";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "任务3被中断";
                }
            });
            
            // 等待所有任务完成
            try {
                System.out.println("等待所有任务完成...");
                System.out.println(task1.get());
                System.out.println(task2.get());
                System.out.println(task3.get());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("任务执行出错: " + e.getMessage());
            }
        }
    }

    /**
     * 测量执行时间的工具方法
     */
    private static long measureExecutionTime(Runnable task) {
        Instant start = Instant.now();
        task.run();
        Instant end = Instant.now();
        return Duration.between(start, end).toMillis();
    }

    /**
     * 主演示方法
     */
    public static void main(String[] args) {
        System.out.println("Java 虚拟线程演示");
        System.out.println("Java版本: " + System.getProperty("java.version"));
        System.out.println("=".repeat(50));
        
        // 检查Java版本
        String javaVersion = System.getProperty("java.version");
        if (!javaVersion.startsWith("21") && !javaVersion.startsWith("22") && !javaVersion.startsWith("23")) {
            System.out.println("警告: 虚拟线程需要Java 19+，推荐Java 21+");
        }
        
        basicVirtualThreadDemo();
        performanceComparison();
        massiveConcurrencyDemo();
        structuredConcurrencyDemo();
        
        System.out.println("\n=== 演示完成 ===");
    }
}
