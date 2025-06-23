package com.cpms.model.dao;

import com.cpms.model.entity.User;
import com.cpms.util.db.DatabaseConnection;
import com.cpms.util.db.DatabaseUtil;
import com.cpms.util.constants.SQLConstants;
import com.cpms.util.log.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问对象类
 * 提供对User表的增删改查操作
 */
public class UserDAO {
    private DatabaseConnection dbc = null;
    private Connection conn = null;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public UserDAO() throws SQLException {
        dbc = new DatabaseConnection();
        conn = dbc.getConnection();
    }
    
    /**
     * 添加用户
     * @param user 用户对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean addUser(User user) throws SQLException {
        try {
            int result = DatabaseUtil.executeUpdate(conn, SQLConstants.User.INSERT, 
                    user.getUsername(), 
                    user.getPassword(), 
                    user.getRealName(), 
                    user.getPhone(), 
                    user.getRoleID(), 
                    user.getBuildingID(), 
                    user.getManagedBuildingID(), 
                    user.getAccountStatus(), 
                    user.getRoomNumber());
            Logger.info("用户添加操作完成，影响行数: " + result);
            return result > 0;
        } catch (SQLException e) {
            Logger.error("添加用户失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updateUser(User user) throws SQLException {
        try {
            int result = DatabaseUtil.executeUpdate(conn, SQLConstants.User.UPDATE, 
                    user.getUsername(), 
                    user.getPassword(), 
                    user.getRealName(), 
                    user.getPhone(), 
                    user.getRoleID(), 
                    user.getBuildingID(), 
                    user.getManagedBuildingID(), 
                    user.getAccountStatus(), 
                    user.getRoomNumber(), 
                    user.getUserID());
            Logger.info("用户更新操作完成，影响行数: " + result);
            return result > 0;
        } catch (SQLException e) {
            Logger.error("更新用户失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 更新用户密码
     * @param userID 用户ID
     * @param newPassword 新密码
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updatePassword(int userID, String newPassword) throws SQLException {
        try {
            int result = DatabaseUtil.executeUpdate(conn, SQLConstants.User.UPDATE_PASSWORD, newPassword, userID);
            Logger.info("用户密码更新操作完成，影响行数: " + result);
            return result > 0;
        } catch (SQLException e) {
            Logger.error("更新用户密码失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 更新用户状态
     * @param userID 用户ID
     * @param status 状态（0-禁用，1-启用）
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updateStatus(int userID, int status) throws SQLException {
        try {
            int result = DatabaseUtil.executeUpdate(conn, SQLConstants.User.UPDATE_STATUS, status, userID);
            Logger.info("用户状态更新操作完成，影响行数: " + result);
            return result > 0;
        } catch (SQLException e) {
            Logger.error("更新用户状态失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 根据ID删除用户
     * @param userID 用户ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deleteUser(int userID) throws SQLException {
        try {
            int result = DatabaseUtil.executeUpdate(conn, SQLConstants.User.DELETE, userID);
            Logger.info("用户删除操作完成，影响行数: " + result);
            return result > 0;
        } catch (SQLException e) {
            Logger.error("删除用户失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 根据ID查找用户
     * @param userID 用户ID
     * @return User 用户对象
     * @throws SQLException 数据库操作异常
     */
    public User findByID(int userID) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQLConstants.User.SELECT_BY_ID);
            pstmt.setInt(1, userID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            Logger.error("根据ID查找用户失败: " + e.getMessage());
            throw e;
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
    }
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return User 用户对象
     * @throws SQLException 数据库操作异常
     */
    public User findByUsername(String username) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQLConstants.User.SELECT_BY_USERNAME);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            Logger.error("根据用户名查找用户失败: " + e.getMessage());
            throw e;
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
    }
    
    /**
     * 根据角色ID查找用户
     * @param roleID 角色ID
     * @return List<User> 用户列表
     * @throws SQLException 数据库操作异常
     */
    public List<User> findByRoleID(int roleID) throws SQLException {
        List<User> userList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQLConstants.User.SELECT_BY_ROLE);
            pstmt.setInt(1, roleID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userList.add(mapResultSetToUser(rs));
            }
            return userList;
        } catch (SQLException e) {
            Logger.error("根据角色ID查找用户失败: " + e.getMessage());
            throw e;
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
    }
    
    /**
     * 根据楼栋ID查找业主
     * @param buildingID 楼栋ID
     * @return 业主列表
     * @throws SQLException 数据库异常
     */
    public List<User> findOwnersByBuildingID(int buildingID) throws SQLException {
        List<User> userList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQLConstants.User.SELECT_OWNERS_BY_BUILDING);
            pstmt.setInt(1, buildingID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userList.add(mapResultSetToUser(rs));
            }
            return userList;
        } catch (SQLException e) {
            Logger.error("根据楼栋ID查找业主失败: " + e.getMessage());
            throw e;
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
    }
    
    /**
     * 根据管理楼栋ID查找管家
     * @param managedBuildingID 管理楼栋ID
     * @return User 管家对象
     * @throws SQLException 数据库操作异常
     */
    public User findManagerByManagedBuildingID(int managedBuildingID) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQLConstants.User.SELECT_MANAGER_BY_BUILDING);
            pstmt.setInt(1, managedBuildingID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            Logger.error("根据管理楼栋ID查找管家失败: " + e.getMessage());
            throw e;
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
    }
    
    /**
     * 查询所有用户
     * @return List<User> 用户列表
     * @throws SQLException 数据库操作异常
     */
    public List<User> findAll() throws SQLException {
        List<User> userList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQLConstants.User.SELECT_ALL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userList.add(mapResultSetToUser(rs));
            }
            return userList;
        } catch (SQLException e) {
            Logger.error("查询所有用户失败: " + e.getMessage());
            throw e;
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
    }
    
    /**
     * 根据角色ID查找第一个用户
     * @param roleID 角色ID
     * @return User 用户对象
     * @throws SQLException 数据库操作异常
     */
    public User findFirstByRoleID(int roleID) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQLConstants.User.SELECT_ACTIVE_BY_ROLE);
            pstmt.setInt(1, roleID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            Logger.error("根据角色ID查找第一个用户失败: " + e.getMessage());
            throw e;
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
    }
    
    /**
     * 将ResultSet映射为User对象
     * @param rs ResultSet对象
     * @return User 用户对象
     * @throws SQLException 数据库操作异常
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserID(rs.getInt("UserID"));
        user.setUsername(rs.getString("Username"));
        user.setPassword(rs.getString("Password"));
        user.setRealName(rs.getString("RealName"));
        user.setPhone(rs.getString("Phone"));
        user.setRoleID(rs.getInt("RoleID"));
        
        int buildingID = rs.getInt("BuildingID");
        if (!rs.wasNull()) {
            user.setBuildingID(buildingID);
        }
        
        int managedBuildingID = rs.getInt("ManagedBuildingID");
        if (!rs.wasNull()) {
            user.setManagedBuildingID(managedBuildingID);
        }
        
        // 设置房间号
        String roomNumber = rs.getString("RoomNumber");
        if (roomNumber != null) {
            user.setRoomNumber(roomNumber);
        }
        
        user.setAccountStatus(rs.getInt("AccountStatus"));
        user.setCreateTime(rs.getTimestamp("CreateTime"));
        user.setUpdateTime(rs.getTimestamp("UpdateTime"));
        return user;
    }
    
    /**
     * 关闭数据库连接
     */
    public void close() {
        try {
            if (dbc != null) {
                dbc.close();
                Logger.info("UserDAO数据库连接已关闭");
            }
        } catch (Exception e) {
            Logger.error("关闭UserDAO数据库连接失败: " + e.getMessage());
        }
    }
}