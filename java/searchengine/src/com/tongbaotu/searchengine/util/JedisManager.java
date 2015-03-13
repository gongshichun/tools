package com.tongbaotu.searchengine.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.tongbaotu.searchengine.config.ConfigManager;
import com.tongbaotu.searchengine.exception.MyException;

/**
 * Redis管理
 * @author Tim
 */
public class JedisManager {

    private static final Logger logger = LoggerFactory.getLogger(JedisManager.class);

    private JedisPool jedisPool;

    private static JedisManager instance = new JedisManager();

    private static String CONFIG_PATH = "config/redis.properties";

    private JedisManager() {
    }

    public static JedisManager getInstance() {
        return instance;
    }

    /**
     * 初始化
     * @throws YYDaoException 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void init(String configPath) throws MyException {
        Properties props = new Properties();

        if (ParaChecker.isValidPara(configPath)) {
            CONFIG_PATH = configPath;
        }
        try {
            props.load(new FileInputStream(CONFIG_PATH));
        } catch (IOException e) {
            throw new MyException("ymhhouException redis.properties IOException:" + e.getMessage());
        }
        ConfigManager.getInstance().setRedisIndex(
            Integer.parseInt(props.getProperty("redis.db.index")));

        JedisPoolConfig config = new JedisPoolConfig();
        //config.setMaxActive(Integer.valueOf(props.getProperty("redis.pool.maxActive")));
        config.setMaxIdle(Integer.valueOf(props.getProperty("redis.pool.maxIdle")));
        config.setMaxTotal(Integer.valueOf(props.getProperty("redis.pool.maxTotal")));
        //config.setMaxWait(Long.valueOf(props.getProperty("redis.pool.maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(props.getProperty("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(props.getProperty("redis.pool.testOnReturn")));
        jedisPool =
            new JedisPool(config, props.getProperty("redis.ip"), Integer.valueOf(props
                .getProperty("redis.port")), 0, props.getProperty("redis.pool.password"));
    }

    /**
     * 获取连接
     * @return
     */
    private Jedis doGetResource() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
            logger.warn("get redis resource exception");
        }
        return jedis;
    }

    /**
     * 获取连接
     * @return
     */
    public Jedis getResource() {
        Jedis jedis = null;
        for (int i = 0; i < 3; i++) {
            jedis = doGetResource();
            if (jedis != null) {
                break;
            }
        }
        return jedis;
    }

    /**
     * 返回池中
     * @param jedis
     */
    public void recycleJedisOjbect(Jedis jedis) {
        jedisPool.returnResource(jedis);
    }
}
