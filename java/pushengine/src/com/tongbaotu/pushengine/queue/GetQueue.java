package com.tongbaotu.pushengine.queue;


import java.util.List;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;
import com.tongbaotu.pushengine.define.Define;
import com.tongbaotu.pushengine.config.*;
import com.tongbaotu.pushengine.helper.JedisHelper;
import com.tongbaotu.pushengine.engine.Android;
import com.tongbaotu.pushengine.engine.IOS;
import com.tongbaotu.pushengine.engine.Pm;
import com.tongbaotu.pushengine.engine.SystemMessage;
import com.tongbaotu.pushengine.model.*;


public class GetQueue {
	private static GetQueue instance  = new GetQueue();
	private static JedisHelper jedisHelper = JedisHelper.getInstance();
	private static Config config = ConfigManager.getInstance().getConfig();
	private static Task task = new Task();
	private static Gson gson = new Gson();
//	获取一个任务
	//依据任务类型调用engine 发送消息
	
	public static GetQueue getInstance() {
		return instance;
	}
	
	private GetQueue() {
		
	}
	
	 public void run() throws Exception {
        Jedis jedis = null;
        Android androidEngine = new Android();
        Task task = null;
        IOS iosEngine = new IOS();
        jedis = jedisHelper.getResource();
        jedis.select(config.getRedisIndex());
//    	iosEngine.send("this is a test", "8212bb7e 653ace41 62e104dd 5506ea83 719cc7e8 825395c9 55d0048a 2e4bcf75");
//    	iosEngine.checkToken("48cb43c6 07f0300a 57128f19 5026f169 2e2be7a5 e79a6999 48fb3d6c df3928c9");
//    	iosEngine.send("this is a test", "48cb43c6 07f0300a 57128f19 5026f169 2e2be7a5 e79a6999 48fb3d6c df3928c9");
		 while (true) {
//			iosEngine.send("this is a test", "8212bb7e 653ace41 62e104dd 5506ea83 719cc7e8 825395c9 55d0048a 2e4bcf75");
//	        iosEngine.send("this is a test", "48cb43c6 07f0300a 57128f19 5026f169 2e2be7a5 e79a6999 48fb3d6c df3928c9");
//	        iosEngine.send("this is a test", "397fab60 036b7413 9e460d32 39bc0ef6 bc2b934b 59f19265 8949fbe7 1a153343");
//			iosEngine.send("this is a test", "481da1d5 9ebc4f30 6d0f50c4 60eee515 9f9a95a6 cf79466e b84d50fc 77b5b971");
//			iosEngine.send("this is a test", "8212bb7e 653ace41 62e104dd 5506ea83 719cc7e8 825395c9 55d0048a 2e4bcf75");
//			iosEngine.send("this is a test", "b19e43b1 eb457cdc 1273825d 92e51c3a 7ff13dae e8b6cba0 56bd3eea 0937ac02");
//			iosEngine.send("this is a test", "b19e43b1 eb457cdc 1273825d 92e51c3a 7ff13dae e8b6cba0 56bd3eea 0937ac02");
//			iosEngine.send("this is a test", "a5c894bd f8bd69fa bf65e163 111386fd c7974b26 8c605c76 3d49f000 9fe897a1");
//			iosEngine.send("this is a test", "cb0d7bcc 69d5e0b8 f94a5ce0 3aecb96e b6621933 a5294f1d 6aee0f42 cb63f694");
			
            List<String> list = jedis.brpop(0, Define.APP_NOTIFICATION);//从队列获取任务
            task = gson.fromJson(list.get(1), Task.class);
            if(task.getType().equals(Define.DEVICE_TYPE_IOS)) {
            	iosEngine.send(task.getMessage(), "48cb43c6 07f0300a 57128f19 5026f169 2e2be7a5 e79a6999 48fb3d6c df3928c9");
            	System.out.println("send ios message");
            } else if(task.getType().equals(Define.DEVICE_TYPE_ANDROID)) {
            	if(task.getDeviceoken().equals("all")) {
            		androidEngine.sendAndroidBroadcast("系统消息", task.getMessage(), "123123");
            	} else {
            		androidEngine.sendUnicast("系统消息", task.getMessage(), task.getDeviceoken());
            	}

            	System.out.println("send android");
            } else if(task.getType().equals(Define.SYSTEM)) {
            	Pm.getInstance().insert(task.getCustomerId(), task.getMessage(), 1);
            	System.out.println("system message");
            }
            SystemMessage.getInstance().insert(task.getCustomerId(), task.getmessageBatchId(), task.getMessage(), task.getType());
        }    
    }

}
