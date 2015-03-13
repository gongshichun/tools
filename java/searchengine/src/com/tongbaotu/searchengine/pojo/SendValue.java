package com.tongbaotu.searchengine.pojo;
/**
 * 发送对象 
 * @author Tim
 */
public class SendValue {
    private String message;

    private int customer_id;

    private String messageBatchId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getMessageBatchId() {
        return messageBatchId;
    }

    public void setMessageBatchId(String messageBatchId) {
        this.messageBatchId = messageBatchId;
    }
}
