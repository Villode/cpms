package com.cpms.model.dao;

import com.cpms.util.db.DatabaseConnection;
import com.cpms.util.db.DatabaseUtil;
import com.cpms.util.log.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DAO基类
 * 提供统一的数据库连接管理和异常处理
 */
public abstract class BaseDAO {
    
    protected DatabaseConnection dbc;
    protected Connection conn;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public BaseDAO() throws SQLException {
        dbc = new DatabaseConnection();
        conn = dbc.getConnection();
        Logger.debug("数据库连接初始化成功: " + this.getClass().getSimpleName());
    }
    
    /**
     * 关闭数据库连接
     */
    public void close() {
        if (dbc != null) {
            dbc.close();
            Logger.debug("数据库连接关闭成功: " + this.getClass().getSimpleName());
        }
    }
    
    /**
     * 执行更新操作的通用方法
     * @param sql SQL语句
     * @param params 参数
     * @return 影响的行数
     * @throws SQLException 数据库操作异常
     */
    protected int executeUpdate(String sql, Object... params) throws SQLException {
        Logger.debug("执行SQL更新: " + sql);
        try {
            return DatabaseUtil.executeUpdate(conn, sql, params);
        } catch (SQLException e) {
            Logger.error("SQL更新执行失败: " + sql, e);
            throw e;
        }
    }
    
    /**
     * 检查连接是否有效
     * @return 连接是否有效
     */
    protected boolean isConnectionValid() {
        try {
            return conn != null && !conn.isClosed() && conn.isValid(5);
        } catch (SQLException e) {
            Logger.warn("检查数据库连接有效性时发生异常", e);
            return false;
        }
    }
    
    /**
     * 重新连接数据库
     * @throws SQLException 重连异常
     */
    protected void reconnect() throws SQLException {
        close();
        try {
            dbc = new DatabaseConnection();
            conn = dbc.getConnection();
            Logger.info("数据库重连成功: " + this.getClass().getSimpleName());
        } catch (SQLException e) {
            Logger.error("数据库重连失败: " + this.getClass().getSimpleName(), e);
            throw e;
        }
    }
}