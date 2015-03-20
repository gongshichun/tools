package com.tongbaotu.smscenter.queue;

import java.util.ArrayList;

import redis.clients.jedis.Jedis;
import com.google.gson.Gson;
import com.tongbaotu.smscenter.config.Config;
import com.tongbaotu.smscenter.config.ConfigManager;
import com.tongbaotu.smscenter.pojo.MessageSendValue;
import com.tongbaotu.smscenter.util.GlobalConst;
import com.tongbaotu.smscenter.util.JedisManager;
import com.tongbaotu.smscenter.util.TimeUtils;

/**
 * 消息回扫队列
 * @author Tim
 */
public class MessageRegressionQueue {
    private JedisManager jedisManager = JedisManager.getInstance();

    private Gson gson = new Gson();

    private Config config = ConfigManager.getInstance().getConfig();

    private static MessageRegressionQueue queue = new MessageRegressionQueue();

    private MessageRegressionQueue() {
    }

    public static MessageRegressionQueue getInstance() {
        return queue;
    }

    /**
     * 放入回扫队列
     * @param value
     */
    public void put(MessageSendValue value) {
        Jedis jedis = null;
        try {
            //获取连接
            jedis = jedisManager.getResource();

            //获取Redis index
            jedis.select(config.getRedisIndex());

            jedis.lpush(GlobalConst.MESSAGE_CACHE_KEY, gson.toJson(value));

        } finally {
            jedisManager.recycleJedisOjbect(jedis);
        }
    }

    public ArrayList<MessageSendValue> get(String timeStamp) {
        ArrayList<MessageSendValue> messageList = new ArrayList<MessageSendValue>();
        Jedis jedis = null;
        try {
            //获取连接
            jedis = jedisManager.getResource();

            //获取Redis index
            jedis.select(config.getRedisIndex());

            long len = jedis.llen(GlobalConst.MESSAGE_CACHE_KEY);

            for (int i = 0; i < len; i++) {
                String data = jedis.rpop(GlobalConst.MESSAGE_CACHE_KEY);
                MessageSendValue value = gson.fromJson(data, MessageSendValue.class);

                String oldTimeStamp = value.getTimeStamp();
                //本次发送
                if (TimeUtils.isSend(timeStamp, oldTimeStamp, 0)) {
                    messageList.add(value);
                }
                //重新放入等待下次发送
                else {
                    jedis.lpush(GlobalConst.MESSAGE_CACHE_KEY, gson.toJson(value));
                    continue;
                }
            }
        } finally {
            jedisManager.recycleJedisOjbect(jedis);
        }

        return messageList;
    }
}
