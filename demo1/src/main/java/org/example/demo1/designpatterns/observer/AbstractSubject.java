package org.example.demo1.designpatterns.observer;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 观察者模式 - 抽象主题类 (Abstract Subject)
 * 
 * 提供了主题接口的默认实现，包括观察者的管理和通知机制。
 * 使用线程安全的集合和读写锁来支持多线程环境。
 */
public abstract class AbstractSubject implements Subject {
    
    // 使用CopyOnWriteArrayList保证线程安全，适合读多写少的场景
    private final List<Observer> observers = new CopyOnWriteArrayList<>();
    
    // 读写锁，用于保护观察者列表的并发访问
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    
    @Override
    public boolean registerObserver(Observer observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }
        
        writeLock.lock();
        try {
            // 检查观察者是否已存在
            if (observers.contains(observer)) {
                System.out.println("Observer " + observer.getName() + " is already registered");
                return false;
            }
            
            boolean added = observers.add(observer);
            if (added) {
                System.out.println("Observer " + observer.getName() + " registered successfully");
                onObserverRegistered(observer);
            }
            return added;
        } finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public boolean removeObserver(Observer observer) {
        if (observer == null) {
            return false;
        }
        
        writeLock.lock();
        try {
            boolean removed = observers.remove(observer);
            if (removed) {
                System.out.println("Observer " + observer.getName() + " removed successfully");
                onObserverRemoved(observer);
            } else {
                System.out.println("Observer " + observer.getName() + " not found");
            }
            return removed;
        } finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public void notifyObservers(String event, Object data) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        
        readLock.lock();
        try {
            if (observers.isEmpty()) {
                System.out.println("No observers to notify for event: " + event);
                return;
            }
            
            System.out.println("Notifying " + observers.size() + " observers about event: " + event);
            
            // 通知所有感兴趣的观察者
            for (Observer observer : observers) {
                try {
                    if (observer.isInterestedIn(event)) {
                        observer.update(this, event, data);
                    }
                } catch (Exception e) {
                    // 捕获观察者更新时的异常，避免影响其他观察者
                    System.err.println("Error notifying observer " + observer.getName() + ": " + e.getMessage());
                    onObserverError(observer, e);
                }
            }
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public int getObserverCount() {
        readLock.lock();
        try {
            return observers.size();
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 获取所有观察者的副本（防止外部修改）
     * 
     * @return 观察者列表的副本
     */
    protected List<Observer> getObservers() {
        readLock.lock();
        try {
            return List.copyOf(observers);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 清除所有观察者
     */
    public void clearObservers() {
        writeLock.lock();
        try {
            int count = observers.size();
            observers.clear();
            System.out.println("Cleared " + count + " observers");
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 当观察者注册时的回调方法，子类可以重写
     * 
     * @param observer 注册的观察者
     */
    protected void onObserverRegistered(Observer observer) {
        // 默认空实现，子类可以重写
    }
    
    /**
     * 当观察者移除时的回调方法，子类可以重写
     * 
     * @param observer 移除的观察者
     */
    protected void onObserverRemoved(Observer observer) {
        // 默认空实现，子类可以重写
    }
    
    /**
     * 当观察者更新时发生错误的回调方法，子类可以重写
     * 
     * @param observer 发生错误的观察者
     * @param error 错误信息
     */
    protected void onObserverError(Observer observer, Exception error) {
        // 默认空实现，子类可以重写
    }
}
