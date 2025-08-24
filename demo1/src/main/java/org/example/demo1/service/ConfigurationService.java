package org.example.demo1.service;

import org.example.demo1.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置服务类，演示如何使用APT生成的配置属性
 */
@Service
public class ConfigurationService {

    private final AppProperties appProperties;

    @Autowired
    public ConfigurationService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * 获取应用基本信息
     */
    public Map<String, Object> getAppInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", appProperties.getName());
        info.put("version", appProperties.getVersion());
        info.put("description", appProperties.getDescription());
        return info;
    }

    /**
     * 获取服务器配置信息
     */
    public Map<String, Object> getServerConfig() {
        Map<String, Object> config = new HashMap<>();
        AppProperties.Server server = appProperties.getServer();
        config.put("host", server.getHost());
        config.put("port", server.getPort());
        config.put("timeout", server.getTimeout());
        config.put("sslEnabled", server.isSslEnabled());
        return config;
    }

    /**
     * 获取数据库配置信息（隐藏敏感信息）
     */
    public Map<String, Object> getDatabaseConfig() {
        Map<String, Object> config = new HashMap<>();
        AppProperties.Database database = appProperties.getDatabase();
        config.put("url", database.getUrl());
        config.put("username", database.getUsername());
        config.put("password", "***"); // 隐藏密码
        config.put("maxConnections", database.getMaxConnections());
        config.put("showSql", database.isShowSql());
        return config;
    }

    /**
     * 获取缓存配置信息
     */
    public Map<String, Object> getCacheConfig() {
        Map<String, Object> config = new HashMap<>();
        AppProperties.Cache cache = appProperties.getCache();
        config.put("type", cache.getType());
        config.put("expireMinutes", cache.getExpireMinutes());
        config.put("maxEntries", cache.getMaxEntries());
        config.put("enabled", cache.isEnabled());
        return config;
    }

    /**
     * 获取所有配置信息
     */
    public Map<String, Object> getAllConfigurations() {
        Map<String, Object> allConfig = new HashMap<>();
        allConfig.put("app", getAppInfo());
        allConfig.put("server", getServerConfig());
        allConfig.put("database", getDatabaseConfig());
        allConfig.put("cache", getCacheConfig());
        return allConfig;
    }

    /**
     * 验证配置是否有效
     */
    public Map<String, Object> validateConfiguration() {
        Map<String, Object> validation = new HashMap<>();
        
        // 验证应用配置
        boolean appValid = appProperties.getName() != null && !appProperties.getName().trim().isEmpty();
        validation.put("appConfigValid", appValid);
        
        // 验证服务器配置
        AppProperties.Server server = appProperties.getServer();
        boolean serverValid = server.getPort() > 0 && server.getPort() <= 65535 && 
                             server.getHost() != null && !server.getHost().trim().isEmpty();
        validation.put("serverConfigValid", serverValid);
        
        // 验证数据库配置
        AppProperties.Database database = appProperties.getDatabase();
        boolean databaseValid = database.getUrl() != null && !database.getUrl().trim().isEmpty() &&
                               database.getMaxConnections() > 0;
        validation.put("databaseConfigValid", databaseValid);
        
        // 验证缓存配置
        AppProperties.Cache cache = appProperties.getCache();
        boolean cacheValid = cache.getType() != null && !cache.getType().trim().isEmpty() &&
                            cache.getExpireMinutes() > 0 && cache.getMaxEntries() > 0;
        validation.put("cacheConfigValid", cacheValid);
        
        // 整体验证结果
        boolean overallValid = appValid && serverValid && databaseValid && cacheValid;
        validation.put("overallValid", overallValid);
        
        return validation;
    }
}
