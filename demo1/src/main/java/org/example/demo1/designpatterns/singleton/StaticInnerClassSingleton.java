package org.example.demo1.designpatterns.singleton;

/**
 * 静态内部类单例模式 (Static Inner Class Singleton)
 * 
 * 这是最推荐的单例实现方式，被称为"最优雅的单例实现"
 * 
 * 优点：
 * - 线程安全（JVM保证类加载的线程安全）
 * - 延迟初始化（只有调用getInstance时才加载内部类）
 * - 高性能（无同步开销）
 * - 实现简洁优雅
 * 
 * 原理：
 * - 利用JVM的类加载机制保证线程安全
 * - 静态内部类只有在被引用时才会被加载
 * - 类加载过程是线程安全的
 */
public class StaticInnerClassSingleton {
    
    private StaticInnerClassSingleton() {
        // 防止反射攻击
        if (SingletonHolder.INSTANCE != null) {
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
     * 静态内部类，只有在被引用时才会被加载
     * JVM保证类加载过程的线程安全性
     */
    private static class SingletonHolder {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }
    
    /**
     * 获取单例实例
     * 只有调用这个方法时，SingletonHolder类才会被加载，从而创建实例
     */
    public static StaticInnerClassSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    public void doSomething() {
        System.out.println("StaticInnerClassSingleton is doing something...");
    }
    
    // 防止序列化破坏单例
    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }
    
    @Override
    public String toString() {
        return "StaticInnerClassSingleton{hashCode=" + this.hashCode() + "}";
    }
}
