package com.tongbaotu.smscenter.exception;
/**
 * 自定义异常
 * @author Tim
 */
public class MyException extends Exception {
    /**
     * 构造器
     * @param errorCode 错误代码。
     */
    public MyException(String message) {
        super(message);
    }
}
