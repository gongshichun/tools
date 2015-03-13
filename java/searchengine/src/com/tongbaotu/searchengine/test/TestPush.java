package com.tongbaotu.searchengine.test;

import redis.clients.jedis.Jedis;
import com.google.gson.Gson;
import com.tongbaotu.searchengine.Command;
import com.tongbaotu.searchengine.exception.MyException;
import com.tongbaotu.searchengine.pojo.AcceptValue;
import com.tongbaotu.searchengine.util.GlobalConst;
import com.tongbaotu.searchengine.util.JedisManager;

/**
 * 测试push
 * @author Tim
 */
public class TestPush {
    public static void main(String[] args) throws MyException {
        //初始化Redis
        JedisManager.getInstance().init(Command.options.configPath + "redis.properties");

        Jedis jedis = JedisManager.getInstance().getResource();

        Gson gson = new Gson();
        AcceptValue value = new AcceptValue();
        value.setMessage("hello, product is closed!");
        value.setTargetType(GlobalConst.TARGETTYPE_SYSTME);
        value.setType(GlobalConst.TYPE_PRODUCTCUSTOMER);
        value.setProductId("15");

        jedis.lpush(GlobalConst.ACCEPT_KEY, gson.toJson(value));

        System.out.println("push sucess!");
    }
}
