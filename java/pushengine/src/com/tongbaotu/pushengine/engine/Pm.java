package com.tongbaotu.pushengine.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongbaotu.pushengine.helper.MySQLHelper;
import com.tongbaotu.pushengine.exception.MyException;

public class Pm {
	private static final Logger logger = LoggerFactory.getLogger(MySQLHelper.class);
	private static Pm pm = new Pm();
	private static final String INSERTSTRING = "INSERT INTO tb_pms (customer_id, title, type)" + " VALUES(?, ?, ?)";
	private static MySQLHelper mysqlHelper = MySQLHelper.getInstance();

	public Pm() {
		
	}

	public static Pm getInstance() {
		return pm;
	}
	
	public void insert(Integer customerId, String title, Integer type) throws MyException {
		Connection conn = null;
		try {
			conn  = mysqlHelper.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERTSTRING);
			int i = 1;
			ps.setInt(i++, customerId);
			ps.setString(i++, title);
	        ps.setInt(i++, type);
	        ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mysqlHelper.close(conn);
        }	
	 }
}
