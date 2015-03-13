package com.tongbaotu.smscenter.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.tongbaotu.smscenter.util.GlobalConst;

/**
 * 配置管理器
 * @author Tim
 */
public class ConfigManager {
    private static ConfigManager manager = new ConfigManager();

    private Config config = new Config();

    private static final String CONFIG_PATH = "smscenter.properties";

    private static Logger logger = Logger.getLogger(ConfigManager.class);

    /**
     * 私有构造
     */
    private ConfigManager() {
        try {
            load(GlobalConst.CONFIG_PATH);
        } catch (FileNotFoundException e) {
            logger.error("Config Load FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("Config Load IOException", e);
        }
    }

    /**
     * 实例化
     * @return
     */
    public static ConfigManager getInstance() {
        return manager;
    }

    /**
     * 初始加载
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void load(String configPath) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(configPath + CONFIG_PATH));

        config.setUserNameParameter(prop.getProperty("message.userName.parameter"));

        config.setPasswordParameter(prop.getProperty("message.password.parameter"));

        config.setMobileParameter(prop.getProperty("message.mobile.parameter"));

        config.setMessageParameter(prop.getProperty("message.message.parameter"));

        config.setUrl(prop.getProperty("message.url"));

        config.setUserName(prop.getProperty("message.userName"));

        config.setPassword(prop.getProperty("message.password"));

        String suffix = prop.getProperty("message.suffix");
        config.setSuffix(new String(suffix.getBytes("ISO-8859-1"), "utf-8"));
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
