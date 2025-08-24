package org.example.demo1.designpatterns.observer;

/**
 * 观察者模式演示类
 * 展示天气站和各种显示器之间的观察者模式实现
 */
public class ObserverDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 观察者模式演示 - 天气监测系统 ===\n");
        
        // 创建天气站（被观察者）
        WeatherStation weatherStation = new WeatherStation("Beijing Weather Station");
        
        // 创建各种显示器（观察者）
        WeatherDisplay currentDisplay = new WeatherDisplay("Current Conditions Display");
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay("Statistics Display");
        ForecastDisplay forecastDisplay = new ForecastDisplay("Weather Forecast Display");
        
        System.out.println("=== 1. 注册观察者 ===");
        // 注册观察者
        weatherStation.registerObserver(currentDisplay);
        weatherStation.registerObserver(statisticsDisplay);
        weatherStation.registerObserver(forecastDisplay);
        
        System.out.println("Total observers registered: " + weatherStation.getObserverCount());
        System.out.println();
        
        // 模拟天气数据变化
        System.out.println("=== 2. 第一次天气数据更新 ===");
        weatherStation.setMeasurements(25.5f, 65.0f, 1013.2f);
        
        System.out.println("\n=== 3. 第二次天气数据更新 ===");
        weatherStation.setMeasurements(27.8f, 70.0f, 1012.0f);
        
        System.out.println("\n=== 4. 第三次天气数据更新（气压显著下降）===");
        weatherStation.setMeasurements(29.2f, 78.0f, 1008.5f);
        
        System.out.println("\n=== 5. 极端天气条件 ===");
        weatherStation.setMeasurements(42.0f, 95.0f, 975.0f);
        
        System.out.println("\n=== 6. 天气好转 ===");
        weatherStation.setMeasurements(24.0f, 55.0f, 1020.5f);
        
        // 演示观察者的动态添加和移除
        System.out.println("\n=== 7. 动态观察者管理 ===");
        
        // 创建新的观察者
        WeatherDisplay mobileDisplay = new WeatherDisplay("Mobile Weather App");
        weatherStation.registerObserver(mobileDisplay);
        
        System.out.println("Added new observer. Total observers: " + weatherStation.getObserverCount());
        
        // 更新天气数据，新观察者也会收到通知
        System.out.println("\n--- Weather update with new observer ---");
        weatherStation.setMeasurements(22.0f, 60.0f, 1015.0f);
        
        // 移除一个观察者
        System.out.println("\n--- Removing an observer ---");
        weatherStation.removeObserver(statisticsDisplay);
        System.out.println("Removed statistics display. Total observers: " + weatherStation.getObserverCount());
        
        // 再次更新天气数据
        System.out.println("\n--- Weather update after removing observer ---");
        weatherStation.setMeasurements(20.0f, 58.0f, 1018.0f);
        
        // 演示错误处理
        System.out.println("\n=== 8. 错误处理演示 ===");
        demonstrateErrorHandling(weatherStation);
        
        // 演示多线程安全性
        System.out.println("\n=== 9. 多线程安全性演示 ===");
        demonstrateThreadSafety(weatherStation);
        
        // 清理
        System.out.println("\n=== 10. 清理观察者 ===");
        weatherStation.clearObservers();
        System.out.println("All observers cleared. Total observers: " + weatherStation.getObserverCount());
        
        // 尝试通知（应该没有观察者）
        weatherStation.setMeasurements(25.0f, 60.0f, 1013.0f);
        
        System.out.println("\n=== 观察者模式演示结束 ===");
    }
    
    /**
     * 演示错误处理
     */
    private static void demonstrateErrorHandling(WeatherStation weatherStation) {
        // 创建一个会抛出异常的观察者
        Observer faultyObserver = new Observer() {
            @Override
            public void update(Subject subject, String event, Object data) {
                System.out.println("Faulty observer received update: " + event);
                throw new RuntimeException("Simulated observer error");
            }
            
            @Override
            public String getName() {
                return "Faulty Observer";
            }
        };
        
        weatherStation.registerObserver(faultyObserver);
        
        // 这次更新会触发异常，但不应该影响其他观察者
        System.out.println("--- Triggering update with faulty observer ---");
        weatherStation.setMeasurements(23.0f, 62.0f, 1016.0f);
        
        // 移除有问题的观察者
        weatherStation.removeObserver(faultyObserver);
    }
    
    /**
     * 演示多线程安全性
     */
    private static void demonstrateThreadSafety(WeatherStation weatherStation) {
        System.out.println("Testing thread safety with concurrent updates...");
        
        // 创建多个线程同时更新天气数据
        Thread[] threads = new Thread[3];
        
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 2; j++) {
                    float temp = 20.0f + threadId + j;
                    float humidity = 50.0f + threadId * 5 + j;
                    float pressure = 1010.0f + threadId + j;
                    
                    System.out.println("Thread " + threadId + " updating weather data...");
                    weatherStation.setMeasurements(temp, humidity, pressure);
                    
                    try {
                        Thread.sleep(100); // 短暂休眠
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
        
        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("Thread safety test completed.");
    }
}
