package com.cpms.model.dao;

import com.cpms.model.entity.Role;
import com.cpms.util.db.DatabaseConnection;
import com.cpms.util.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色数据访问对象类
 * 提供对Role表的增删改查操作
 */
public class RoleDAO {
    private DatabaseConnection dbc = null;
    private Connection conn = null;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public RoleDAO() throws SQLException {
        dbc = new DatabaseConnection();
        conn = dbc.getConnection();
    }
    
    /**
     * 添加角色
     * @param role 角色对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean addRole(Role role) throws SQLException {
        // 如果角色ID已设置，使用指定的ID插入
        if (role.getRoleID() > 0) {
            String sql = "INSERT INTO Role(RoleID, RoleName, RoleDesc) VALUES(?, ?, ?)";
            int result = DatabaseUtil.executeUpdate(conn, sql, role.getRoleID(), role.getRoleName(), role.getRoleDesc());
            return result > 0;
        } else {
            // 否则使用自动递增ID
            String sql = "INSERT INTO Role(RoleName, RoleDesc) VALUES(?, ?)";
            int result = DatabaseUtil.executeUpdate(conn, sql, role.getRoleName(), role.getRoleDesc());
            return result > 0;
        }
    }
    
    /**
     * 更新角色
     * @param role 角色对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updateRole(Role role) throws SQLException {
        String sql = "UPDATE Role SET RoleName=?, RoleDesc=? WHERE RoleID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, role.getRoleName(), role.getRoleDesc(), role.getRoleID());
        return result > 0;
    }
    
    /**
     * 根据ID删除角色
     * @param roleID 角色ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deleteRole(int roleID) throws SQLException {
        String sql = "DELETE FROM Role WHERE RoleID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, roleID);
        return result > 0;
    }
    
    /**
     * 根据ID查找角色
     * @param roleID 角色ID
     * @return Role 角色对象
     * @throws SQLException 数据库操作异常
     */
    public Role findByID(int roleID) throws SQLException {
        String sql = "SELECT * FROM Role WHERE RoleID=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToRole(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 根据角色名称查找角色
     * @param roleName 角色名称
     * @return Role 角色对象
     * @throws SQLException 数据库操作异常
     */
    public Role findByName(String roleName) throws SQLException {
        String sql = "SELECT * FROM Role WHERE RoleName=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roleName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToRole(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 查询所有角色
     * @return List<Role> 角色列表
     * @throws SQLException 数据库操作异常
     */
    public List<Role> findAll() throws SQLException {
        String sql = "SELECT * FROM Role ORDER BY RoleID ASC";
        List<Role> roleList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                roleList.add(mapResultSetToRole(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return roleList;
    }
    
    /**
     * 将ResultSet映射为Role对象
     * @param rs ResultSet对象
     * @return Role 角色对象
     * @throws SQLException 数据库操作异常
     */
    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRoleID(rs.getInt("RoleID"));
        role.setRoleName(rs.getString("RoleName"));
        role.setRoleDesc(rs.getString("RoleDesc"));
        role.setCreateTime(rs.getTimestamp("CreateTime"));
        role.setUpdateTime(rs.getTimestamp("UpdateTime"));
        return role;
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