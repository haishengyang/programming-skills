package org.example.demo1.designpatterns.observer;

/**
 * 观察者模式 - 具体主题类 (Concrete Subject)
 * 
 * 天气站类，作为被观察的主题，维护天气数据并在数据变化时通知观察者。
 * 这是观察者模式的经典应用场景。
 */
public class WeatherStation extends AbstractSubject {
    
    // 天气数据
    private float temperature;      // 温度（摄氏度）
    private float humidity;         // 湿度（百分比）
    private float pressure;         // 气压（百帕）
    private String weatherCondition; // 天气状况
    private String location;        // 位置
    
    // 事件类型常量
    public static final String EVENT_TEMPERATURE_CHANGED = "TEMPERATURE_CHANGED";
    public static final String EVENT_HUMIDITY_CHANGED = "HUMIDITY_CHANGED";
    public static final String EVENT_PRESSURE_CHANGED = "PRESSURE_CHANGED";
    public static final String EVENT_WEATHER_CHANGED = "WEATHER_CHANGED";
    public static final String EVENT_MEASUREMENTS_CHANGED = "MEASUREMENTS_CHANGED";
    public static final String EVENT_EXTREME_WEATHER = "EXTREME_WEATHER";
    
    public WeatherStation(String location) {
        this.location = location;
        this.temperature = 0.0f;
        this.humidity = 0.0f;
        this.pressure = 1013.25f; // 标准大气压
        this.weatherCondition = "Unknown";
    }
    
    /**
     * 设置天气测量数据
     * 
     * @param temperature 温度
     * @param humidity 湿度
     * @param pressure 气压
     */
    public void setMeasurements(float temperature, float humidity, float pressure) {
        boolean temperatureChanged = Math.abs(this.temperature - temperature) > 0.1f;
        boolean humidityChanged = Math.abs(this.humidity - humidity) > 1.0f;
        boolean pressureChanged = Math.abs(this.pressure - pressure) > 1.0f;
        
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        
        // 根据数据更新天气状况
        updateWeatherCondition();
        
        // 通知观察者具体的变化
        if (temperatureChanged) {
            notifyObservers(EVENT_TEMPERATURE_CHANGED, temperature);
        }
        if (humidityChanged) {
            notifyObservers(EVENT_HUMIDITY_CHANGED, humidity);
        }
        if (pressureChanged) {
            notifyObservers(EVENT_PRESSURE_CHANGED, pressure);
        }
        
        // 通知观察者整体测量数据变化
        notifyObservers(EVENT_MEASUREMENTS_CHANGED, createWeatherData());
        
        // 检查极端天气
        checkExtremeWeather();
    }
    
    /**
     * 设置天气状况
     * 
     * @param condition 天气状况
     */
    public void setWeatherCondition(String condition) {
        if (!this.weatherCondition.equals(condition)) {
            this.weatherCondition = condition;
            notifyObservers(EVENT_WEATHER_CHANGED, condition);
        }
    }
    
    /**
     * 根据测量数据自动更新天气状况
     */
    private void updateWeatherCondition() {
        String newCondition;
        
        if (temperature > 35) {
            newCondition = "Hot";
        } else if (temperature < 0) {
            newCondition = "Freezing";
        } else if (humidity > 80 && temperature > 20) {
            newCondition = "Humid";
        } else if (humidity < 30) {
            newCondition = "Dry";
        } else if (pressure < 1000) {
            newCondition = "Stormy";
        } else if (pressure > 1020) {
            newCondition = "Clear";
        } else {
            newCondition = "Mild";
        }
        
        setWeatherCondition(newCondition);
    }
    
    /**
     * 检查极端天气条件
     */
    private void checkExtremeWeather() {
        if (temperature > 40 || temperature < -20 || 
            humidity > 95 || pressure < 980 || pressure > 1040) {
            
            ExtremeWeatherData extremeData = new ExtremeWeatherData(
                temperature, humidity, pressure, weatherCondition
            );
            notifyObservers(EVENT_EXTREME_WEATHER, extremeData);
        }
    }
    
    /**
     * 创建天气数据对象
     * 
     * @return 天气数据
     */
    private WeatherData createWeatherData() {
        return new WeatherData(temperature, humidity, pressure, weatherCondition, location);
    }
    
    // Getters
    public float getTemperature() { return temperature; }
    public float getHumidity() { return humidity; }
    public float getPressure() { return pressure; }
    public String getWeatherCondition() { return weatherCondition; }
    public String getLocation() { return location; }
    
    /**
     * 获取当前天气数据
     * 
     * @return 天气数据对象
     */
    public WeatherData getCurrentWeatherData() {
        return createWeatherData();
    }
    
    @Override
    protected void onObserverRegistered(Observer observer) {
        // 新观察者注册时，发送当前天气数据
        try {
            observer.update(this, EVENT_MEASUREMENTS_CHANGED, createWeatherData());
        } catch (Exception e) {
            System.err.println("Error notifying new observer " + observer.getName() + ": " + e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return String.format("WeatherStation{location='%s', temp=%.1f°C, humidity=%.1f%%, pressure=%.1fhPa, condition='%s'}", 
                           location, temperature, humidity, pressure, weatherCondition);
    }
    
    /**
     * 天气数据内部类
     */
    public static class WeatherData {
        private final float temperature;
        private final float humidity;
        private final float pressure;
        private final String condition;
        private final String location;
        private final long timestamp;
        
        public WeatherData(float temperature, float humidity, float pressure, String condition, String location) {
            this.temperature = temperature;
            this.humidity = humidity;
            this.pressure = pressure;
            this.condition = condition;
            this.location = location;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters
        public float getTemperature() { return temperature; }
        public float getHumidity() { return humidity; }
        public float getPressure() { return pressure; }
        public String getCondition() { return condition; }
        public String getLocation() { return location; }
        public long getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return String.format("WeatherData{location='%s', temp=%.1f°C, humidity=%.1f%%, pressure=%.1fhPa, condition='%s'}", 
                               location, temperature, humidity, pressure, condition);
        }
    }
    
    /**
     * 极端天气数据内部类
     */
    public static class ExtremeWeatherData extends WeatherData {
        private final String alertMessage;
        
        public ExtremeWeatherData(float temperature, float humidity, float pressure, String condition) {
            super(temperature, humidity, pressure, condition, "");
            this.alertMessage = generateAlertMessage();
        }
        
        private String generateAlertMessage() {
            StringBuilder alert = new StringBuilder("EXTREME WEATHER ALERT: ");
            
            if (getTemperature() > 40) alert.append("Extreme Heat! ");
            if (getTemperature() < -20) alert.append("Extreme Cold! ");
            if (getHumidity() > 95) alert.append("Extreme Humidity! ");
            if (getPressure() < 980) alert.append("Very Low Pressure! ");
            if (getPressure() > 1040) alert.append("Very High Pressure! ");
            
            return alert.toString();
        }
        
        public String getAlertMessage() { return alertMessage; }
    }
}
