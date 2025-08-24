package org.example.demo1.designpatterns.singleton;

/**
 * 双重检查锁单例模式 (Double-Checked Locking Singleton)
 * 
 * 这是最推荐的单例实现方式之一
 * 
 * 优点：
 * - 线程安全
 * - 延迟初始化
 * - 高性能，只在第一次创建时同步
 * - 避免了每次调用都同步的开销
 * 
 * 关键点：
 * - volatile关键字确保多线程环境下的可见性和有序性
 * - 双重检查避免不必要的同步
 */
public class DoubleCheckedLockingSingleton {
    
    // volatile确保多线程环境下的可见性和禁止指令重排序
    private static volatile DoubleCheckedLockingSingleton instance;
    
    private DoubleCheckedLockingSingleton() {
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
    
    /**
     * 双重检查锁实现
     * 第一次检查：避免不必要的同步
     * 第二次检查：确保只创建一个实例
     */
    public static DoubleCheckedLockingSingleton getInstance() {
        // 第一次检查，如果实例已存在，直接返回，避免同步开销
        if (instance == null) {
            // 同步块，确保线程安全
            synchronized (DoubleCheckedLockingSingleton.class) {
                // 第二次检查，确保在同步块内只创建一个实例
                if (instance == null) {
                    instance = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return instance;
    }
    
    public void doSomething() {
        System.out.println("DoubleCheckedLockingSingleton is doing something...");
    }
    
    // 防止序列化破坏单例
    private Object readResolve() {
        return instance;
    }
    
    @Override
    public String toString() {
        return "DoubleCheckedLockingSingleton{hashCode=" + this.hashCode() + "}";
    }
}
