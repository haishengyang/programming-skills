package org.example.demo1.designpatterns.singleton;

/**
 * 枚举单例模式 (Enum Singleton)
 * 
 * 这是《Effective Java》作者Josh Bloch推荐的单例实现方式
 * 
 * 优点：
 * - 线程安全（JVM保证）
 * - 防止反射攻击（枚举类型不能通过反射创建实例）
 * - 防止序列化破坏单例（枚举序列化由JVM保证）
 * - 实现简洁
 * - 天然支持序列化
 * 
 * 缺点：
 * - 不支持延迟初始化
 * - 可读性相对较差（不够直观）
 */
public enum EnumSingleton {
    
    INSTANCE;
    
    // 可以添加实例变量
    private String data;
    
    // 构造方法（枚举的构造方法默认是private的）
    EnumSingleton() {
        this.data = "EnumSingleton initialized";
        
        // 模拟初始化操作
        System.out.println("EnumSingleton instance created");
    }
    
    // 业务方法
    public void doSomething() {
        System.out.println("EnumSingleton is doing something... Data: " + data);
    }
    
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "EnumSingleton{data='" + data + "', hashCode=" + this.hashCode() + "}";
    }
}
