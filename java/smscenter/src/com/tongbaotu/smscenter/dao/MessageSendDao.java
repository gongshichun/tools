package com.tongbaotu.smscenter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.tongbaotu.smscenter.exception.MyException;
import com.tongbaotu.smscenter.pojo.MessageSendValue;
import com.tongbaotu.smscenter.util.GlobalConst;
import com.tongbaotu.smscenter.util.MySQLManager;
import com.tongbaotu.smscenter.util.TimeUtils;

/**
 * message dao
 * @author Tim
 */
public class MessageSendDao {
    private static MySQLManager manager = MySQLManager.getInstance();

    private static final String INSERT_MESSAGE =
        "INSERT INTO tb_message (customer_id, message_batch_id, content, tearget, create_time)"
            + " VALUES(?, ?, ?, ?, ?)";

    private static MessageSendDao dao = new MessageSendDao();

    private MessageSendDao() {
    }

    public static MessageSendDao getInstance() {
        return dao;
    }

    /**
     * insert into tb_message
     * @param sendValue
     * @throws MyException
     */
    public void insert(MessageSendValue sendValue) throws MyException {
        Connection conn = null;
        try {
            conn = manager.getConnection();
            try {
                PreparedStatement ps = conn.prepareStatement(INSERT_MESSAGE);

                int i = 1;
                ps.setString(i++, sendValue.getCustomer_id());
                ps.setString(i++, sendValue.getMessageBatchId());
                ps.setString(i++, sendValue.getMessage());
                ps.setInt(i++, GlobalConst.TARGET_TYPE_SMS);

                Timestamp stamp = Timestamp.valueOf(TimeUtils.getCurrentTime());
                ps.setTimestamp(i++, stamp);

                //execute
                ps.execute();
            } catch (SQLException e) {
                throw new MyException("Message insert table Error" + e.getMessage());
            }

        } finally {
            manager.close(conn);
        }
    }
}
