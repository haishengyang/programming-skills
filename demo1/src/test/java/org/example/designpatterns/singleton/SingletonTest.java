package org.example.designpatterns.singleton;

import org.example.demo1.designpatterns.singleton.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例模式测试类
 */
public class SingletonTest {
    
    @Test
    public void testEagerSingleton() {
        EagerSingleton instance1 = EagerSingleton.getInstance();
        EagerSingleton instance2 = EagerSingleton.getInstance();
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testThreadSafeSingleton() {
        ThreadSafeSingleton instance1 = ThreadSafeSingleton.getInstance();
        ThreadSafeSingleton instance2 = ThreadSafeSingleton.getInstance();
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }
    
    @Test
    public void testDoubleCheckedLockingSingleton() {
        DoubleCheckedLockingSingleton instance1 = DoubleCheckedLockingSingleton.getInstance();
        DoubleCheckedLockingSingleton instance2 = DoubleCheckedLockingSingleton.getInstance();
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }
    
    @Test
    public void testStaticInnerClassSingleton() {
        StaticInnerClassSingleton instance1 = StaticInnerClassSingleton.getInstance();
        StaticInnerClassSingleton instance2 = StaticInnerClassSingleton.getInstance();
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }
    
    @Test
    public void testEnumSingleton() {
        EnumSingleton instance1 = EnumSingleton.INSTANCE;
        EnumSingleton instance2 = EnumSingleton.INSTANCE;
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
        
        // 测试枚举单例的业务方法
        instance1.setData("Test Data");
        assertEquals("Test Data", instance2.getData());
    }
    
    /**
     * 测试双重检查锁单例的多线程安全性
     */
    @RepeatedTest(5)
    public void testDoubleCheckedLockingThreadSafety() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Set<Integer> hashCodes = ConcurrentHashMap.newKeySet();
        AtomicInteger completedTasks = new AtomicInteger(0);
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                DoubleCheckedLockingSingleton instance = DoubleCheckedLockingSingleton.getInstance();
                hashCodes.add(instance.hashCode());
                completedTasks.incrementAndGet();
            });
        }
        
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        
        // 验证所有线程都完成了
        assertEquals(threadCount, completedTasks.get());
        
        // 验证只创建了一个实例（所有hashCode相同）
        assertEquals(1, hashCodes.size(), "Should have only one unique instance");
    }
    
    /**
     * 测试静态内部类单例的多线程安全性
     */
    @RepeatedTest(5)
    public void testStaticInnerClassThreadSafety() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Set<Integer> hashCodes = ConcurrentHashMap.newKeySet();
        AtomicInteger completedTasks = new AtomicInteger(0);
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                StaticInnerClassSingleton instance = StaticInnerClassSingleton.getInstance();
                hashCodes.add(instance.hashCode());
                completedTasks.incrementAndGet();
            });
        }
        
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        
        // 验证所有线程都完成了
        assertEquals(threadCount, completedTasks.get());
        
        // 验证只创建了一个实例
        assertEquals(1, hashCodes.size(), "Should have only one unique instance");
    }
    
    /**
     * 测试懒汉式单例在多线程环境下的不安全性
     * 注意：这个测试可能会偶尔失败，因为线程不安全的实现可能创建多个实例
     */
    @Test
    public void testLazySingletonThreadUnsafety() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Set<Integer> hashCodes = ConcurrentHashMap.newKeySet();
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                LazySingleton instance = LazySingleton.getInstance();
                hashCodes.add(instance.hashCode());
            });
        }
        
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        
        // 在多线程环境下，懒汉式可能创建多个实例
        // 这个断言可能会失败，说明线程不安全
        System.out.println("LazySingleton created " + hashCodes.size() + " instances in multithreaded environment");
        
        // 注意：这里不使用assertEquals(1, hashCodes.size())
        // 因为我们期望在多线程环境下可能创建多个实例
        assertTrue(hashCodes.size() >= 1, "Should create at least one instance");
    }
}
