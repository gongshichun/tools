package com.tongbaotu.searchengine.queue;

import java.util.List;

import redis.clients.jedis.Jedis;
import com.google.gson.Gson;
import com.tongbaotu.searchengine.config.Config;
import com.tongbaotu.searchengine.config.ConfigManager;
import com.tongbaotu.searchengine.pojo.AcceptValue;
import com.tongbaotu.searchengine.util.GlobalConst;
import com.tongbaotu.searchengine.util.JedisManager;

/**
 * 接收队列
 * @author Tim
 */
public class AcceptQueue {
    private static JedisManager jedisManager = JedisManager.getInstance();

    private static Config config = ConfigManager.getInstance().getConfig();

    private static Gson gson = new Gson();

    private static AcceptQueue queue = new AcceptQueue();

    /**
     * 私有构造
     */
    private AcceptQueue() {
    }

    /**
     * 实例化
     * @return
     */
    public static AcceptQueue getInstance() {
        return queue;
    }

    /**
     * take
     * @return
     */
    public AcceptValue take() {
        AcceptValue value = null;
        Jedis jedis = null;
        try {
            //获取连接
            jedis = jedisManager.getResource();

            //获取Redis index
            jedis.select(config.getRedisIndex());

            List<String> list = jedis.brpop(0, GlobalConst.ACCEPT_KEY);
            if (list == null || list.size() <= 0) {
                return value;
            }

            //获取数据
            value = gson.fromJson(list.get(1), AcceptValue.class);
        } finally {
            jedisManager.recycleJedisOjbect(jedis);
        }

        return value;
    }
}
