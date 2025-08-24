package org.example.demo1.designpatterns.observer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 观察者模式 - 具体观察者类 (Concrete Observer)
 * 
 * 天气显示器类，实现观察者接口，用于显示当前天气信息。
 * 这是一个基础的天气显示观察者。
 */
public class WeatherDisplay implements Observer {
    
    private final String name;
    private WeatherStation.WeatherData currentWeatherData;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public WeatherDisplay(String name) {
        this.name = name;
    }
    
    @Override
    public void update(Subject subject, String event, Object data) {
        System.out.println("\n=== " + name + " Update ===");
        System.out.println("Event: " + event);
        System.out.println("Time: " + dateFormat.format(new Date()));
        
        if (subject instanceof WeatherStation) {
            WeatherStation station = (WeatherStation) subject;
            
            switch (event) {
                case WeatherStation.EVENT_MEASUREMENTS_CHANGED:
                    if (data instanceof WeatherStation.WeatherData) {
                        this.currentWeatherData = (WeatherStation.WeatherData) data;
                        displayCurrentConditions();
                    }
                    break;
                    
                case WeatherStation.EVENT_TEMPERATURE_CHANGED:
                    System.out.println("🌡️ Temperature updated: " + data + "°C");
                    break;
                    
                case WeatherStation.EVENT_HUMIDITY_CHANGED:
                    System.out.println("💧 Humidity updated: " + data + "%");
                    break;
                    
                case WeatherStation.EVENT_PRESSURE_CHANGED:
                    System.out.println("🌪️ Pressure updated: " + data + " hPa");
                    break;
                    
                case WeatherStation.EVENT_WEATHER_CHANGED:
                    System.out.println("🌤️ Weather condition changed to: " + data);
                    break;
                    
                case WeatherStation.EVENT_EXTREME_WEATHER:
                    if (data instanceof WeatherStation.ExtremeWeatherData) {
                        displayExtremeWeatherAlert((WeatherStation.ExtremeWeatherData) data);
                    }
                    break;
                    
                default:
                    System.out.println("Unknown event received: " + event);
                    break;
            }
        }
        
        System.out.println("=== End " + name + " Update ===\n");
    }
    
    /**
     * 显示当前天气状况
     */
    private void displayCurrentConditions() {
        if (currentWeatherData == null) {
            System.out.println("No weather data available");
            return;
        }
        
        System.out.println("📊 Current Weather Conditions:");
        System.out.println("   Location: " + currentWeatherData.getLocation());
        System.out.println("   Temperature: " + currentWeatherData.getTemperature() + "°C");
        System.out.println("   Humidity: " + currentWeatherData.getHumidity() + "%");
        System.out.println("   Pressure: " + currentWeatherData.getPressure() + " hPa");
        System.out.println("   Condition: " + currentWeatherData.getCondition());
        System.out.println("   Comfort Level: " + getComfortLevel());
    }
    
    /**
     * 显示极端天气警报
     */
    private void displayExtremeWeatherAlert(WeatherStation.ExtremeWeatherData extremeData) {
        System.out.println("🚨 " + extremeData.getAlertMessage());
        System.out.println("   Temperature: " + extremeData.getTemperature() + "°C");
        System.out.println("   Humidity: " + extremeData.getHumidity() + "%");
        System.out.println("   Pressure: " + extremeData.getPressure() + " hPa");
        System.out.println("   Please take necessary precautions!");
    }
    
    /**
     * 根据当前天气数据计算舒适度等级
     */
    private String getComfortLevel() {
        if (currentWeatherData == null) {
            return "Unknown";
        }
        
        float temp = currentWeatherData.getTemperature();
        float humidity = currentWeatherData.getHumidity();
        
        if (temp >= 20 && temp <= 26 && humidity >= 40 && humidity <= 60) {
            return "Very Comfortable 😊";
        } else if (temp >= 18 && temp <= 28 && humidity >= 30 && humidity <= 70) {
            return "Comfortable 🙂";
        } else if (temp >= 15 && temp <= 32 && humidity >= 25 && humidity <= 80) {
            return "Acceptable 😐";
        } else if (temp >= 10 && temp <= 35 && humidity >= 20 && humidity <= 85) {
            return "Uncomfortable 😕";
        } else {
            return "Very Uncomfortable 😰";
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * 获取当前天气数据
     */
    public WeatherStation.WeatherData getCurrentWeatherData() {
        return currentWeatherData;
    }
    
    @Override
    public String toString() {
        return "WeatherDisplay{name='" + name + "'}";
    }
}
