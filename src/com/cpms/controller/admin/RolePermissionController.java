package com.cpms.controller.admin;

import com.cpms.model.dao.RoleDAO;
import com.cpms.model.dao.RolePermissionDAO;
import com.cpms.model.entity.Role;

import java.sql.SQLException;
import java.util.List;

/**
 * 角色权限分配控制器
 * 处理角色权限分配相关的业务逻辑
 */
public class RolePermissionController {
    
    /**
     * 分配角色权限
     * @param roleID 角色ID
     * @param permissionIDs 权限ID列表
     * @return 是否成功
     */
    public boolean assignPermissions(int roleID, List<Integer> permissionIDs) {
        if (roleID <= 0 || permissionIDs == null) {
            return false;
        }
        
        // 验证角色是否存在
        RoleDAO roleDAO = null;
        try {
            roleDAO = new RoleDAO();
            Role role = roleDAO.findByID(roleID);
            if (role == null) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (roleDAO != null) {
                roleDAO.close();
            }
        }
        
        // 分配权限
        RolePermissionDAO rolePermissionDAO = null;
        try {
            rolePermissionDAO = new RolePermissionDAO();
            return rolePermissionDAO.batchAddRolePermissions(roleID, permissionIDs);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rolePermissionDAO != null) {
                rolePermissionDAO.close();
            }
        }
    }
    
    /**
     * 移除角色所有权限
     * @param roleID 角色ID
     * @return 是否成功
     */
    public boolean removeAllPermissions(int roleID) {
        if (roleID <= 0) {
            return false;
        }
        
        // 验证角色是否存在
        RoleDAO roleDAO = null;
        try {
            roleDAO = new RoleDAO();
            Role role = roleDAO.findByID(roleID);
            if (role == null) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (roleDAO != null) {
                roleDAO.close();
            }
        }
        
        // 移除权限
        RolePermissionDAO rolePermissionDAO = null;
        try {
            rolePermissionDAO = new RolePermissionDAO();
            return rolePermissionDAO.deleteRolePermissionsByRoleID(roleID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rolePermissionDAO != null) {
                rolePermissionDAO.close();
            }
        }
    }
    
    /**
     * 移除角色指定权限
     * @param roleID 角色ID
     * @param permissionID 权限ID
     * @return 是否成功
     */
    public boolean removePermission(int roleID, int permissionID) {
        if (roleID <= 0 || permissionID <= 0) {
            return false;
        }
        
        RolePermissionDAO rolePermissionDAO = null;
        try {
            rolePermissionDAO = new RolePermissionDAO();
            return rolePermissionDAO.deleteRolePermission(roleID, permissionID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rolePermissionDAO != null) {
                rolePermissionDAO.close();
            }
        }
    }
} 