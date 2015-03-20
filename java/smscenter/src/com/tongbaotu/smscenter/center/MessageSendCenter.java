package com.tongbaotu.smscenter.center;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.tongbaotu.smscenter.config.Config;
import com.tongbaotu.smscenter.config.ConfigManager;
import com.tongbaotu.smscenter.dao.MessageSendDao;
import com.tongbaotu.smscenter.exception.MyException;
import com.tongbaotu.smscenter.pojo.MessageSendValue;
import com.tongbaotu.smscenter.queue.MessageRegressionQueue;
import com.tongbaotu.smscenter.util.GlobalConst;

/**
 * 信息发送中心
 * @author Tim
 */
public class MessageSendCenter {
    private static Logger logger = LoggerFactory.getLogger(MessageSendCenter.class);

    private static MessageSendCenter center = new MessageSendCenter();

    private MessageRegisterProcessCenter registerCenter = MessageRegisterProcessCenter
        .getInstance();

    private MessageRegressionQueue queue = MessageRegressionQueue.getInstance();

    private Config config = ConfigManager.getInstance().getConfig();

    private MessageSendDao dao = MessageSendDao.getInstance();

    private static final String CHARSET = "utf-8";

    private static final int MAXLEN = 304;

    /**
     * 私有构造
     */
    private MessageSendCenter() {
    }

    /**
     * 获取实例
     * @return
     */
    public static MessageSendCenter getInstance() {
        return center;
    }

    /**
     * 发送
     * @param messageSendValue
     * @param filterFlag
     * @throws MyException
     */
    public void send(MessageSendValue messageSendValue, boolean filterFlag) throws MyException {
        //消息类型
        String message = messageSendValue.getMessage();
        //消息不能超过304个字符
        if (message.getBytes().length > MAXLEN) {
            return;
        }

        //发送规则过滤
        if (filterFlag) {
            MessageSendRule rule = new MessageSendRule();
            //不执行
            if (!rule.filter(messageSendValue)) {
                //放入回扫队列，等待下次发送
                queue.put(messageSendValue);

                return;
            }
        }
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(config.getUrl());

        //在头文件中设置转码
        post.addRequestHeader("Content-Type", GlobalConst.CONTENTTYPE);

        //名称值对
        NameValuePair[] data =
            {
                new NameValuePair(config.getUserNameParameter(), config.getUserName()),
                new NameValuePair(config.getPasswordParameter(), config.getPassword()),
                new NameValuePair(config.getMobileParameter(), messageSendValue.getPhoneNumber()),
                new NameValuePair(config.getMessageParameter(), messageSendValue.getMessage()
                    + config.getSuffix())};

        //设置请求
        post.setRequestBody(data);

        //执行
        try {
            client.executeMethod(post);
        } catch (IOException e) {
            throw new MyException("HttpClient Error:" + e.getMessage());
        }

        //获取返回值
        String result = "";
        try {
            result = parse(new String(post.getResponseBodyAsString().getBytes(CHARSET)));
        } catch (IOException e) {
            throw new MyException("Response Result Error:" + e.getMessage());
        }

        //打印返回信息
        StringBuilder returnMsg = new StringBuilder();
        returnMsg.append("手机号：").append(messageSendValue.getPhoneNumber());
        returnMsg.append(("1".equals(result) ? "短信发送成功" : "短信发送失败"));
        logger.debug(returnMsg.toString());

        //关闭连接
        post.releaseConnection();

        //插入数据库
        dao.insert(messageSendValue);
    }

    /**
     * 消息发送规则
     * @author Tim
     */
    private class MessageSendRule {
        public boolean filter(MessageSendValue messageSendValue) {
            //本次发送短信中同一号码不能只能发送一次
            if (!registerCenter.isSend(messageSendValue)) {
                //放入缓存队列，等待下次发送
                return false;
            }

            return true;
        }
    }

    /**
     * 解析返回结果
     * @param protocolXML
     * @return
     * @throws MyException 
     */
    private static String parse(String protocolXML) throws MyException {
        String result = null;
        try {
            SAXReader builder = new SAXReader(false);
            Document doc = builder.read(new InputSource(new StringReader(protocolXML)));
            List<Element> resultNode = doc.selectNodes("//Root/Result");

            Iterator<Element> iter = resultNode.iterator();

            while (iter.hasNext()) {
                Element e = iter.next();
                result = e.getText();
            }

        } catch (Exception e) {
            throw new MyException("Parse Result Error:" + e.getMessage());
        }

        return result;
    }
}
