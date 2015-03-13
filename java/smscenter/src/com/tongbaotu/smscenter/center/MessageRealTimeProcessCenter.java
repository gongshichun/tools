package com.tongbaotu.smscenter.center;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongbaotu.smscenter.exception.MyException;
import com.tongbaotu.smscenter.pojo.MessageSendValue;
import com.tongbaotu.smscenter.queue.MessageQueue;

/**
 * 信息实时处理中心
 * @author Tim
 */
public class MessageRealTimeProcessCenter extends Thread {
    private static Logger logger = LoggerFactory.getLogger(MessageRealTimeProcessCenter.class);

    private static MessageRealTimeProcessCenter center = new MessageRealTimeProcessCenter();

    private MessageSendCenter sendCenter = MessageSendCenter.getInstance();

    private MessageQueue queue = MessageQueue.getInstance();

    /**
     * 私有构造
     */
    private MessageRealTimeProcessCenter() {
    }

    /**
     * 获取实例
     * @return
     */
    public static MessageRealTimeProcessCenter getInstance() {
        return center;
    }

    /**
     * 启动
     */
    public void active() {
        this.start();
    }

    /**
     * 启动数据处理中心
     */
    public void run() {
        while (true) {
            try {
                //从推送队列中获取
                MessageSendValue messageSendValue = queue.take();

                if (queue == null) {
                    return;
                }

                //发送消息
                sendCenter.send(messageSendValue, true);

            } catch (MyException e) {
                logger.error("smscenterException:", e);
            }
        }
    }
}
