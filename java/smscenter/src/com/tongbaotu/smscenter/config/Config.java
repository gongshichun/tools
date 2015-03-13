package com.tongbaotu.smscenter.config;
/**
 * 配置对象类
 * @author Tim
 */
public class Config {
    //RedisIndex
    private int redisIndex;

    //用户参数
    private String userNameParameter;

    //密码参数
    private String passwordParameter;

    //手机参数
    private String mobileParameter;

    //信息参数
    private String messageParameter;

    //url
    private String url;

    //用户名
    private String userName;

    //密码
    private String password;

    //后缀
    private String suffix;

    public int getRedisIndex() {
        return redisIndex;
    }

    public void setRedisIndex(int redisIndex) {
        this.redisIndex = redisIndex;
    }

    public String getUserNameParameter() {
        return userNameParameter;
    }

    public void setUserNameParameter(String userNameParameter) {
        this.userNameParameter = userNameParameter;
    }

    public String getPasswordParameter() {
        return passwordParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        this.passwordParameter = passwordParameter;
    }

    public String getMobileParameter() {
        return mobileParameter;
    }

    public void setMobileParameter(String mobileParameter) {
        this.mobileParameter = mobileParameter;
    }

    public String getMessageParameter() {
        return messageParameter;
    }

    public void setMessageParameter(String messageParameter) {
        this.messageParameter = messageParameter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
