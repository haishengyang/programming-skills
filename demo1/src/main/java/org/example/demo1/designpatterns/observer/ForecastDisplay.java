package org.example.demo1.designpatterns.observer;

import java.util.HashSet;
import java.util.Set;

/**
 * 观察者模式 - 具体观察者类 (Concrete Observer)
 * 
 * 天气预报显示器类，基于当前天气数据和气压变化趋势
 * 提供简单的天气预报功能。
 */
public class ForecastDisplay implements Observer {
    
    private final String name;
    private float currentPressure = 1013.25f; // 标准大气压
    private float lastPressure = 1013.25f;
    private String currentForecast = "More of the same";
    
    // 感兴趣的事件类型
    private final Set<String> interestedEvents = new HashSet<>();
    
    public ForecastDisplay(String name) {
        this.name = name;
        
        // 设置感兴趣的事件
        interestedEvents.add(WeatherStation.EVENT_MEASUREMENTS_CHANGED);
        interestedEvents.add(WeatherStation.EVENT_PRESSURE_CHANGED);
        interestedEvents.add(WeatherStation.EVENT_EXTREME_WEATHER);
    }
    
    @Override
    public void update(Subject subject, String event, Object data) {
        if (subject instanceof WeatherStation) {
            WeatherStation station = (WeatherStation) subject;
            
            switch (event) {
                case WeatherStation.EVENT_MEASUREMENTS_CHANGED:
                    if (data instanceof WeatherStation.WeatherData) {
                        updateForecast((WeatherStation.WeatherData) data);
                        displayForecast();
                    }
                    break;
                    
                case WeatherStation.EVENT_PRESSURE_CHANGED:
                    System.out.println("\n=== " + name + " Pressure Alert ===");
                    System.out.println("🌪️ Pressure change detected: " + data + " hPa");
                    updatePressureForecast((Float) data);
                    displayForecast();
                    System.out.println("=== End " + name + " Pressure Alert ===\n");
                    break;
                    
                case WeatherStation.EVENT_EXTREME_WEATHER:
                    System.out.println("\n=== " + name + " Extreme Weather Forecast ===");
                    System.out.println("🚨 Extreme weather conditions detected!");
                    displayExtremeForecast();
                    System.out.println("=== End " + name + " Extreme Weather Forecast ===\n");
                    break;
            }
        }
    }
    
    @Override
    public boolean isInterestedIn(String event) {
        return interestedEvents.contains(event);
    }
    
    /**
     * 更新天气预报
     */
    private void updateForecast(WeatherStation.WeatherData weatherData) {
        lastPressure = currentPressure;
        currentPressure = weatherData.getPressure();
        
        currentForecast = generateForecast(weatherData);
    }
    
    /**
     * 基于气压变化更新预报
     */
    private void updatePressureForecast(float newPressure) {
        lastPressure = currentPressure;
        currentPressure = newPressure;
        
        // 基于气压变化生成预报
        float pressureChange = currentPressure - lastPressure;
        
        if (pressureChange > 2.0f) {
            currentForecast = "Weather improving - expect clearer skies";
        } else if (pressureChange < -2.0f) {
            currentForecast = "Weather deteriorating - expect storms";
        } else if (Math.abs(pressureChange) < 0.5f) {
            currentForecast = "Weather conditions stable";
        }
    }
    
    /**
     * 生成天气预报
     */
    private String generateForecast(WeatherStation.WeatherData weatherData) {
        float pressureChange = currentPressure - lastPressure;
        float temperature = weatherData.getTemperature();
        float humidity = weatherData.getHumidity();
        String condition = weatherData.getCondition();
        
        // 基于多个因素生成预报
        StringBuilder forecast = new StringBuilder();
        
        // 基于气压变化
        if (pressureChange > 2.0f) {
            forecast.append("Improving weather ahead - ");
            if (currentPressure > 1020) {
                forecast.append("expect sunny and clear conditions");
            } else {
                forecast.append("expect partly cloudy skies");
            }
        } else if (pressureChange < -2.0f) {
            forecast.append("Deteriorating weather - ");
            if (currentPressure < 1000) {
                forecast.append("storms likely");
            } else {
                forecast.append("expect clouds and possible rain");
            }
        } else {
            // 基于当前条件
            if (currentPressure > 1020 && humidity < 50) {
                forecast.append("Continued clear and dry weather");
            } else if (currentPressure < 1000 && humidity > 80) {
                forecast.append("Continued unsettled weather with possible precipitation");
            } else if (temperature > 30 && humidity > 70) {
                forecast.append("Hot and humid - possible thunderstorms");
            } else if (temperature < 5 && humidity > 80) {
                forecast.append("Cold and damp - possible frost or snow");
            } else {
                forecast.append("More of the same weather conditions");
            }
        }
        
        // 添加时间范围
        forecast.append(" over the next 6-12 hours");
        
        return forecast.toString();
    }
    
    /**
     * 显示天气预报
     */
    private void displayForecast() {
        System.out.println("\n=== " + name + " Forecast ===");
        System.out.println("🔮 Weather Forecast:");
        System.out.println("   " + currentForecast);
        
        // 显示预报依据
        float pressureChange = currentPressure - lastPressure;
        System.out.println("📊 Forecast Basis:");
        System.out.printf("   Current Pressure: %.1f hPa%n", currentPressure);
        System.out.printf("   Pressure Change: %+.1f hPa%n", pressureChange);
        System.out.println("   Trend: " + getPressureTrend(pressureChange));
        
        // 可信度评估
        System.out.println("   Confidence: " + getConfidenceLevel(pressureChange));
        
        System.out.println("=== End " + name + " Forecast ===\n");
    }
    
    /**
     * 显示极端天气预报
     */
    private void displayExtremeForecast() {
        System.out.println("⚠️ Extreme Weather Forecast:");
        System.out.println("   Conditions are expected to remain extreme for the next few hours");
        System.out.println("   Please monitor weather updates closely and take appropriate precautions");
        
        if (currentPressure < 980) {
            System.out.println("   Very low pressure - severe storms possible");
        } else if (currentPressure > 1040) {
            System.out.println("   Very high pressure - stable but potentially extreme temperatures");
        }
    }
    
    /**
     * 获取气压趋势描述
     */
    private String getPressureTrend(float pressureChange) {
        if (pressureChange > 2.0f) {
            return "Rising rapidly ⬆️⬆️";
        } else if (pressureChange > 0.5f) {
            return "Rising ⬆️";
        } else if (pressureChange < -2.0f) {
            return "Falling rapidly ⬇️⬇️";
        } else if (pressureChange < -0.5f) {
            return "Falling ⬇️";
        } else {
            return "Stable ➡️";
        }
    }
    
    /**
     * 获取预报可信度
     */
    private String getConfidenceLevel(float pressureChange) {
        float absPressureChange = Math.abs(pressureChange);
        
        if (absPressureChange > 5.0f) {
            return "Very High 🟢🟢🟢";
        } else if (absPressureChange > 2.0f) {
            return "High 🟢🟢";
        } else if (absPressureChange > 0.5f) {
            return "Medium 🟡";
        } else {
            return "Low 🔴";
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * 获取当前预报
     */
    public String getCurrentForecast() {
        return currentForecast;
    }
    
    /**
     * 获取气压变化
     */
    public float getPressureChange() {
        return currentPressure - lastPressure;
    }
    
    @Override
    public String toString() {
        return "ForecastDisplay{name='" + name + "', currentPressure=" + currentPressure + "}";
    }
}
