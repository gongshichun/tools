package com.tongbaotu.smscenter.test;

import redis.clients.jedis.Jedis;
import com.google.gson.Gson;
import com.tongbaotu.smscenter.Command;
import com.tongbaotu.smscenter.exception.MyException;
import com.tongbaotu.smscenter.pojo.MessageSendValue;
import com.tongbaotu.smscenter.util.GlobalConst;
import com.tongbaotu.smscenter.util.JedisManager;

public class TestPush {

    public static void main(String[] args) throws MyException {
        //初始化Redis
        JedisManager.getInstance().init(Command.options.configPath + "redis.properties");

        Jedis jedis = JedisManager.getInstance().getResource();

        Gson gson = new Gson();
        MessageSendValue value = new MessageSendValue();
        value.setMessage("谢谢您的认购，一百万已到账!");
        value.setCustomer_id("10133");
        value.setPhoneNumber("18600344955");
        value.setMessageBatchId("2343");

        jedis.lpush(GlobalConst.MESSAGE_KEY, gson.toJson(value));

        System.out.println("push sucess!");
    }
}
