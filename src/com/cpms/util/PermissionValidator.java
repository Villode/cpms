package com.cpms.util;

import com.cpms.controller.admin.PermissionController;
import com.cpms.model.entity.User;

/**
 * 权限验证工具类
 * 用于验证用户是否具有特定功能的访问权限
 */
public class PermissionValidator {
    
    private PermissionController permissionController;
    
    public PermissionValidator() {
        this.permissionController = new PermissionController();
    }
    
    /**
     * 验证用户是否具有指定权限
     * @param user 用户对象
     * @param permissionCode 权限代码
     * @return 是否具有权限
     */
    public static boolean hasPermission(User user, String permissionCode) {
        if (user == null || permissionCode == null) {
            return false;
        }
        
        int roleID = user.getRoleID();
        if (roleID <= 0) {
            return false;
        }
        
        try {
            // 管理员拥有所有权限
            if (roleID == 1) {
                return true;
            }
            
            PermissionController permissionController = new PermissionController();
            return permissionController.hasPermission(roleID, permissionCode);
            
        } catch (Exception e) {
            System.out.println("权限验证异常: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 验证用户是否可以访问指定菜单
     * @param user 用户对象
     * @param menuCode 菜单权限代码
     * @return 是否可以访问
     */
    public static boolean canAccessMenu(User user, String menuCode) {
        return hasPermission(user, menuCode) || user.getRoleID() == 1;
    }
    
    /**
     * 验证用户是否可以使用指定按钮
     * @param user 用户对象
     * @param buttonCode 按钮权限代码
     * @return 是否可以使用
     */
    public static boolean canUseButton(User user, String buttonCode) {
        return hasPermission(user, buttonCode) || user.getRoleID() == 1;
    }
    
    /**
     * 验证用户是否为管理员
     * @param user 用户对象
     * @return 是否为管理员
     */
    public static boolean isAdmin(User user) {
        return user != null && user.getRoleID() == 1;
    }
    
    /**
     * 验证用户是否为物业管家
     * @param user 用户对象
     * @return 是否为物业管家
     */
    public static boolean isPropertyManager(User user) {
        return user != null && user.getRoleID() == 2;
    }
    
    /**
     * 验证用户是否为业主
     * @param user 用户对象
     * @return 是否为业主
     */
    public static boolean isOwner(User user) {
        return user != null && user.getRoleID() == 3;
    }
    
    /**
     * 验证用户是否具有车牌号修改权限
     * 只有管理员和物业管家可以修改车牌号
     * @param user 用户对象
     * @return 是否具有车牌号修改权限
     */
    public static boolean canEditLicensePlate(User user) {
        return isAdmin(user) || isPropertyManager(user);
    }
    
    /**
     * 验证用户是否具有业主信息管理权限
     * @param user 用户对象
     * @return 是否具有业主信息管理权限
     */
    public static boolean canManageOwner(User user) {
        return hasPermission(user, "OWNER_MANAGE") || isAdmin(user);
    }
    
    /**
     * 验证用户是否具有车位管理权限
     * @param user 用户对象
     * @return 是否具有车位管理权限
     */
    public static boolean canManageParking(User user) {
        return hasPermission(user, "PARKING_MANAGE") || isAdmin(user);
    }
    
    /**
     * 验证用户是否具有报修管理权限
     * @param user 用户对象
     * @return 是否具有报修管理权限
     */
    public static boolean canManageRepair(User user) {
        return hasPermission(user, "REPAIR_MANAGE") || isAdmin(user);
    }
    
    /**
     * 验证用户是否具有缴费管理权限
     * @param user 用户对象
     * @return 是否具有缴费管理权限
     */
    public static boolean canManagePayment(User user) {
        return hasPermission(user, "PAYMENT_MANAGE") || isAdmin(user);
    }
    
    /**
     * 验证用户是否具有系统管理权限
     * @param user 用户对象
     * @return 是否具有系统管理权限
     */
    public static boolean canManageSystem(User user) {
        return hasPermission(user, "SYSTEM_MANAGE") || isAdmin(user);
    }
    
    /**
     * 根据角色名称获取角色ID
     * @param roleName 角色名称
     * @return 角色ID
     */
    private int getRoleID(String roleName) {
        switch (roleName) {
            case "管理员":
                return 1;
            case "物业管家":
                return 2;
            case "业主":
                return 3;
            default:
                return 0;
        }
    }
    
    /**
     * 获取权限错误提示信息
     * @param permissionName 权限名称
     * @return 错误提示信息
     */
    public static String getPermissionDeniedMessage(String permissionName) {
        return "您没有" + permissionName + "的权限，请联系管理员！";
    }
    
    /**
     * 显示权限不足的提示对话框
     * @param permissionName 权限名称
     */
    public static void showPermissionDeniedDialog(String permissionName) {
        javax.swing.JOptionPane.showMessageDialog(
            null, 
            getPermissionDeniedMessage(permissionName), 
            "权限不足", 
            javax.swing.JOptionPane.WARNING_MESSAGE
        );
    }
}