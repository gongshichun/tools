package com.tongbaotu.pushengine.engine;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongbaotu.pushengine.exception.MyException;
import com.tongbaotu.pushengine.helper.MySQLHelper;

public class SystemMessage {

	private static MySQLHelper mysqlHelper = new MySQLHelper().getInstance();

	private static final Logger logger = LoggerFactory.getLogger(MySQLHelper.class);
	private static final String INSERTSTRING =  "INSERT INTO tb_message (customer_id, message_batch_id, content, target)" + " VALUES(?, ?, ?, ?)";
	private static SystemMessage sm = new SystemMessage();

	private SystemMessage() {
	
	}

	public static SystemMessage getInstance() {
	    return sm;
	}

	public void insert(Integer customerId, String messageBatchId,  String content, String teaget) throws MyException {
		Connection conn = null;
        try {
            conn = mysqlHelper.getConnection();
            try {
                PreparedStatement ps = conn.prepareStatement(INSERTSTRING);
                int i = 1;
                ps.setInt(i++, customerId);
                ps.setString(i++, messageBatchId);
                ps.setString(i++, content);
                ps.setString(i++, teaget);
                ps.execute();
            } catch (SQLException e) {
                throw new MyException("Message insert table Error" + e.getMessage());
            }

        } finally {
        	mysqlHelper.close(conn);
        }
	}

}
