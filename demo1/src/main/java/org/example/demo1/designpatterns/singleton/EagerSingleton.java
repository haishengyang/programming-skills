package org.example.demo1.designpatterns.singleton;

/**
 * 饿汉式单例模式 (Eager Initialization Singleton)
 * 
 * 优点：
 * - 线程安全，无需同步
 * - 实现简单
 * - 类加载时就创建实例，避免了多线程同步问题
 * 
 * 缺点：
 * - 不管是否使用都会创建实例，可能造成内存浪费
 * - 无法延迟初始化
 */
public class EagerSingleton {
    
    // 在类加载时就创建实例
    private static final EagerSingleton INSTANCE = new EagerSingleton();
    
    // 私有构造函数，防止外部实例化
    private EagerSingleton() {
        // 防止反射攻击
        if (INSTANCE != null) {
            throw new IllegalStateException("Singleton instance already exists!");
        }
    }
    
    // 提供全局访问点
    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
    
    // 业务方法示例
    public void doSomething() {
        System.out.println("EagerSingleton is doing something...");
    }
    
    // 防止序列化破坏单例
    private Object readResolve() {
        return INSTANCE;
    }
    
    @Override
    public String toString() {
        return "EagerSingleton{hashCode=" + this.hashCode() + "}";
    }
}
