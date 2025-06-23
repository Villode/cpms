package com.cpms.controller.common;

import com.cpms.model.dao.UserDAO;
import com.cpms.model.entity.User;
import com.cpms.util.security.MD5Util;

import java.sql.SQLException;

/**
 * 用户控制器
 * 处理用户相关的业务逻辑
 */
public class UserController {
    
    /**
     * 修改用户密码
     * @param userID 用户ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    public boolean changePassword(int userID, String oldPassword, String newPassword) {
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            
            // 查询用户
            User user = userDAO.findByID(userID);
            if (user == null) {
                System.out.println("用户不存在: " + userID);
                return false;
            }
            
            // 验证原密码
            String storedPassword = user.getPassword();
            String md5OldPassword = MD5Util.MD5Encode(oldPassword, "UTF-8");
            
            if (!md5OldPassword.equals(storedPassword)) {
                System.out.println("原密码不正确");
                return false;
            }
            
            // 加密新密码
            String md5NewPassword = MD5Util.MD5Encode(newPassword, "UTF-8");
            
            // 更新密码
            return userDAO.updatePassword(userID, md5NewPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 是否成功
     */
    public boolean updateUserInfo(User user) {
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            return userDAO.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
    
    /**
     * 根据ID查询用户
     * @param userID 用户ID
     * @return 用户对象
     */
    public User getUserByID(int userID) {
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            return userDAO.findByID(userID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    public User getUserByUsername(String username) {
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            return userDAO.findByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
} 