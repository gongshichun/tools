package com.tongbaotu.smscenter;

import org.apache.log4j.PropertyConfigurator;

import com.tongbaotu.smscenter.center.MessageRealTimeProcessCenter;
import com.tongbaotu.smscenter.center.MessageRegressionProcessCenter;
import com.tongbaotu.smscenter.exception.MyException;
import com.tongbaotu.smscenter.util.JedisManager;
import com.tongbaotu.smscenter.util.MySQLManager;

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

        //消息回扫处理中心启动
        MessageRegressionProcessCenter.getInstance().active();

        //消息处理中心启动
        MessageRealTimeProcessCenter.getInstance().active();
    }
}