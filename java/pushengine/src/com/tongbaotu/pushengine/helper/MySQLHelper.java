package com.tongbaotu.pushengine.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongbaotu.pushengine.exception.MyException;

/**
 * MySQL管理
 * @author Tim
 */
public class MySQLHelper {
    private static final Logger logger = LoggerFactory.getLogger(MySQLHelper.class);

    private static final MySQLHelper instance = new MySQLHelper();

    private DataSource dataSource;//dataSource

    //配置文件路径
    private static String CONFIG_PATH = "config/mysql.properties";

    public static MySQLHelper getInstance() {
        return instance;
    }

    /**
     * 初始化连接池
     * @throws YYDaoException
     */
    public void init(String configPath) throws MyException {
        logger.debug("init mysql.");

        Properties prop = new Properties();
        prop = new Properties();
        try {
            prop.load(new FileInputStream(CONFIG_PATH));
        } catch (FileNotFoundException e) {
            throw new MyException("ymhhouException mysql.properties FileNotFund:" + e.getMessage());
        } catch (IOException e) {
            throw new MyException("ymhhouException mysql.properties IOException:" + e.getMessage());
        }

        try {
            dataSource = BasicDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            throw new MyException("ymhhouException mysql createDataSource:" + e.getMessage());
        }

    }

    /**
     * 获取连接
     * @return
     * @throws YYDaoException
     */
    public Connection getConnection() throws MyException {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new MyException("ymhhouException mysql.properties IOException:" + e.getMessage());
        }

        return connection;
    }

    /**
     * 关闭连接，真实是放入连接池中
     * @param connection
     */
    public void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                connection = null;
            }
            connection = null;
        }
    }
}