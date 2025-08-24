package org.example.demo1.designpatterns.observer;

/**
 * 观察者模式 - 观察者接口 (Observer)
 * 
 * 定义了观察者的统一接口，当被观察的主题状态发生变化时，
 * 所有注册的观察者都会收到通知并执行相应的更新操作。
 */
public interface Observer {
    
    /**
     * 更新方法，当主题状态发生变化时被调用
     * 
     * @param subject 发生变化的主题对象
     * @param event 事件类型或变化信息
     * @param data 相关数据，可以为null
     */
    void update(Subject subject, String event, Object data);
    
    /**
     * 获取观察者的名称或标识
     * 
     * @return 观察者名称
     */
    String getName();
    
    /**
     * 检查观察者是否对特定事件感兴趣
     * 默认实现返回true，表示对所有事件都感兴趣
     * 
     * @param event 事件类型
     * @return 如果感兴趣返回true，否则返回false
     */
    default boolean isInterestedIn(String event) {
        return true;
    }
}
