package org.example.demo1.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * APT示例：使用@ConfigurationProperties注解的配置属性类
 * spring-boot-configuration-processor会在编译时处理这个类，
 * 生成META-INF/spring-configuration-metadata.json文件，
 * 为IDE提供配置提示功能
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    /**
     * 应用名称
     */
    private String name = "Demo Application";

    /**
     * 应用版本
     */
    private String version = "1.0.0";

    /**
     * 应用描述
     */
    private String description = "Spring Boot APT Configuration Demo";

    /**
     * 服务器配置
     */
    private Server server = new Server();

    /**
     * 数据库配置
     */
    private Database database = new Database();

    /**
     * 缓存配置
     */
    private Cache cache = new Cache();

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * 服务器配置内部类
     */
    public static class Server {
        /**
         * 服务器端口
         */
        private int port = 8080;

        /**
         * 服务器主机
         */
        private String host = "localhost";

        /**
         * 连接超时时间（秒）
         */
        private int timeout = 30;

        /**
         * 是否启用SSL
         */
        private boolean sslEnabled = false;

        // Getters and Setters
        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public boolean isSslEnabled() {
            return sslEnabled;
        }

        public void setSslEnabled(boolean sslEnabled) {
            this.sslEnabled = sslEnabled;
        }
    }

    /**
     * 数据库配置内部类
     */
    public static class Database {
        /**
         * 数据库URL
         */
        private String url = "jdbc:h2:mem:testdb";

        /**
         * 数据库用户名
         */
        private String username = "sa";

        /**
         * 数据库密码
         */
        private String password = "";

        /**
         * 连接池最大连接数
         */
        private int maxConnections = 10;

        /**
         * 是否显示SQL
         */
        private boolean showSql = false;

        // Getters and Setters
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getMaxConnections() {
            return maxConnections;
        }

        public void setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
        }

        public boolean isShowSql() {
            return showSql;
        }

        public void setShowSql(boolean showSql) {
            this.showSql = showSql;
        }
    }

    /**
     * 缓存配置内部类
     */
    public static class Cache {
        /**
         * 缓存类型
         */
        private String type = "memory";

        /**
         * 缓存过期时间（分钟）
         */
        private int expireMinutes = 60;

        /**
         * 最大缓存条目数
         */
        private int maxEntries = 1000;

        /**
         * 是否启用缓存
         */
        private boolean enabled = true;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getExpireMinutes() {
            return expireMinutes;
        }

        public void setExpireMinutes(int expireMinutes) {
            this.expireMinutes = expireMinutes;
        }

        public int getMaxEntries() {
            return maxEntries;
        }

        public void setMaxEntries(int maxEntries) {
            this.maxEntries = maxEntries;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    @Override
    public String toString() {
        return "AppProperties{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", server=" + server +
                ", database=" + database +
                ", cache=" + cache +
                '}';
    }
}
