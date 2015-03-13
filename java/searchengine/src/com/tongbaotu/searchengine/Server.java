package com.tongbaotu.searchengine;

import org.apache.log4j.PropertyConfigurator;

import com.tongbaotu.searchengine.engine.SearchEngine;
import com.tongbaotu.searchengine.exception.MyException;
import com.tongbaotu.searchengine.util.JedisManager;
import com.tongbaotu.searchengine.util.MySQLManager;
/**
 * 主程序运行
 * @author Tim
 */
public class Server {
    public static void main(String[] args) throws MyException {
        //解析参数
        new Command().parseOption(args);

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
