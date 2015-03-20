package com.tongbaotu.pushengine;

import org.apache.log4j.PropertyConfigurator;

import com.tongbaotu.pushengine.define.Define;
import com.tongbaotu.pushengine.helper.JedisHelper;
import com.tongbaotu.pushengine.helper.MySQLHelper;
import com.tongbaotu.pushengine.queue.GetQueue;

public class Server {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//加载配置文件
		JedisHelper.getInstance().init(Define.CONFIG_PATH + "redis.properties");
		MySQLHelper.getInstance().init(Define.CONFIG_PATH + "mysql.properties");
		GetQueue.getInstance().run();
	}

}
