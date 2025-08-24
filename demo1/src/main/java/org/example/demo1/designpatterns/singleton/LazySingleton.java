package org.example.demo1.designpatterns.singleton;

/**
 * 懒汉式单例模式 (Lazy Initialization Singleton)
 * 
 * 优点：
 * - 延迟初始化，只有在需要时才创建实例
 * - 节省内存
 * 
 * 缺点：
 * - 线程不安全，多线程环境下可能创建多个实例
 * - 需要同步处理，影响性能
 * 
 * 注意：这个版本是线程不安全的，仅用于演示，实际项目中不推荐使用
 */
public class LazySingleton {
    
    private static LazySingleton instance;
    
    // 私有构造函数
    private LazySingleton() {
        // 模拟初始化耗时操作
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // 线程不安全的获取实例方法
    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
    
    public void doSomething() {
        System.out.println("LazySingleton is doing something...");
    }
    
    @Override
    public String toString() {
        return "LazySingleton{hashCode=" + this.hashCode() + "}";
    }
}
