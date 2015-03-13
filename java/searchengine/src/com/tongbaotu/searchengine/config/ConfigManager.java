package com.tongbaotu.searchengine.config;

/**
 * 配置管理器
 * @author Tim
 */
public class ConfigManager {
    private static ConfigManager manager = new ConfigManager();

    private Config config = new Config();

    /**
     * 私有构造
     */
    private ConfigManager() {
    }

    /**
     * 实例化
     * @return
     */
    public static ConfigManager getInstance() {
        return manager;
    }

    /**
     * 获取配置文件内容
     * @return
     */
    public Config getConfig() {
        return config;
    }

    /**
     * 设置RedisIndex
     * @param redisIndex
     */
    public void setRedisIndex(int redisIndex) {
        config.setRedisIndex(redisIndex);
    }
}
