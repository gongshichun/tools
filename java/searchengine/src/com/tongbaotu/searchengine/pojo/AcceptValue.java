package com.tongbaotu.searchengine.pojo;
/**
 * 接收对象
 * @author Tim
 */
public class AcceptValue {
    private String productId;

    private int type;

    private int targetType;

    private String message;

    private String messageBatchId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageBatchId() {
        return messageBatchId;
    }

    public void setMessageBatchId(String messageBatchId) {
        this.messageBatchId = messageBatchId;
    }
}
