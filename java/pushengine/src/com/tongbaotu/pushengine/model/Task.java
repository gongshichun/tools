package com.tongbaotu.pushengine.model;

public class Task {

    private String type;

    private String message;

    private String messageBatchId;
    
    private String device_token;
    
    private Integer customer_id;

   public String getType() {
	   return type;
   }

   public String getMessage() {
	   return message;
   }

   public String getDeviceoken() {
	   return device_token;
   }

   public Integer getCustomerId() {
	   return customer_id;
   }
 
   public String getmessageBatchId() {
	   return messageBatchId;
   }

}
