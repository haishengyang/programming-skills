package org.example.demo1.designpatterns.singleton;

/**
 * 线程安全的懒汉式单例模式 (Thread-Safe Lazy Singleton)
 * 
 * 优点：
 * - 线程安全
 * - 延迟初始化
 * 
 * 缺点：
 * - 每次调用getInstance()都需要同步，性能较差
 * - 同步开销大
 */
public class ThreadSafeSingleton {
    
    private static ThreadSafeSingleton instance;
    
    private ThreadSafeSingleton() {
        // 防止反射攻击
        if (instance != null) {
            throw new IllegalStateException("Singleton instance already exists!");
        }
        
        // 模拟初始化耗时操作
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // 同步方法，确保线程安全
    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }
    
    public void doSomething() {
        System.out.println("ThreadSafeSingleton is doing something...");
    }
    
    @Override
    public String toString() {
        return "ThreadSafeSingleton{hashCode=" + this.hashCode() + "}";
    }
}
