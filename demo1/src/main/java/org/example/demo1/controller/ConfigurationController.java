package org.example.demo1.controller;

import org.example.demo1.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 配置控制器，展示APT处理的配置属性
 */
@RestController
@RequestMapping("/api/config")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @Autowired
    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * 获取应用基本信息
     * GET /api/config/app
     */
    @GetMapping("/app")
    public Map<String, Object> getAppInfo() {
        return configurationService.getAppInfo();
    }

    /**
     * 获取服务器配置
     * GET /api/config/server
     */
    @GetMapping("/server")
    public Map<String, Object> getServerConfig() {
        return configurationService.getServerConfig();
    }

    /**
     * 获取数据库配置
     * GET /api/config/database
     */
    @GetMapping("/database")
    public Map<String, Object> getDatabaseConfig() {
        return configurationService.getDatabaseConfig();
    }

    /**
     * 获取缓存配置
     * GET /api/config/cache
     */
    @GetMapping("/cache")
    public Map<String, Object> getCacheConfig() {
        return configurationService.getCacheConfig();
    }

    /**
     * 获取所有配置信息
     * GET /api/config/all
     */
    @GetMapping("/all")
    public Map<String, Object> getAllConfigurations() {
        return configurationService.getAllConfigurations();
    }

    /**
     * 验证配置
     * GET /api/config/validate
     */
    @GetMapping("/validate")
    public Map<String, Object> validateConfiguration() {
        return configurationService.validateConfiguration();
    }

    /**
     * 获取APT信息说明
     * GET /api/config/apt-info
     */
    @GetMapping("/apt-info")
    public Map<String, Object> getAptInfo() {
        return Map.of(
            "title", "Spring Boot Configuration Processor APT Demo",
            "description", "这个示例展示了如何使用spring-boot-configuration-processor进行编译时注解处理",
            "features", Map.of(
                "apt_processing", "编译时处理@ConfigurationProperties注解",
                "metadata_generation", "自动生成META-INF/spring-configuration-metadata.json",
                "ide_support", "为IDE提供配置属性的智能提示和自动完成",
                "type_safety", "编译时类型检查和验证"
            ),
            "benefits", Map.of(
                "developer_experience", "提供更好的开发体验",
                "error_prevention", "减少配置错误",
                "documentation", "自动生成配置文档",
                "performance", "避免运行时反射开销"
            ),
            "usage", Map.of(
                "step1", "添加spring-boot-configuration-processor依赖",
                "step2", "创建@ConfigurationProperties注解的配置类",
                "step3", "编译项目生成元数据文件",
                "step4", "在application.properties中享受智能提示"
            )
        );
    }
}
