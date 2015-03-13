package com.tongbaotu.smscenter.center;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongbaotu.smscenter.exception.MyException;
import com.tongbaotu.smscenter.pojo.MessageSendValue;
import com.tongbaotu.smscenter.queue.MessageRegressionQueue;
import com.tongbaotu.smscenter.util.TimeUtils;

/**
 * 消息回归处理中心
 * @author Tim
 */
public class MessageRegressionProcessCenter extends Thread {
    private static Logger logger = LoggerFactory.getLogger(MessageRealTimeProcessCenter.class);

    private MessageSendCenter sendCenter = MessageSendCenter.getInstance();

    private MessageRegressionQueue regressionQueue = MessageRegressionQueue.getInstance();

    private MessageRegisterProcessCenter Registerprocess = MessageRegisterProcessCenter
        .getInstance();

    private static MessageRegressionProcessCenter center = new MessageRegressionProcessCenter();

    /**
     * 私有构造
     */
    private MessageRegressionProcessCenter() {
    }

    /**
     * 获取实例
     * @return
     */
    public static MessageRegressionProcessCenter getInstance() {
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
                //从回扫队列中获取
                ArrayList<MessageSendValue> sendList =
                    regressionQueue.get(TimeUtils.getCurrentTime());

                //如果不存在，则继续下次
                if (sendList == null | sendList.size() <= 0) {
                    //睡眠时间2秒
                    Thread.sleep(2000);

                    continue;
                }

                for (MessageSendValue messageSendValue : sendList) {
                    //更新注册信息
                    Registerprocess.updateWaitCount(messageSendValue, -1);

                    //发送消息
                    sendCenter.send(messageSendValue, false);
                }

            } catch (MyException e) {
                logger.error("smscenterException:", e);
            } catch (InterruptedException e) {
                logger.error("smscenterException:", e);
            }
        }
    }
}
