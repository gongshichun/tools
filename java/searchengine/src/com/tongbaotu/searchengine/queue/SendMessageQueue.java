package com.tongbaotu.searchengine.queue;

import redis.clients.jedis.Jedis;
import com.google.gson.Gson;
import com.tongbaotu.searchengine.config.Config;
import com.tongbaotu.searchengine.config.ConfigManager;
import com.tongbaotu.searchengine.pojo.SendValue;
import com.tongbaotu.searchengine.util.GlobalConst;
import com.tongbaotu.searchengine.util.JedisManager;
/**
 * 发送Message队列
 * @author Tim
 */
public class SendMessageQueue {

    private static JedisManager jedisManager = JedisManager.getInstance();

    private static Config config = ConfigManager.getInstance().getConfig();

    private static Gson gson = new Gson();

    private static SendMessageQueue queue = new SendMessageQueue();

    /**
     * 私有构造
     */
    private SendMessageQueue() {
    }

    /**
     * 获取实例
     * @return
     */
    public static SendMessageQueue getInstance() {
        return queue;
    }

    /**
     * 推送短消息
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
            jedis.lpush(GlobalConst.SEND_SMS_KEY, gson.toJson(value));
        } finally {
            jedisManager.recycleJedisOjbect(jedis);
        }
    }

}
