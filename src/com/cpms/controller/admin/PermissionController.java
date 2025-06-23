package com.cpms.controller.admin;

import com.cpms.model.dao.PermissionDAO;
import com.cpms.model.dao.RolePermissionDAO;
import com.cpms.model.entity.Permission;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理控制器
 * 处理权限相关的业务逻辑
 */
public class PermissionController {
    
    /**
     * 获取所有权限列表
     * @return 权限列表
     */
    public List<Permission> getAllPermissions() {
        PermissionDAO permissionDAO = null;
        try {
            permissionDAO = new PermissionDAO();
            return permissionDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (permissionDAO != null) {
                permissionDAO.close();
            }
        }
    }
    
    /**
     * 根据权限类型获取权限列表
     * @param permissionType 权限类型
     * @return 权限列表
     */
    public List<Permission> getPermissionsByType(int permissionType) {
        PermissionDAO permissionDAO = null;
        try {
            permissionDAO = new PermissionDAO();
            return permissionDAO.findByType(permissionType);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (permissionDAO != null) {
                permissionDAO.close();
            }
        }
    }
    
    /**
     * 添加权限
     * @param permissionName 权限名称
     * @param permissionCode 权限代码
     * @param permissionDesc 权限描述
     * @param permissionType 权限类型
     * @return 是否成功
     */
    public boolean addPermission(String permissionName, String permissionCode, String permissionDesc, int permissionType) {
        if (permissionName == null || permissionName.trim().isEmpty() || 
            permissionCode == null || permissionCode.trim().isEmpty()) {
            return false;
        }
        
        PermissionDAO permissionDAO = null;
        try {
            permissionDAO = new PermissionDAO();
            
            // 检查权限代码是否已存在
            Permission existingPermission = permissionDAO.findByCode(permissionCode);
            if (existingPermission != null) {
                return false;
            }
            
            // 创建新权限
            Permission permission = new Permission();
            permission.setPermissionName(permissionName);
            permission.setPermissionCode(permissionCode);
            permission.setPermissionDesc(permissionDesc);
            permission.setPermissionType(permissionType);
            
            return permissionDAO.addPermission(permission);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (permissionDAO != null) {
                permissionDAO.close();
            }
        }
    }
    
    /**
     * 更新权限
     * @param permissionID 权限ID
     * @param permissionName 权限名称
     * @param permissionCode 权限代码
     * @param permissionDesc 权限描述
     * @param permissionType 权限类型
     * @return 是否成功
     */
    public boolean updatePermission(int permissionID, String permissionName, String permissionCode, String permissionDesc, int permissionType) {
        if (permissionID <= 0 || permissionName == null || permissionName.trim().isEmpty() || 
            permissionCode == null || permissionCode.trim().isEmpty()) {
            return false;
        }
        
        PermissionDAO permissionDAO = null;
        try {
            permissionDAO = new PermissionDAO();
            
            // 检查权限是否存在
            Permission existingPermission = permissionDAO.findByID(permissionID);
            if (existingPermission == null) {
                return false;
            }
            
            // 检查权限代码是否与其他权限重复
            Permission codeExistingPermission = permissionDAO.findByCode(permissionCode);
            if (codeExistingPermission != null && codeExistingPermission.getPermissionID() != permissionID) {
                return false;
            }
            
            // 更新权限
            existingPermission.setPermissionName(permissionName);
            existingPermission.setPermissionCode(permissionCode);
            existingPermission.setPermissionDesc(permissionDesc);
            existingPermission.setPermissionType(permissionType);
            
            return permissionDAO.updatePermission(existingPermission);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (permissionDAO != null) {
                permissionDAO.close();
            }
        }
    }
    
    /**
     * 删除权限
     * @param permissionID 权限ID
     * @return 是否成功
     */
    public boolean deletePermission(int permissionID) {
        if (permissionID <= 0) {
            return false;
        }
        
        // 系统内置权限不允许删除
        if (permissionID <= 10) {
            return false;
        }
        
        PermissionDAO permissionDAO = null;
        RolePermissionDAO rolePermissionDAO = null;
        try {
            permissionDAO = new PermissionDAO();
            rolePermissionDAO = new RolePermissionDAO();
            
            // 先删除角色权限关联
            rolePermissionDAO.deleteRolePermissionsByPermissionID(permissionID);
            
            // 再删除权限
            return permissionDAO.deletePermission(permissionID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (permissionDAO != null) {
                permissionDAO.close();
            }
            if (rolePermissionDAO != null) {
                rolePermissionDAO.close();
            }
        }
    }
    
    /**
     * 根据ID获取权限
     * @param permissionID 权限ID
     * @return 权限对象
     */
    public Permission getPermissionByID(int permissionID) {
        if (permissionID <= 0) {
            return null;
        }
        
        PermissionDAO permissionDAO = null;
        try {
            permissionDAO = new PermissionDAO();
            return permissionDAO.findByID(permissionID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (permissionDAO != null) {
                permissionDAO.close();
            }
        }
    }
    
    /**
     * 获取角色的所有权限
     * @param roleID 角色ID
     * @return 权限列表
     */
    public List<Permission> getPermissionsByRoleID(int roleID) {
        if (roleID <= 0) {
            return new ArrayList<>();
        }
        
        RolePermissionDAO rolePermissionDAO = null;
        try {
            rolePermissionDAO = new RolePermissionDAO();
            return rolePermissionDAO.findPermissionsByRoleID(roleID);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (rolePermissionDAO != null) {
                rolePermissionDAO.close();
            }
        }
    }
    
    /**
     * 获取角色的所有权限ID
     * @param roleID 角色ID
     * @return 权限ID列表
     */
    public List<Integer> getPermissionIDsByRoleID(int roleID) {
        if (roleID <= 0) {
            return new ArrayList<>();
        }
        
        RolePermissionDAO rolePermissionDAO = null;
        try {
            rolePermissionDAO = new RolePermissionDAO();
            return rolePermissionDAO.findPermissionIDsByRoleID(roleID);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (rolePermissionDAO != null) {
                rolePermissionDAO.close();
            }
        }
    }
    
    /**
     * 验证角色是否拥有指定权限
     * @param roleID 角色ID
     * @param permissionID 权限ID
     * @return 是否拥有权限
     */
    public boolean hasPermission(int roleID, int permissionID) {
        if (roleID <= 0 || permissionID <= 0) {
            return false;
        }
        
        RolePermissionDAO rolePermissionDAO = null;
        try {
            rolePermissionDAO = new RolePermissionDAO();
            return rolePermissionDAO.hasPermission(roleID, permissionID);
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
     * 验证角色是否拥有指定权限（根据权限代码）
     * @param roleID 角色ID
     * @param permissionCode 权限代码
     * @return 是否拥有权限
     */
    public boolean hasPermission(int roleID, String permissionCode) {
        if (roleID <= 0 || permissionCode == null || permissionCode.trim().isEmpty()) {
            return false;
        }
        
        PermissionDAO permissionDAO = null;
        RolePermissionDAO rolePermissionDAO = null;
        try {
            permissionDAO = new PermissionDAO();
            rolePermissionDAO = new RolePermissionDAO();
            
            // 根据权限代码查找权限
            Permission permission = permissionDAO.findByCode(permissionCode);
            if (permission == null) {
                return false;
            }
            
            // 验证角色是否拥有该权限
            return rolePermissionDAO.hasPermission(roleID, permission.getPermissionID());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (permissionDAO != null) {
                permissionDAO.close();
            }
            if (rolePermissionDAO != null) {
                rolePermissionDAO.close();
            }
        }
    }
}