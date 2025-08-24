package org.example.demo1.designpatterns.observer;

/**
 * 观察者模式 - 主题接口 (Subject)
 * 
 * 定义了被观察主题的统一接口，提供注册、移除和通知观察者的方法。
 * 主题维护一个观察者列表，当状态发生变化时通知所有观察者。
 */
public interface Subject {
    
    /**
     * 注册观察者
     * 
     * @param observer 要注册的观察者
     * @return 如果注册成功返回true，如果观察者已存在返回false
     */
    boolean registerObserver(Observer observer);
    
    /**
     * 移除观察者
     * 
     * @param observer 要移除的观察者
     * @return 如果移除成功返回true，如果观察者不存在返回false
     */
    boolean removeObserver(Observer observer);
    
    /**
     * 通知所有观察者
     * 
     * @param event 事件类型
     * @param data 相关数据
     */
    void notifyObservers(String event, Object data);
    
    /**
     * 获取当前注册的观察者数量
     * 
     * @return 观察者数量
     */
    int getObserverCount();
    
    /**
     * 检查是否有观察者注册
     * 
     * @return 如果有观察者返回true，否则返回false
     */
    default boolean hasObservers() {
        return getObserverCount() > 0;
    }
}
