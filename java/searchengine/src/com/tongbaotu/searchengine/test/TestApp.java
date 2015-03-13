package com.tongbaotu.searchengine.test;

import org.apache.log4j.PropertyConfigurator;

import com.tongbaotu.searchengine.Command;
import com.tongbaotu.searchengine.engine.SearchEngine;
import com.tongbaotu.searchengine.exception.MyException;
import com.tongbaotu.searchengine.util.JedisManager;
import com.tongbaotu.searchengine.util.MySQLManager;

/**
 * just TestApp
 * @author Tim
 */
public class TestApp {
    public static void main(String[] args) throws MyException {
        //加载Log4j
        PropertyConfigurator.configure(Command.options.configPath + "log4j.properties");

        //初始化MySQL
        MySQLManager.getInstance().init(Command.options.configPath + "mysql.properties");

        //初始化Redis
        JedisManager.getInstance().init(Command.options.configPath + "redis.properties");

        //搜索引擎启动
        SearchEngine.getInstance().active();
    }
}
