package com.cpms.util.db;

import com.cpms.util.config.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 * 提供数据库连接和关闭功能
 */
public class DatabaseConnection {
    // 配置管理器
    private static final ConfigManager config = ConfigManager.getInstance();
    
    // 数据库连接参数
    private static final String DRIVER = config.getProperty("database.driver", "com.mysql.cj.jdbc.Driver");
    private static final String URL = config.getProperty("database.url", "jdbc:mysql://localhost:3306/cpms_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false");
    private static final String USERNAME = config.getProperty("database.username", "root");
    private static final String PASSWORD = config.getProperty("database.password", "");
    
    // 数据库连接对象
    private Connection connection;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public DatabaseConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("数据库驱动加载失败！", e);
        }
    }
    
    /**
     * 获取数据库连接对象
     * @return Connection 数据库连接对象
     */
    public Connection getConnection() {
        return this.connection;
    }
    
    /**
     * 关闭数据库连接
     */
    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
} 