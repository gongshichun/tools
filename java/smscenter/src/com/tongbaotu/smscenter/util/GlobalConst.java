package com.tongbaotu.smscenter.util;
/**
 * 全局常用类
 * @author Tim
 */
public class GlobalConst {
    public static final String CONFIG_PATH = "config/";//路径

    public static final String MESSAGE_KEY = "sms_notification";//message Key

    public static final String MESSAGE_CACHE_KEY = "sms_notification_cache";//message cache Key

    public static final String MESSAGE_CACHE_REGISTER = "sms_notification_register";//message register Key

    //smscenterJOB组
    public static final String JOB_GROUP_NAME = "JOBGROUP";

    //smscenter监听组
    public static final String TRIGGER_GROUP_NAME = "TRIGGERGROUP";

    //smscenter JOB NAME
    public static final String GATHER_JOB_NAME = "GATHER_JOB";

    public static final String CONTENTTYPE = "application/x-www-form-urlencoded;charset=utf-8";

    public static final long MILLISECOND = 60;

    public static final int TARGET_TYPE_SMS = 4;
}
