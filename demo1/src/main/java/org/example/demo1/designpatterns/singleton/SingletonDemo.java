package org.example.demo1.designpatterns.singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 单例模式演示类
 * 展示各种单例模式的使用和线程安全测试
 */
public class SingletonDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 单例模式演示 ===\n");
        
        // 1. 饿汉式单例演示
        demonstrateEagerSingleton();
        
        // 2. 懒汉式单例演示（线程不安全）
        demonstrateLazySingleton();
        
        // 3. 线程安全的懒汉式单例演示
        demonstrateThreadSafeSingleton();
        
        // 4. 双重检查锁单例演示
        demonstrateDoubleCheckedLockingSingleton();
        
        // 5. 静态内部类单例演示
        demonstrateStaticInnerClassSingleton();
        
        // 6. 枚举单例演示
        demonstrateEnumSingleton();
        
        // 7. 多线程测试
        System.out.println("\n=== 多线程安全性测试 ===");
        testThreadSafety();
    }
    
    private static void demonstrateEagerSingleton() {
        System.out.println("1. 饿汉式单例模式：");
        EagerSingleton instance1 = EagerSingleton.getInstance();
        EagerSingleton instance2 = EagerSingleton.getInstance();
        
        System.out.println("Instance1: " + instance1);
        System.out.println("Instance2: " + instance2);
        System.out.println("Same instance? " + (instance1 == instance2));
        instance1.doSomething();
        System.out.println();
    }
    
    private static void demonstrateLazySingleton() {
        System.out.println("2. 懒汉式单例模式（线程不安全）：");
        LazySingleton instance1 = LazySingleton.getInstance();
        LazySingleton instance2 = LazySingleton.getInstance();
        
        System.out.println("Instance1: " + instance1);
        System.out.println("Instance2: " + instance2);
        System.out.println("Same instance? " + (instance1 == instance2));
        instance1.doSomething();
        System.out.println();
    }
    
    private static void demonstrateThreadSafeSingleton() {
        System.out.println("3. 线程安全的懒汉式单例模式：");
        ThreadSafeSingleton instance1 = ThreadSafeSingleton.getInstance();
        ThreadSafeSingleton instance2 = ThreadSafeSingleton.getInstance();
        
        System.out.println("Instance1: " + instance1);
        System.out.println("Instance2: " + instance2);
        System.out.println("Same instance? " + (instance1 == instance2));
        instance1.doSomething();
        System.out.println();
    }
    
    private static void demonstrateDoubleCheckedLockingSingleton() {
        System.out.println("4. 双重检查锁单例模式：");
        DoubleCheckedLockingSingleton instance1 = DoubleCheckedLockingSingleton.getInstance();
        DoubleCheckedLockingSingleton instance2 = DoubleCheckedLockingSingleton.getInstance();
        
        System.out.println("Instance1: " + instance1);
        System.out.println("Instance2: " + instance2);
        System.out.println("Same instance? " + (instance1 == instance2));
        instance1.doSomething();
        System.out.println();
    }
    
    private static void demonstrateStaticInnerClassSingleton() {
        System.out.println("5. 静态内部类单例模式：");
        StaticInnerClassSingleton instance1 = StaticInnerClassSingleton.getInstance();
        StaticInnerClassSingleton instance2 = StaticInnerClassSingleton.getInstance();
        
        System.out.println("Instance1: " + instance1);
        System.out.println("Instance2: " + instance2);
        System.out.println("Same instance? " + (instance1 == instance2));
        instance1.doSomething();
        System.out.println();
    }
    
    private static void demonstrateEnumSingleton() {
        System.out.println("6. 枚举单例模式：");
        EnumSingleton instance1 = EnumSingleton.INSTANCE;
        EnumSingleton instance2 = EnumSingleton.INSTANCE;
        
        System.out.println("Instance1: " + instance1);
        System.out.println("Instance2: " + instance2);
        System.out.println("Same instance? " + (instance1 == instance2));
        instance1.doSomething();
        System.out.println();
    }
    
    /**
     * 多线程安全性测试
     */
    private static void testThreadSafety() {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        System.out.println("测试双重检查锁单例的线程安全性：");
        
        // 测试双重检查锁单例
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                DoubleCheckedLockingSingleton instance = DoubleCheckedLockingSingleton.getInstance();
                System.out.println("Thread " + threadId + " got instance: " + instance.hashCode());
            });
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n测试静态内部类单例的线程安全性：");
        ExecutorService executor2 = Executors.newFixedThreadPool(threadCount);
        
        // 测试静态内部类单例
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor2.submit(() -> {
                StaticInnerClassSingleton instance = StaticInnerClassSingleton.getInstance();
                System.out.println("Thread " + threadId + " got instance: " + instance.hashCode());
            });
        }
        
        executor2.shutdown();
        try {
            executor2.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
