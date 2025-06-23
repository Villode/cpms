package com.cpms.controller.admin;

import com.cpms.model.dao.RoleDAO;
import com.cpms.model.dao.UserDAO;
import com.cpms.model.entity.Role;
import com.cpms.model.entity.User;
import com.cpms.util.security.PasswordUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理控制器
 * 处理用户相关的业务逻辑
 */
public class UserController {
    
    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            return userDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
    
    /**
     * 根据角色ID获取用户列表
     * @param roleID 角色ID
     * @return 用户列表
     */
    public List<User> getUsersByRoleID(int roleID) {
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            return userDAO.findByRoleID(roleID);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
    
    /**
     * 添加用户
     * @param username 用户名
     * @param password 密码
     * @param realName 真实姓名
     * @param phone 联系电话
     * @param roleID 角色ID
     * @param buildingID 所属楼栋ID（业主使用）
     * @param managedBuildingID 管理的楼栋ID（管家使用）
     * @param accountStatus 账号状态
     * @return 是否成功
     */
    public boolean addUser(String username, String password, String realName, 
                           String phone, int roleID, Integer buildingID, 
                           Integer managedBuildingID, int accountStatus) {
        return addUser(username, password, realName, phone, roleID, buildingID, managedBuildingID, accountStatus, null);
    }
    
    /**
     * 添加用户（包含房间号）
     * @param username 用户名
     * @param password 密码
     * @param realName 真实姓名
     * @param phone 联系电话
     * @param roleID 角色ID
     * @param buildingID 所属楼栋ID（业主使用）
     * @param managedBuildingID 管理的楼栋ID（管家使用）
     * @param accountStatus 账号状态
     * @param roomNumber 房间号
     * @return 是否成功
     */
    public boolean addUser(String username, String password, String realName, 
                           String phone, int roleID, Integer buildingID, 
                           Integer managedBuildingID, int accountStatus, String roomNumber) {
        // 输入验证
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty() || 
            realName == null || realName.trim().isEmpty() || 
            phone == null || phone.trim().isEmpty() || 
            roleID <= 0) {
            return false;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            
            // 检查用户名是否已存在
            User existingUser = userDAO.findByUsername(username);
            if (existingUser != null) {
                return false;
            }
            
            // 创建新用户
            User user = new User();
            user.setUsername(username);
            // 加密密码
            user.setPassword(PasswordUtil.encryptPassword(password));
            user.setRealName(realName);
            user.setPhone(phone);
            user.setRoleID(roleID);
            user.setBuildingID(buildingID);
            user.setManagedBuildingID(managedBuildingID);
            user.setAccountStatus(accountStatus);
            user.setRoomNumber(roomNumber);
            
            return userDAO.addUser(user);
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
     * @param userID 用户ID
     * @param username 用户名
     * @param realName 真实姓名
     * @param phone 联系电话
     * @param roleID 角色ID
     * @param buildingID 所属楼栋ID（业主使用）
     * @param managedBuildingID 管理的楼栋ID（管家使用）
     * @param accountStatus 账号状态
     * @return 是否成功
     */
    public boolean updateUser(int userID, String username, String realName, 
                             String phone, int roleID, Integer buildingID, 
                             Integer managedBuildingID, int accountStatus) {
        return updateUser(userID, username, realName, phone, roleID, buildingID, managedBuildingID, accountStatus, null);
    }
    
    /**
     * 更新用户信息（包含房间号）
     * @param userID 用户ID
     * @param username 用户名
     * @param realName 真实姓名
     * @param phone 联系电话
     * @param roleID 角色ID
     * @param buildingID 所属楼栋ID（业主使用）
     * @param managedBuildingID 管理的楼栋ID（管家使用）
     * @param accountStatus 账号状态
     * @param roomNumber 房间号
     * @return 是否成功
     */
    public boolean updateUser(int userID, String username, String realName, 
                             String phone, int roleID, Integer buildingID, 
                             Integer managedBuildingID, int accountStatus, String roomNumber) {
        // 输入验证
        if (userID <= 0 || username == null || username.trim().isEmpty() || 
            realName == null || realName.trim().isEmpty() || 
            phone == null || phone.trim().isEmpty() || 
            roleID <= 0) {
            return false;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            
            // 检查用户是否存在
            User existingUser = userDAO.findByID(userID);
            if (existingUser == null) {
                return false;
            }
            
            // 检查用户名是否与其他用户重复
            User nameExistingUser = userDAO.findByUsername(username);
            if (nameExistingUser != null && nameExistingUser.getUserID() != userID) {
                return false;
            }
            
            // 更新用户信息
            existingUser.setUsername(username);
            existingUser.setRealName(realName);
            existingUser.setPhone(phone);
            existingUser.setRoleID(roleID);
            existingUser.setBuildingID(buildingID);
            existingUser.setManagedBuildingID(managedBuildingID);
            existingUser.setAccountStatus(accountStatus);
            existingUser.setRoomNumber(roomNumber);
            
            return userDAO.updateUser(existingUser);
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
     * 重置用户密码
     * @param userID 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    public boolean resetPassword(int userID, String newPassword) {
        // 输入验证
        if (userID <= 0 || newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            
            // 检查用户是否存在
            User existingUser = userDAO.findByID(userID);
            if (existingUser == null) {
                return false;
            }
            
            // 加密密码
            String encryptedPassword = PasswordUtil.encryptPassword(newPassword);
            
            return userDAO.updatePassword(userID, encryptedPassword);
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
     * 更新用户状态
     * @param userID 用户ID
     * @param status 状态（0-禁用，1-启用）
     * @return 是否成功
     */
    public boolean updateStatus(int userID, int status) {
        // 输入验证
        if (userID <= 0 || (status != 0 && status != 1)) {
            return false;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            
            // 检查用户是否存在
            User existingUser = userDAO.findByID(userID);
            if (existingUser == null) {
                return false;
            }
            
            // 管理员账号不允许禁用
            if (existingUser.getRoleID() == 1 && status == 0) {
                return false;
            }
            
            return userDAO.updateStatus(userID, status);
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
     * 删除用户
     * @param userID 用户ID
     * @return 是否成功
     */
    public boolean deleteUser(int userID) {
        // 输入验证
        if (userID <= 0) {
            return false;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            
            // 检查用户是否存在
            User existingUser = userDAO.findByID(userID);
            if (existingUser == null) {
                return false;
            }
            
            // 管理员账号不允许删除
            if (existingUser.getRoleID() == 1) {
                return false;
            }
            
            return userDAO.deleteUser(userID);
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
     * 根据ID获取用户
     * @param userID 用户ID
     * @return 用户对象
     */
    public User getUserByID(int userID) {
        if (userID <= 0) {
            return null;
        }
        
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
     * 搜索用户
     * @param keyword 搜索关键词（用户名、真实姓名或联系电话）
     * @return 匹配的用户列表
     */
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUsers();
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            List<User> allUsers = userDAO.findAll();
            List<User> searchResults = new ArrayList<>();
            
            String searchKeyword = keyword.trim().toLowerCase();
            
            for (User user : allUsers) {
                // 搜索用户名、真实姓名、联系电话
                if ((user.getUsername() != null && user.getUsername().toLowerCase().contains(searchKeyword)) ||
                    (user.getRealName() != null && user.getRealName().toLowerCase().contains(searchKeyword)) ||
                    (user.getPhone() != null && user.getPhone().toLowerCase().contains(searchKeyword))) {
                    searchResults.add(user);
                }
            }
            
            return searchResults;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
    
    /**
     * 获取所有角色列表
     * @return 角色列表
     */
    public List<Role> getAllRoles() {
        RoleDAO roleDAO = null;
        try {
            roleDAO = new RoleDAO();
            return roleDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (roleDAO != null) {
                roleDAO.close();
            }
        }
    }
    
    /**
     * 获取角色名称
     * @param roleID 角色ID
     * @return 角色名称
     */
    public String getRoleName(int roleID) {
        RoleDAO roleDAO = null;
        try {
            roleDAO = new RoleDAO();
            Role role = roleDAO.findByID(roleID);
            return role != null ? role.getRoleName() : "未知角色";
        } catch (SQLException e) {
            e.printStackTrace();
            return "未知角色";
        } finally {
            if (roleDAO != null) {
                roleDAO.close();
            }
        }
    }
}