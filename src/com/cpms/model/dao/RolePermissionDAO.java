package com.cpms.model.dao;

import com.cpms.model.entity.Permission;
import com.cpms.model.entity.RolePermission;
import com.cpms.util.db.DatabaseConnection;
import com.cpms.util.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限关联数据访问对象类
 * 提供对RolePermission表的增删改查操作
 */
public class RolePermissionDAO {
    private DatabaseConnection dbc = null;
    private Connection conn = null;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public RolePermissionDAO() throws SQLException {
        dbc = new DatabaseConnection();
        conn = dbc.getConnection();
    }
    
    /**
     * 添加角色权限关联
     * @param rolePermission 角色权限关联对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean addRolePermission(RolePermission rolePermission) throws SQLException {
        String sql = "INSERT INTO RolePermission(RoleID, PermissionID) VALUES(?, ?)";
        int result = DatabaseUtil.executeUpdate(conn, sql, 
                rolePermission.getRoleID(), 
                rolePermission.getPermissionID());
        return result > 0;
    }
    
    /**
     * 批量添加角色权限关联
     * @param roleID 角色ID
     * @param permissionIDs 权限ID列表
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean batchAddRolePermissions(int roleID, List<Integer> permissionIDs) throws SQLException {
        // 开始事务
        conn.setAutoCommit(false);
        
        try {
            // 先删除该角色的所有权限
            deleteRolePermissionsByRoleID(roleID);
            
            // 批量添加新的权限
            String sql = "INSERT INTO RolePermission(RoleID, PermissionID) VALUES(?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            for (Integer permissionID : permissionIDs) {
                pstmt.setInt(1, roleID);
                pstmt.setInt(2, permissionID);
                pstmt.addBatch();
            }
            
            int[] results = pstmt.executeBatch();
            
            // 提交事务
            conn.commit();
            
            // 检查结果
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            
            return true;
        } catch (SQLException e) {
            // 回滚事务
            conn.rollback();
            throw e;
        } finally {
            // 恢复自动提交
            conn.setAutoCommit(true);
        }
    }
    
    /**
     * 删除角色权限关联
     * @param id 主键ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deleteRolePermission(int id) throws SQLException {
        String sql = "DELETE FROM RolePermission WHERE ID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, id);
        return result > 0;
    }
    
    /**
     * 根据角色ID删除角色权限关联
     * @param roleID 角色ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deleteRolePermissionsByRoleID(int roleID) throws SQLException {
        String sql = "DELETE FROM RolePermission WHERE RoleID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, roleID);
        return result >= 0; // 即使没有记录被删除也认为是成功的
    }
    
    /**
     * 根据权限ID删除角色权限关联
     * @param permissionID 权限ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deleteRolePermissionsByPermissionID(int permissionID) throws SQLException {
        String sql = "DELETE FROM RolePermission WHERE PermissionID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, permissionID);
        return result >= 0; // 即使没有记录被删除也认为是成功的
    }
    
    /**
     * 根据角色ID和权限ID删除角色权限关联
     * @param roleID 角色ID
     * @param permissionID 权限ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deleteRolePermission(int roleID, int permissionID) throws SQLException {
        String sql = "DELETE FROM RolePermission WHERE RoleID=? AND PermissionID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, roleID, permissionID);
        return result > 0;
    }
    
    /**
     * 查询角色的所有权限ID
     * @param roleID 角色ID
     * @return List<Integer> 权限ID列表
     * @throws SQLException 数据库操作异常
     */
    public List<Integer> findPermissionIDsByRoleID(int roleID) throws SQLException {
        String sql = "SELECT PermissionID FROM RolePermission WHERE RoleID=?";
        List<Integer> permissionIDs = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                permissionIDs.add(rs.getInt("PermissionID"));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return permissionIDs;
    }
    
    /**
     * 查询角色的所有权限
     * @param roleID 角色ID
     * @return List<Permission> 权限列表
     * @throws SQLException 数据库操作异常
     */
    public List<Permission> findPermissionsByRoleID(int roleID) throws SQLException {
        String sql = "SELECT p.* FROM Permission p " +
                     "JOIN RolePermission rp ON p.PermissionID = rp.PermissionID " +
                     "WHERE rp.RoleID = ? " +
                     "ORDER BY p.PermissionID ASC";
        List<Permission> permissions = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Permission permission = new Permission();
                permission.setPermissionID(rs.getInt("PermissionID"));
                permission.setPermissionName(rs.getString("PermissionName"));
                permission.setPermissionCode(rs.getString("PermissionCode"));
                permission.setPermissionDesc(rs.getString("PermissionDesc"));
                permission.setPermissionType(rs.getInt("PermissionType"));
                permission.setCreateTime(rs.getTimestamp("CreateTime"));
                permission.setUpdateTime(rs.getTimestamp("UpdateTime"));
                permissions.add(permission);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return permissions;
    }
    
    /**
     * 查询拥有指定权限的所有角色ID
     * @param permissionID 权限ID
     * @return List<Integer> 角色ID列表
     * @throws SQLException 数据库操作异常
     */
    public List<Integer> findRoleIDsByPermissionID(int permissionID) throws SQLException {
        String sql = "SELECT RoleID FROM RolePermission WHERE PermissionID=?";
        List<Integer> roleIDs = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, permissionID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                roleIDs.add(rs.getInt("RoleID"));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return roleIDs;
    }
    
    /**
     * 检查角色是否拥有指定权限
     * @param roleID 角色ID
     * @param permissionID 权限ID
     * @return boolean 是否拥有权限
     * @throws SQLException 数据库操作异常
     */
    public boolean hasPermission(int roleID, int permissionID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM RolePermission WHERE RoleID=? AND PermissionID=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleID);
            pstmt.setInt(2, permissionID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return false;
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