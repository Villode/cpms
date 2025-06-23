package com.cpms.model.dao;

import com.cpms.model.entity.Permission;
import com.cpms.util.db.DatabaseConnection;
import com.cpms.util.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限数据访问对象类
 * 提供对Permission表的增删改查操作
 */
public class PermissionDAO {
    private DatabaseConnection dbc = null;
    private Connection conn = null;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public PermissionDAO() throws SQLException {
        dbc = new DatabaseConnection();
        conn = dbc.getConnection();
    }
    
    /**
     * 添加权限
     * @param permission 权限对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean addPermission(Permission permission) throws SQLException {
        // 如果权限ID已设置，使用指定的ID插入
        if (permission.getPermissionID() > 0) {
            String sql = "INSERT INTO Permission(PermissionID, PermissionName, PermissionCode, PermissionDesc, PermissionType) VALUES(?, ?, ?, ?, ?)";
            int result = DatabaseUtil.executeUpdate(conn, sql, 
                    permission.getPermissionID(), 
                    permission.getPermissionName(), 
                    permission.getPermissionCode(), 
                    permission.getPermissionDesc(), 
                    permission.getPermissionType());
            return result > 0;
        } else {
            // 否则使用自动递增ID
            String sql = "INSERT INTO Permission(PermissionName, PermissionCode, PermissionDesc, PermissionType) VALUES(?, ?, ?, ?)";
            int result = DatabaseUtil.executeUpdate(conn, sql, 
                    permission.getPermissionName(), 
                    permission.getPermissionCode(), 
                    permission.getPermissionDesc(), 
                    permission.getPermissionType());
            return result > 0;
        }
    }
    
    /**
     * 更新权限
     * @param permission 权限对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updatePermission(Permission permission) throws SQLException {
        String sql = "UPDATE Permission SET PermissionName=?, PermissionCode=?, PermissionDesc=?, PermissionType=? WHERE PermissionID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, 
                permission.getPermissionName(), 
                permission.getPermissionCode(), 
                permission.getPermissionDesc(), 
                permission.getPermissionType(), 
                permission.getPermissionID());
        return result > 0;
    }
    
    /**
     * 根据ID删除权限
     * @param permissionID 权限ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deletePermission(int permissionID) throws SQLException {
        String sql = "DELETE FROM Permission WHERE PermissionID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, permissionID);
        return result > 0;
    }
    
    /**
     * 根据ID查找权限
     * @param permissionID 权限ID
     * @return Permission 权限对象
     * @throws SQLException 数据库操作异常
     */
    public Permission findByID(int permissionID) throws SQLException {
        String sql = "SELECT * FROM Permission WHERE PermissionID=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, permissionID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPermission(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 根据权限代码查找权限
     * @param permissionCode 权限代码
     * @return Permission 权限对象
     * @throws SQLException 数据库操作异常
     */
    public Permission findByCode(String permissionCode) throws SQLException {
        String sql = "SELECT * FROM Permission WHERE PermissionCode=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, permissionCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPermission(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 查询所有权限
     * @return List<Permission> 权限列表
     * @throws SQLException 数据库操作异常
     */
    public List<Permission> findAll() throws SQLException {
        String sql = "SELECT * FROM Permission ORDER BY PermissionID ASC";
        List<Permission> permissionList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                permissionList.add(mapResultSetToPermission(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return permissionList;
    }
    
    /**
     * 根据权限类型查询权限
     * @param permissionType 权限类型
     * @return List<Permission> 权限列表
     * @throws SQLException 数据库操作异常
     */
    public List<Permission> findByType(int permissionType) throws SQLException {
        String sql = "SELECT * FROM Permission WHERE PermissionType=? ORDER BY PermissionID ASC";
        List<Permission> permissionList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, permissionType);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                permissionList.add(mapResultSetToPermission(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return permissionList;
    }
    
    /**
     * 将ResultSet映射为Permission对象
     * @param rs ResultSet对象
     * @return Permission 权限对象
     * @throws SQLException 数据库操作异常
     */
    private Permission mapResultSetToPermission(ResultSet rs) throws SQLException {
        Permission permission = new Permission();
        permission.setPermissionID(rs.getInt("PermissionID"));
        permission.setPermissionName(rs.getString("PermissionName"));
        permission.setPermissionCode(rs.getString("PermissionCode"));
        permission.setPermissionDesc(rs.getString("PermissionDesc"));
        permission.setPermissionType(rs.getInt("PermissionType"));
        permission.setCreateTime(rs.getTimestamp("CreateTime"));
        permission.setUpdateTime(rs.getTimestamp("UpdateTime"));
        return permission;
    }
    
    /**
     * 关闭数据库连接
     */
    public void close() {
        if (dbc != null) {
            dbc.close();
        }
    }
} 