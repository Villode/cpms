package com.cpms.controller.admin;

import com.cpms.model.dao.RoleDAO;
import com.cpms.model.entity.Role;

import java.sql.SQLException;
import java.util.List;

/**
 * 角色管理控制器
 * 处理角色相关的业务逻辑
 */
public class RoleController {
    
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
            return null;
        } finally {
            if (roleDAO != null) {
                roleDAO.close();
            }
        }
    }
    
    /**
     * 添加角色
     * @param roleName 角色名称
     * @param roleDesc 角色描述
     * @return 是否成功
     */
    public boolean addRole(String roleName, String roleDesc) {
        if (roleName == null || roleName.trim().isEmpty()) {
            return false;
        }
        
        RoleDAO roleDAO = null;
        try {
            roleDAO = new RoleDAO();
            
            // 检查角色名是否已存在
            Role existingRole = roleDAO.findByName(roleName);
            if (existingRole != null) {
                return false;
            }
            
            // 创建新角色
            Role role = new Role();
            role.setRoleName(roleName);
            role.setRoleDesc(roleDesc);
            
            return roleDAO.addRole(role);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (roleDAO != null) {
                roleDAO.close();
            }
        }
    }
    
    /**
     * 更新角色
     * @param roleID 角色ID
     * @param roleName 角色名称
     * @param roleDesc 角色描述
     * @return 是否成功
     */
    public boolean updateRole(int roleID, String roleName, String roleDesc) {
        if (roleID <= 0 || roleName == null || roleName.trim().isEmpty()) {
            return false;
        }
        
        RoleDAO roleDAO = null;
        try {
            roleDAO = new RoleDAO();
            
            // 检查角色是否存在
            Role existingRole = roleDAO.findByID(roleID);
            if (existingRole == null) {
                return false;
            }
            
            // 检查角色名是否与其他角色重复
            Role nameExistingRole = roleDAO.findByName(roleName);
            if (nameExistingRole != null && nameExistingRole.getRoleID() != roleID) {
                return false;
            }
            
            // 更新角色
            existingRole.setRoleName(roleName);
            existingRole.setRoleDesc(roleDesc);
            
            return roleDAO.updateRole(existingRole);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (roleDAO != null) {
                roleDAO.close();
            }
        }
    }
    
    /**
     * 删除角色
     * @param roleID 角色ID
     * @return 是否成功
     */
    public boolean deleteRole(int roleID) {
        if (roleID <= 0) {
            return false;
        }
        
        // 系统内置角色不允许删除
        if (roleID <= 3) {
            return false;
        }
        
        RoleDAO roleDAO = null;
        try {
            roleDAO = new RoleDAO();
            return roleDAO.deleteRole(roleID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (roleDAO != null) {
                roleDAO.close();
            }
        }
    }
    
    /**
     * 根据ID获取角色
     * @param roleID 角色ID
     * @return 角色对象
     */
    public Role getRoleByID(int roleID) {
        if (roleID <= 0) {
            return null;
        }
        
        RoleDAO roleDAO = null;
        try {
            roleDAO = new RoleDAO();
            return roleDAO.findByID(roleID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (roleDAO != null) {
                roleDAO.close();
            }
        }
    }
} 