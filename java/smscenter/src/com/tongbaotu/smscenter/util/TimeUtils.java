package com.tongbaotu.smscenter.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * 时间工具类
 * @author Tim
 */
public class TimeUtils {
    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime() {
        LocalDateTime localTime = LocalDateTime.now();

        //线程安全的格式化类，不用每次都new个SimpleDateFormat，since 1.8
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return dateTimeFormatter.format(localTime);
    }

    /**
     * 是否发送
     * @param timeStamp1
     * @param timeStamp2
     * @param second
     * @return
     */
    public static boolean isSend(String timeStamp1, String timeStamp2, long second) {
        //线程安全的格式化类，不用每次都new个SimpleDateFormat，since 1.8
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime localTime1 = LocalDateTime.parse(timeStamp1, dateTimeFormatter);

        localTime1 = localTime1.minusSeconds(second);

        LocalDateTime localTime2 = LocalDateTime.parse(timeStamp2, dateTimeFormatter);

        //使用long比较
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        if (Long.parseLong(dateTimeFormatter1.format(localTime1)) >= Long
            .parseLong(dateTimeFormatter1.format(localTime2))) {
            return true;
        }

        return false;
    }

    /**
     * 是否发送
     * @param timeStamp1
     * @param timeStamp2
     * @param second
     * @return
     */
    public static String plusSecond(String timeStamp, long second) {
        //线程安全的格式化类，不用每次都new个SimpleDateFormat，since 1.8
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime localTime = LocalDateTime.parse(timeStamp, dateTimeFormatter);

        localTime = localTime.plusSeconds(second);

        return dateTimeFormatter.format(localTime);
    }
}