package com.cpms.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库操作工具类
 * 提供通用的数据库操作方法
 */
public class DatabaseUtil {
    
    /**
     * 执行查询操作
     * @param conn 数据库连接
     * @param sql SQL语句
     * @param params 参数数组
     * @return ResultSet 查询结果集
     * @throws SQLException 数据库操作异常
     */
    public static ResultSet executeQuery(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }
        return pstmt.executeQuery();
    }
    
    /**
     * 执行更新操作（插入、更新、删除）
     * @param conn 数据库连接
     * @param sql SQL语句
     * @param params 参数数组
     * @return int 影响行数
     * @throws SQLException 数据库操作异常
     */
    public static int executeUpdate(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            return pstmt.executeUpdate();
        } finally {
            closePreparedStatement(pstmt);
        }
    }
    
    /**
     * 关闭ResultSet
     * @param rs ResultSet对象
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // 记录关闭ResultSet时的异常，但不抛出
                System.err.println("关闭ResultSet时发生异常: " + e.getMessage());
            }
        }
    }
    
    /**
     * 关闭PreparedStatement
     * @param pstmt PreparedStatement对象
     */
    public static void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                // 记录关闭PreparedStatement时的异常，但不抛出
                System.err.println("关闭PreparedStatement时发生异常: " + e.getMessage());
            }
        }
    }
    
    /**
     * 关闭Connection
     * @param conn Connection对象
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // 记录关闭Connection时的异常，但不抛出
                System.err.println("关闭Connection时发生异常: " + e.getMessage());
            }
        }
    }
    
    /**
     * 关闭数据库资源
     * @param rs ResultSet对象
     * @param pstmt PreparedStatement对象
     */
    public static void closeResources(ResultSet rs, PreparedStatement pstmt) {
        closeResultSet(rs);
        closePreparedStatement(pstmt);
    }
    
    /**
     * 关闭所有数据库资源
     * @param rs ResultSet对象
     * @param pstmt PreparedStatement对象
     * @param conn Connection对象
     */
    public static void closeAllResources(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        closeResultSet(rs);
        closePreparedStatement(pstmt);
        closeConnection(conn);
    }
}