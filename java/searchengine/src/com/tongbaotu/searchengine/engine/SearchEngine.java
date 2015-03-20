package com.tongbaotu.searchengine.engine;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongbaotu.searchengine.exception.MyException;
import com.tongbaotu.searchengine.pojo.AcceptValue;
import com.tongbaotu.searchengine.pojo.SendAppValue;
import com.tongbaotu.searchengine.pojo.SendMessageValue;
import com.tongbaotu.searchengine.pojo.SendValue;
import com.tongbaotu.searchengine.queue.AcceptQueue;
import com.tongbaotu.searchengine.queue.SendAppQueue;
import com.tongbaotu.searchengine.queue.SendMessageQueue;
import com.tongbaotu.searchengine.util.GlobalConst;
import com.tongbaotu.searchengine.util.MySQLManager;
import com.tongbaotu.searchengine.util.ParaChecker;

/**
 * 搜索引擎
 * @author Tim
 */
public class SearchEngine extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(SearchEngine.class);

    private static SearchEngine se = new SearchEngine();

    private AcceptQueue acceptQueue = AcceptQueue.getInstance();

    private SendAppQueue sendAppQueue = SendAppQueue.getInstance();

    private SendMessageQueue sendSmsQueue = SendMessageQueue.getInstance();

    /**
     * 私有构造
     */
    private SearchEngine() {
    }

    /**
     * 实例化
     * @return
     */
    public static SearchEngine getInstance() {
        return se;
    }

    /**
     * 服务启动
     */
    public void active() {
        try {
            //启动
            this.start();

            //记录日志
            logger.debug("SearchEngine start...");

        } catch (Exception e) {
            logger.error("SearchEngine start error!!!", e);
        }
    }

    /**
     * 运行主体
     */
    public void run() {
        while (true) {
            try {
                //从队列中获取
                AcceptValue acceptValue = acceptQueue.take();

                //处理数据
                DataProcessor processor = new DataProcessor();
                ArrayList<SendValue> sendList = processor.dealData(acceptValue);

                //推送发送队列
                push(acceptValue.getTargetType(), sendList);

            } catch (MyException | SQLException e) {
                logger.error("ymhhouException:", e);
            } catch (Throwable e) {
                //避免出现致命错误
                logger.error("ymhhouException:", e);
            }
        }
    }

    /**
     * 推送消息
     * @param targetType
     * @param sendList
     */
    private void push(int targetType, ArrayList<SendValue> sendList) {
        //为空即返回
        if (sendList == null || sendList.size() <= 0) {
            return;
        }

        //推送APP
        if (targetType == GlobalConst.TARGETTYPE_ANDROID
            || targetType == GlobalConst.TARGETTYPE_IOS
            || targetType == GlobalConst.TARGETTYPE_SYSTME) {
            for (SendValue sendValue : sendList) {
                sendAppQueue.push(sendValue);
            }
        }
        //推送短消息
        else if (targetType == GlobalConst.TARGETTYPE_SMS) {
            for (SendValue sendValue : sendList) {
                sendSmsQueue.push(sendValue);
            }
        }
    }

    /**
     * 数据处理类
     * @author Tim
     */
    private class DataProcessor {
        private MySQLManager mysqlManager = MySQLManager.getInstance();

        /**
         * 处理数据
         * @param acceptValue
         * @return
         * @throws MyException
         * @throws SQLException
         */
        public ArrayList<SendValue> dealData(AcceptValue acceptValue) throws MyException,
            SQLException {
            ArrayList<SendValue> sendList = new ArrayList<SendValue>();

            int type = acceptValue.getType();
            int targetType = acceptValue.getTargetType();
            String productId = acceptValue.getProductId();
            String message = acceptValue.getMessage();
            String messageBatchId = acceptValue.getMessageBatchId();
            //APP
            if (targetType == GlobalConst.TARGETTYPE_ANDROID
                || targetType == GlobalConst.TARGETTYPE_IOS) {
                dealAppData(type, targetType, message, productId, messageBatchId, sendList);
            }
            //message
            else if (targetType == GlobalConst.TARGETTYPE_SMS
                || targetType == GlobalConst.TARGETTYPE_SYSTME) {
                dealMessageData(type, targetType, message, productId, messageBatchId, sendList);
            }

            return sendList;
        }

        /**
         * 处理APP数据
         * @param type
         * @param targetType
         * @param message
         * @param productId
         * @param sendList
         * @throws MyException
         * @throws SQLException
         */
        private void dealAppData(int type,
            int targetType,
            String message,
            String productId,
            String messageBatchId,
            ArrayList<SendValue> sendList) throws MyException, SQLException {
            Connection conn = null;
            try {
                conn = mysqlManager.getConnection();

                StringBuilder sql = new StringBuilder();
                //所有客户
                if (type == GlobalConst.TYPE_ALLCUSTOMER) {
                    sql.append("SELECT d.device_token, d.customer_id FROM tb_customer c,");
                    sql.append(" tb_device_token d WHERE c.id = d.customer_id");
                    sql.append(" AND device = '").append(
                        GlobalConst.TARGETTYPE_IOS == targetType
                            ? GlobalConst.DEVICE_TYPE_IOS
                            : GlobalConst.DEVICE_TYPE_ANDROID);
                    sql.append("'");
                }
                //产品下客户
                else if (type == GlobalConst.TYPE_PRODUCTCUSTOMER) {
                    if (!ParaChecker.isValidPara(productId)) {
                        return;
                    }
                    sql.append("SELECT d.device_token, d.customer_id FROM tb_customer c,");
                    sql.append(" tb_order o, tb_device_token d WHERE c.id = d.customer_id");
                    sql.append(" AND o.customer_id = c.id");
                    sql.append(" AND device = '").append(
                        GlobalConst.TARGETTYPE_IOS == targetType
                            ? GlobalConst.DEVICE_TYPE_IOS
                            : GlobalConst.DEVICE_TYPE_ANDROID);
                    sql.append("'");
                    sql.append(" AND o.product_id = '").append(productId).append("'");
                }

                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(sql.toString());
                while (rs.next()) {
                    String device_token = rs.getString(1);
                    int customer_id = rs.getInt(2);

                    SendAppValue appValue = new SendAppValue();
                    appValue.setCustomer_id(customer_id);
                    appValue.setDevice_token(device_token);
                    appValue.setMessage(message);
                    appValue.setMessageBatchId(messageBatchId);
                    appValue.setType(String.valueOf(targetType));
                    appValue.setProductId(productId);

                    sendList.add(appValue);

                }
            } finally {
                mysqlManager.close(conn);
            }
        }

        /**
         * 处理Message数据
         * @param type
         * @param targetType
         * @param message
         * @param productId
         * @param sendList
         * @throws MyException
         * @throws SQLException
         */
        private void dealMessageData(int type,
            int targetType,
            String message,
            String productId,
            String messageBatchId,
            ArrayList<SendValue> sendList) throws MyException, SQLException {
            Connection conn = null;
            try {
                conn = mysqlManager.getConnection();
                StringBuilder sql = new StringBuilder();

                //所有客户
                if (type == GlobalConst.TYPE_ALLCUSTOMER) {
                    sql.append("SELECT id, phoneNumber FROM tb_customer");
                }
                //产品下客户
                else if (type == GlobalConst.TYPE_PRODUCTCUSTOMER) {
                    if (!ParaChecker.isValidPara(productId)) {
                        return;
                    }
                    sql.append("SELECT c.id, c.phoneNumber FROM tb_customer c,");
                    sql.append(" tb_order o WHERE o.customer_id = c.id");
                    sql.append(" AND o.product_id = '").append(productId).append("'");
                }

                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(sql.toString());
                while (rs.next()) {
                    int customer_id = rs.getInt(1);
                    String phoneNumber = rs.getString(2);

                    if (targetType == GlobalConst.TARGETTYPE_SYSTME) {
                        SendAppValue appValue = new SendAppValue();
                        appValue.setCustomer_id(customer_id);
                        appValue.setDevice_token("");
                        appValue.setMessage(message);
                        appValue.setMessageBatchId(messageBatchId);
                        appValue.setType(String.valueOf(targetType));
                        appValue.setProductId(productId);

                        sendList.add(appValue);
                    } else {
                        SendMessageValue sendValue = new SendMessageValue();
                        sendValue.setCustomer_id(customer_id);
                        sendValue.setMessage(message);
                        sendValue.setMessageBatchId(messageBatchId);
                        sendValue.setPhoneNumber(phoneNumber);
                        sendList.add(sendValue);
                    }

                }
            } finally {
                mysqlManager.close(conn);
            }
        }
    }
}
