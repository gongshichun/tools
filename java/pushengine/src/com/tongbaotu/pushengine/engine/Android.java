package com.tongbaotu.pushengine.engine;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.tongbaotu.pushengine.define.Define;
import com.tongbaotu.pushengine.helper.android.AndroidBroadcast;
import com.tongbaotu.pushengine.helper.android.AndroidUnicast;


public class Android {
	
	private String appkey = null;
	private String appMasterSecret = null;
	private String timestamp = null;
	private String productionMode = null;
	
	public Android() {
		Properties props = new Properties();

		try {
	        props.load(new FileInputStream(Define.CONFIG_PATH + "android_push.properties"));   
	    } catch (IOException e) {
	    	//throw new MyException("ymhhouException redis.properties IOException:" + e.getMessage());
		}

		if(props.getProperty("environment").equals("product")) {
			this.productionMode = "true";
		} else if(props.getProperty("environment").equals("test")) {
			this.productionMode = "false";
		}

		this.appkey = props.getProperty("appkey");
		this.appMasterSecret = props.getProperty("appMasterSecret");
		this.timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
	}
	

	public void sendUnicast(String title, String message, String deviceToken) throws Exception {
		AndroidUnicast unicast = new AndroidUnicast();
		unicast.setAppMasterSecret(this.appMasterSecret);
		unicast.setPredefinedKeyValue("appkey", this.appkey);
		unicast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO Set your device token
		unicast.setPredefinedKeyValue("device_tokens", deviceToken);
		unicast.setPredefinedKeyValue("ticker", "Android unicast ticker");
		unicast.setPredefinedKeyValue("title",  title);
		unicast.setPredefinedKeyValue("text",   message);
		unicast.setPredefinedKeyValue("after_open", "go_app");
		unicast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		unicast.setPredefinedKeyValue("production_mode", this.productionMode);
		// Set customized fields
//		unicast.setExtraField("test", "helloworld");
		unicast.send();
	}

	public void sendAndroidBroadcast(String title, String message, String deviceToken) throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast();
		broadcast.setAppMasterSecret(this.appMasterSecret);
		broadcast.setPredefinedKeyValue("appkey", this.appkey);
		broadcast.setPredefinedKeyValue("timestamp", this.timestamp);
		broadcast.setPredefinedKeyValue("ticker", title);
		broadcast.setPredefinedKeyValue("title", title);
		broadcast.setPredefinedKeyValue("text", message);
		broadcast.setPredefinedKeyValue("after_open", "go_app");
		broadcast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		broadcast.setPredefinedKeyValue("production_mode", this.productionMode);
		// Set customized fields
//		broadcast.setExtraField("test", "helloworld");
		broadcast.send();
	}
}
