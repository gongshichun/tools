package com.tongbaotu.searchengine.queue;

import redis.clients.jedis.Jedis;
import com.google.gson.Gson;
import com.tongbaotu.searchengine.config.Config;
import com.tongbaotu.searchengine.config.ConfigManager;
import com.tongbaotu.searchengine.pojo.SendValue;
import com.tongbaotu.searchengine.util.GlobalConst;
import com.tongbaotu.searchengine.util.JedisManager;
/**
 * 发送App队列
 * @author tbt
 */
public class SendAppQueue {
    private static JedisManager jedisManager = JedisManager.getInstance();

    private static Config config = ConfigManager.getInstance().getConfig();

    private static Gson gson = new Gson();

    private static SendAppQueue queue = new SendAppQueue();

    /**
     * 私有构造
     */
    private SendAppQueue() {
    }

    /**
     * 获取实例
     * @return
     */
    public static SendAppQueue getInstance() {
        return queue;
    }

    /**
     * 推动APP消息
     * @param value
     */
    public void push(SendValue value) {
        Jedis jedis = null;
        try {
            //获取连接
            jedis = jedisManager.getResource();

            //获取Redis index
            jedis.select(config.getRedisIndex());

            //推送
            jedis.lpush(GlobalConst.SEND_APP_KEY, gson.toJson(value));
        } finally {
            jedisManager.recycleJedisOjbect(jedis);
        }
    }
}
