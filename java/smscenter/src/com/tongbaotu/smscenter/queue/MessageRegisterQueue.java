package com.tongbaotu.smscenter.queue;

import redis.clients.jedis.Jedis;
import com.google.gson.Gson;
import com.tongbaotu.smscenter.config.Config;
import com.tongbaotu.smscenter.config.ConfigManager;
import com.tongbaotu.smscenter.pojo.MessageRegisterValue;
import com.tongbaotu.smscenter.pojo.MessageSendValue;
import com.tongbaotu.smscenter.util.GlobalConst;
import com.tongbaotu.smscenter.util.JedisManager;
/**
 * 注册队列
 * @author Tim
 */
public class MessageRegisterQueue {
    private JedisManager jedisManager = JedisManager.getInstance();

    private Gson gson = new Gson();

    private Config config = ConfigManager.getInstance().getConfig();

    private static MessageRegisterQueue queue = new MessageRegisterQueue();

    private MessageRegisterQueue() {
    }

    public static MessageRegisterQueue getInstance() {
        return queue;
    }

    /**
     * 放入注册队列信息
     * @param value
     */
    public void put(MessageRegisterValue value) {
        Jedis jedis = null;
        try {
            //获取连接
            jedis = jedisManager.getResource();

            //获取Redis index
            jedis.select(config.getRedisIndex());

            jedis.hset(GlobalConst.MESSAGE_CACHE_REGISTER, value.getPhoneNum(), gson.toJson(value));
        } finally {
            jedisManager.recycleJedisOjbect(jedis);
        }
    }

    /**
     * 获取注册队列信息
     * @param sendValue
     * @return
     */
    public MessageRegisterValue get(MessageSendValue sendValue) {
        Jedis jedis = null;
        MessageRegisterValue value = null;
        try {
            //获取连接
            jedis = jedisManager.getResource();

            //获取Redis index
            jedis.select(config.getRedisIndex());

            value =
                gson.fromJson(
                    jedis.hget(GlobalConst.MESSAGE_CACHE_REGISTER, sendValue.getPhoneNumber()),
                    MessageRegisterValue.class);

        } finally {
            jedisManager.recycleJedisOjbect(jedis);
        }

        return value;
    }
}
