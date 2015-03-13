package com.tongbaotu.smscenter.queue;

import java.util.List;

import redis.clients.jedis.Jedis;
import com.google.gson.Gson;
import com.tongbaotu.smscenter.config.Config;
import com.tongbaotu.smscenter.config.ConfigManager;
import com.tongbaotu.smscenter.pojo.MessageSendValue;
import com.tongbaotu.smscenter.util.GlobalConst;
import com.tongbaotu.smscenter.util.JedisManager;
import com.tongbaotu.smscenter.util.TimeUtils;

/**
 * 消息队列
 * @author Tim
 */
public class MessageQueue {
    private static MessageQueue queue = new MessageQueue();

    private JedisManager jedisManager = JedisManager.getInstance();

    private Gson gson = new Gson();

    private Config config = ConfigManager.getInstance().getConfig();

    /**
     * 私有构造
     */
    private MessageQueue() {
    }

    /**
     * 获取实例
     * @return
     */
    public static MessageQueue getInstance() {
        return queue;
    }

    /**
     * take
     * @return
     */
    public MessageSendValue take() {
        Jedis jedis = null;
        MessageSendValue sendInfo = null;
        try {
            //获取连接
            jedis = jedisManager.getResource();

            //获取Redis index
            jedis.select(config.getRedisIndex());

            List<String> list = jedis.brpop(0, GlobalConst.MESSAGE_KEY);
            if (list == null || list.size() <= 0) {
                return sendInfo;
            }

            //获取发送信息
            sendInfo = gson.fromJson(list.get(1), MessageSendValue.class);

            //设置时间戳
            sendInfo.setTimeStamp(TimeUtils.getCurrentTime());
        } finally {
            jedisManager.recycleJedisOjbect(jedis);
        }

        return sendInfo;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}
