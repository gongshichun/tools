package com.tongbaotu.smscenter.center;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.tongbaotu.smscenter.pojo.MessageRegisterValue;
import com.tongbaotu.smscenter.pojo.MessageSendValue;
import com.tongbaotu.smscenter.queue.MessageRegisterQueue;
import com.tongbaotu.smscenter.util.GlobalConst;
import com.tongbaotu.smscenter.util.TimeUtils;

/**
 * 消息注册中心
 * @author Tim
 */
public class MessageRegisterProcessCenter {
    private static MessageRegisterProcessCenter center = new MessageRegisterProcessCenter();

    private MessageRegisterQueue registerQueue = MessageRegisterQueue.getInstance();

    //读写锁
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 私有构造
     */
    private MessageRegisterProcessCenter() {
    }

    /**
     * 实例化
     * @return
     */
    public static MessageRegisterProcessCenter getInstance() {
        return center;
    }

    /**
     * 注册
     * @param sendValue
     */
    public void register(MessageSendValue sendValue) {
        MessageRegisterValue value = new MessageRegisterValue();
        value.setPhoneNum(sendValue.getPhoneNumber());
        value.setTimeStamp(sendValue.getTimeStamp());
        value.setWaitCount(0);

        registerQueue.put(value);
    }

    /**
     * 检查注册信息
     * @param sendValue
     * @return false-可以发送, true-需要放入回扫队列
     */
    public boolean isSend(MessageSendValue sendValue) {
        //获取注册队列
        MessageRegisterValue value = registerQueue.get(sendValue);

        //不存在，则可以发送
        if (value == null) {
            //先进行登记
            register(sendValue);

            return true;
        }

        //等待数量
        lock.readLock().lock();
        long waitCount = value.getWaitCount();
        lock.readLock().unlock();

        long millidSecond = GlobalConst.MILLISECOND;
        String registerTimeStamp = value.getTimeStamp();
        //数量小于等于判定本次是否可以发送
        if (waitCount <= 0) {
            String sendTimeStamp = sendValue.getTimeStamp();
            //大于等于60秒
            if (TimeUtils.isSend(sendTimeStamp, registerTimeStamp, millidSecond)) {
                //更新注册时间戳
                value.setTimeStamp(sendTimeStamp);
                registerQueue.put(value);

                return true;
            }
        }

        sendValue.setTimeStamp(TimeUtils.plusSecond(registerTimeStamp, (waitCount + 1)
            * millidSecond));

        //更新WaitCount
        updateWaitCount(sendValue, 1);

        return false;
    }

    /**
     * 获取等待个数
     * @param sendValue
     * @return
     */
    public long getWaitCount(MessageSendValue sendValue) {
        //获取注册队列
        MessageRegisterValue value = registerQueue.get(sendValue);

        return value.getWaitCount();
    }

    /**
     * 更新注册信息
     * @param sendValue
     */
    public synchronized void updateWaitCount(MessageSendValue sendValue, int count) {
        //获取注册队列
        MessageRegisterValue value = registerQueue.get(sendValue);

        //删减一条等待记录
        lock.writeLock().lock();
        value.setWaitCount(value.getWaitCount() + count);

        //更新
        registerQueue.put(value);
        lock.writeLock().unlock();
    }
}
