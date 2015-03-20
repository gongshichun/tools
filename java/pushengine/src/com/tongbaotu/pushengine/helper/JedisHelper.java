package com.tongbaotu.pushengine.helper;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongbaotu.pushengine.config.ConfigManager;
//import com.tongbaotu.searchengine.exception.MyException;
//import com.tongbaotu.searchengine.util.JedisManager;
//import com.tongbaotu.searchengine.util.ParaChecker;
//import com.tongbaotu.searchengine.util.YYDaoException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisHelper {
	
	 private static JedisHelper instance = new JedisHelper();

	 private static final Logger logger = LoggerFactory.getLogger(JedisHelper.class);

	    private JedisPool jedisPool;

//	    private static String CONFIG_PATH = "config/redis.properties";

	    private JedisHelper() {
	    	
	    }

	    public static JedisHelper getInstance() {
	        return instance;
	    }

	    /**
	     * 初始化
	     * @throws YYDaoException 
	     * @throws FileNotFoundException
	     * @throws IOException
	     */
	    public void init(String configPath) {
	        Properties props = new Properties();
	        try {
	            props.load(new FileInputStream(configPath));
	        } catch (IOException e) {
//	            throw new MyException("ymhhouException redis.properties IOException:" + e.getMessage());
	        }
	        ConfigManager.getInstance().setRedisIndex(
	            Integer.parseInt(props.getProperty("redis.db.index")));

	        JedisPoolConfig config = new JedisPoolConfig();
	        config.setMaxIdle(Integer.valueOf(props.getProperty("redis.pool.maxIdle")));
	        config.setMaxTotal(Integer.valueOf(props.getProperty("redis.pool.maxTotal")));
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
