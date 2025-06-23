package com.cpms.controller.manager;

import com.cpms.model.dao.BuildingDAO;
import com.cpms.model.dao.UserDAO;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.User;
import com.cpms.util.security.PasswordUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 业主管理控制器
 * 处理业主相关的业务逻辑
 */
public class OwnerController {
    
    /**
     * 添加业主
     * @param username 用户名
     * @param password 密码
     * @param realName 真实姓名
     * @param phone 电话
     * @param buildingID 所属楼栋ID
     * @param roomNumber 房间号
     * @return 是否成功
     */
    public boolean addOwner(String username, String password, String realName, String phone, int buildingID, String roomNumber) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty() ||
            realName == null || realName.trim().isEmpty() ||
            buildingID <= 0) {
            return false;
        }
        
        UserDAO userDAO = null;
        BuildingDAO buildingDAO = null;
        try {
            userDAO = new UserDAO();
            buildingDAO = new BuildingDAO();
            
            // 检查用户名是否已存在
            User existingUser = userDAO.findByUsername(username);
            if (existingUser != null) {
                return false;
            }
            
            // 检查楼栋是否存在
            Building building = buildingDAO.findByID(buildingID);
            if (building == null) {
                return false;
            }
            
            // 创建业主用户
            User owner = new User();
            owner.setUsername(username);
            // 加密密码
            owner.setPassword(PasswordUtil.encryptPassword(password));
            owner.setRealName(realName);
            owner.setPhone(phone);
            owner.setRoleID(3); // 业主角色ID为3
            owner.setBuildingID(buildingID);
            owner.setRoomNumber(roomNumber); // 设置房间号
            owner.setAccountStatus(1); // 启用状态
            
            return userDAO.addUser(owner);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
            if (buildingDAO != null) {
                buildingDAO.close();
            }
        }
    }
    
    /**
     * 添加业主（兼容旧版本，不包含房间号）
     * @param username 用户名
     * @param password 密码
     * @param realName 真实姓名
     * @param phone 电话
     * @param buildingID 所属楼栋ID
     * @return 是否成功
     */
    public boolean addOwner(String username, String password, String realName, String phone, int buildingID) {
        return addOwner(username, password, realName, phone, buildingID, null);
    }
    
    /**
     * 更新业主信息
     * @param userID 用户ID
     * @param realName 真实姓名
     * @param phone 电话
     * @param roomNumber 房间号
     * @param buildingID 所属楼栋ID
     * @param accountStatus 账号状态
     * @return 是否成功
     */
    public boolean updateOwner(int userID, String realName, String phone, String roomNumber, int buildingID, int accountStatus) {
        return updateOwner(userID, realName, phone, buildingID, accountStatus, roomNumber);
    }
    
    /**
     * 更新业主信息（兼容旧版本）
     * @param userID 用户ID
     * @param realName 真实姓名
     * @param phone 电话
     * @param buildingID 所属楼栋ID
     * @param accountStatus 账号状态
     * @return 是否成功
     */
    public boolean updateOwner(int userID, String realName, String phone, int buildingID, int accountStatus) {
        return updateOwner(userID, realName, phone, buildingID, accountStatus, null);
    }
    
    /**
     * 更新业主信息（内部方法）
     * @param userID 用户ID
     * @param realName 真实姓名
     * @param phone 电话
     * @param buildingID 所属楼栋ID
     * @param accountStatus 账号状态
     * @param roomNumber 房间号
     * @return 是否成功
     */
    private boolean updateOwner(int userID, String realName, String phone, int buildingID, int accountStatus, String roomNumber) {
        if (userID <= 0 || realName == null || realName.trim().isEmpty() || buildingID <= 0) {
            return false;
        }
        
        UserDAO userDAO = null;
        BuildingDAO buildingDAO = null;
        try {
            userDAO = new UserDAO();
            buildingDAO = new BuildingDAO();
            
            // 检查用户是否存在
            User existingUser = userDAO.findByID(userID);
            if (existingUser == null || existingUser.getRoleID() != 3) {
                return false;
            }
            
            // 检查楼栋是否存在
            Building building = buildingDAO.findByID(buildingID);
            if (building == null) {
                return false;
            }
            
            // 更新业主信息
            existingUser.setRealName(realName);
            existingUser.setPhone(phone);
            existingUser.setBuildingID(buildingID);
            existingUser.setAccountStatus(accountStatus);
            if (roomNumber != null && !roomNumber.trim().isEmpty()) {
                existingUser.setRoomNumber(roomNumber.trim());
            }
            
            return userDAO.updateUser(existingUser);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
            if (buildingDAO != null) {
                buildingDAO.close();
            }
        }
    }
    
    /**
     * 重置业主密码
     * @param userID 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    public boolean resetOwnerPassword(int userID, String newPassword) {
        if (userID <= 0 || newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            
            // 检查用户是否存在
            User existingUser = userDAO.findByID(userID);
            if (existingUser == null || existingUser.getRoleID() != 3) {
                return false;
            }
            
            // 加密密码
            String encryptedPassword = PasswordUtil.encryptPassword(newPassword);
            
            // 更新密码
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
     * 删除业主
     * @param userID 用户ID
     * @return 是否成功
     */
    public boolean deleteOwner(int userID) {
        if (userID <= 0) {
            return false;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            
            // 检查用户是否存在
            User existingUser = userDAO.findByID(userID);
            if (existingUser == null || existingUser.getRoleID() != 3) {
                return false;
            }
            
            // 删除用户
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
     * 获取所有业主
     * @return 业主列表
     */
    public List<User> getAllOwners() {
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            return userDAO.findByRoleID(3); // 业主角色ID为3
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
     * 根据楼栋ID获取业主列表
     * @param buildingID 楼栋ID
     * @return 业主列表
     */
    public List<User> getOwnersByBuildingID(int buildingID) {
        if (buildingID <= 0) {
            return new ArrayList<>();
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            return userDAO.findOwnersByBuildingID(buildingID);
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
     * 根据ID获取业主
     * @param userID 用户ID
     * @return 业主对象
     */
    public User getOwnerByID(int userID) {
        if (userID <= 0) {
            return null;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            User user = userDAO.findByID(userID);
            if (user != null && user.getRoleID() == 3) {
                return user;
            }
            return null;
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
     * 根据用户名查找业主
     * @param username 用户名
     * @return 业主对象
     */
    public User getOwnerByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            User user = userDAO.findByUsername(username);
            if (user != null && user.getRoleID() == 3) {
                return user;
            }
            return null;
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
     * 启用业主账号
     * @param userID 用户ID
     * @return 是否成功
     */
    public boolean enableOwner(int userID) {
        if (userID <= 0) {
            return false;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            
            // 检查用户是否存在
            User existingUser = userDAO.findByID(userID);
            if (existingUser == null || existingUser.getRoleID() != 3) {
                return false;
            }
            
            // 启用账号
            return userDAO.updateStatus(userID, 1);
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
     * 禁用业主账号
     * @param userID 用户ID
     * @return 是否成功
     */
    public boolean disableOwner(int userID) {
        if (userID <= 0) {
            return false;
        }
        
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            
            // 检查用户是否存在
            User existingUser = userDAO.findByID(userID);
            if (existingUser == null || existingUser.getRoleID() != 3) {
                return false;
            }
            
            // 禁用账号
            return userDAO.updateStatus(userID, 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
}