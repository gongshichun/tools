package com.tongbaotu.smscenter.pojo;
/**
 * 消息注册对象
 * @author Tim
 */
public class MessageRegisterValue {
    private String phoneNum;

    private long waitCount;

    private String timeStamp;

    public long getWaitCount() {
        return waitCount;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setWaitCount(long waitCount) {
        this.waitCount = waitCount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
