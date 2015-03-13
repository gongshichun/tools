package com.tongbaotu.smscenter.pojo;
/**
 * 发送信息对象
 * @author Tim
 */
public class MessageSendValue {
    private String phoneNumber;

    private String message;

    private String customer_id;

    private String messageBatchId;

    private String timeStamp;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getMessageBatchId() {
        return messageBatchId;
    }

    public void setMessageBatchId(String messageBatchId) {
        this.messageBatchId = messageBatchId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
