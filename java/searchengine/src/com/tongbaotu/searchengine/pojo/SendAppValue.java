package com.tongbaotu.searchengine.pojo;
/**
 * 发送APP对象
 * @author Tim
 */
public class SendAppValue extends SendValue {
    private String type;

    private String device_token;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

}
