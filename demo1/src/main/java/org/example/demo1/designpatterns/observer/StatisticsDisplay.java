package org.example.demo1.designpatterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式 - 具体观察者类 (Concrete Observer)
 * 
 * 统计显示器类，用于收集和显示天气数据的统计信息，
 * 包括最高温、最低温、平均温度等。
 */
public class StatisticsDisplay implements Observer {
    
    private final String name;
    private final List<Float> temperatureHistory = new ArrayList<>();
    private final List<Float> humidityHistory = new ArrayList<>();
    private final List<Float> pressureHistory = new ArrayList<>();
    
    private float maxTemperature = Float.MIN_VALUE;
    private float minTemperature = Float.MAX_VALUE;
    private float maxHumidity = Float.MIN_VALUE;
    private float minHumidity = Float.MAX_VALUE;
    private float maxPressure = Float.MIN_VALUE;
    private float minPressure = Float.MAX_VALUE;
    
    private int updateCount = 0;
    
    public StatisticsDisplay(String name) {
        this.name = name;
    }
    
    @Override
    public void update(Subject subject, String event, Object data) {
        // 只对测量数据变化事件感兴趣
        if (!WeatherStation.EVENT_MEASUREMENTS_CHANGED.equals(event)) {
            return;
        }
        
        if (data instanceof WeatherStation.WeatherData) {
            WeatherStation.WeatherData weatherData = (WeatherStation.WeatherData) data;
            updateStatistics(weatherData);
            displayStatistics();
        }
    }
    
    @Override
    public boolean isInterestedIn(String event) {
        // 只对测量数据变化事件感兴趣
        return WeatherStation.EVENT_MEASUREMENTS_CHANGED.equals(event);
    }
    
    /**
     * 更新统计数据
     */
    private void updateStatistics(WeatherStation.WeatherData weatherData) {
        updateCount++;
        
        float temperature = weatherData.getTemperature();
        float humidity = weatherData.getHumidity();
        float pressure = weatherData.getPressure();
        
        // 更新温度统计
        temperatureHistory.add(temperature);
        if (temperature > maxTemperature) {
            maxTemperature = temperature;
        }
        if (temperature < minTemperature) {
            minTemperature = temperature;
        }
        
        // 更新湿度统计
        humidityHistory.add(humidity);
        if (humidity > maxHumidity) {
            maxHumidity = humidity;
        }
        if (humidity < minHumidity) {
            minHumidity = humidity;
        }
        
        // 更新气压统计
        pressureHistory.add(pressure);
        if (pressure > maxPressure) {
            maxPressure = pressure;
        }
        if (pressure < minPressure) {
            minPressure = pressure;
        }
    }
    
    /**
     * 显示统计信息
     */
    private void displayStatistics() {
        System.out.println("\n=== " + name + " Statistics ===");
        System.out.println("📈 Weather Statistics (Based on " + updateCount + " readings):");
        
        // 温度统计
        System.out.println("🌡️ Temperature:");
        System.out.printf("   Max: %.1f°C | Min: %.1f°C | Avg: %.1f°C%n", 
                         maxTemperature, minTemperature, getAverageTemperature());
        
        // 湿度统计
        System.out.println("💧 Humidity:");
        System.out.printf("   Max: %.1f%% | Min: %.1f%% | Avg: %.1f%%%n", 
                         maxHumidity, minHumidity, getAverageHumidity());
        
        // 气压统计
        System.out.println("🌪️ Pressure:");
        System.out.printf("   Max: %.1f hPa | Min: %.1f hPa | Avg: %.1f hPa%n", 
                         maxPressure, minPressure, getAveragePressure());
        
        // 趋势分析
        displayTrends();
        
        System.out.println("=== End " + name + " Statistics ===\n");
    }
    
    /**
     * 显示趋势分析
     */
    private void displayTrends() {
        if (temperatureHistory.size() < 2) {
            return;
        }
        
        System.out.println("📊 Trends:");
        
        // 温度趋势
        String tempTrend = getTrend(temperatureHistory);
        System.out.println("   Temperature: " + tempTrend);
        
        // 湿度趋势
        String humidityTrend = getTrend(humidityHistory);
        System.out.println("   Humidity: " + humidityTrend);
        
        // 气压趋势
        String pressureTrend = getTrend(pressureHistory);
        System.out.println("   Pressure: " + pressureTrend);
    }
    
    /**
     * 计算数据趋势
     */
    private String getTrend(List<Float> data) {
        if (data.size() < 2) {
            return "Insufficient data";
        }
        
        int size = data.size();
        int recentCount = Math.min(5, size); // 取最近5个数据点
        
        float recentAvg = 0;
        float previousAvg = 0;
        
        // 计算最近几个数据的平均值
        for (int i = size - recentCount; i < size; i++) {
            recentAvg += data.get(i);
        }
        recentAvg /= recentCount;
        
        // 计算之前几个数据的平均值
        int previousCount = Math.min(recentCount, size - recentCount);
        if (previousCount > 0) {
            for (int i = size - recentCount - previousCount; i < size - recentCount; i++) {
                previousAvg += data.get(i);
            }
            previousAvg /= previousCount;
            
            float difference = recentAvg - previousAvg;
            if (Math.abs(difference) < 0.1) {
                return "Stable ➡️";
            } else if (difference > 0) {
                return "Rising ⬆️ (+" + String.format("%.1f", difference) + ")";
            } else {
                return "Falling ⬇️ (" + String.format("%.1f", difference) + ")";
            }
        }
        
        return "Stable ➡️";
    }
    
    /**
     * 计算平均温度
     */
    private float getAverageTemperature() {
        if (temperatureHistory.isEmpty()) {
            return 0;
        }
        return (float) temperatureHistory.stream().mapToDouble(Float::doubleValue).average().orElse(0);
    }
    
    /**
     * 计算平均湿度
     */
    private float getAverageHumidity() {
        if (humidityHistory.isEmpty()) {
            return 0;
        }
        return (float) humidityHistory.stream().mapToDouble(Float::doubleValue).average().orElse(0);
    }
    
    /**
     * 计算平均气压
     */
    private float getAveragePressure() {
        if (pressureHistory.isEmpty()) {
            return 0;
        }
        return (float) pressureHistory.stream().mapToDouble(Float::doubleValue).average().orElse(0);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * 重置统计数据
     */
    public void resetStatistics() {
        temperatureHistory.clear();
        humidityHistory.clear();
        pressureHistory.clear();
        
        maxTemperature = Float.MIN_VALUE;
        minTemperature = Float.MAX_VALUE;
        maxHumidity = Float.MIN_VALUE;
        minHumidity = Float.MAX_VALUE;
        maxPressure = Float.MIN_VALUE;
        minPressure = Float.MAX_VALUE;
        
        updateCount = 0;
        
        System.out.println(name + " statistics have been reset.");
    }
    
    /**
     * 获取统计摘要
     */
    public String getStatisticsSummary() {
        if (updateCount == 0) {
            return "No data collected yet";
        }
        
        return String.format("Statistics Summary: %d readings, Temp(%.1f-%.1f°C), Humidity(%.1f-%.1f%%), Pressure(%.1f-%.1f hPa)",
                           updateCount, minTemperature, maxTemperature, 
                           minHumidity, maxHumidity, minPressure, maxPressure);
    }
    
    @Override
    public String toString() {
        return "StatisticsDisplay{name='" + name + "', readings=" + updateCount + "}";
    }
}
