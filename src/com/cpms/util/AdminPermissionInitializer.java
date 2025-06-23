package com.cpms.util;

import com.cpms.controller.admin.PermissionController;
import com.cpms.controller.admin.RolePermissionController;
import com.cpms.model.entity.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理员权限初始化工具类
 * 根据系统现有功能为管理员角色分配权限
 */
public class AdminPermissionInitializer {
    
    private PermissionController permissionController;
    private RolePermissionController rolePermissionController;
    
    public AdminPermissionInitializer() {
        this.permissionController = new PermissionController();
        this.rolePermissionController = new RolePermissionController();
    }
    
    /**
     * 初始化系统权限
     * 根据现有功能模块定义权限
     */
    public void initializeSystemPermissions() {
        List<Permission> permissions = getSystemPermissions();
        
        for (Permission permission : permissions) {
            try {
                permissionController.addPermission(
                    permission.getPermissionName(),
                    permission.getPermissionCode(),
                    permission.getPermissionDesc(),
                    permission.getPermissionType()
                );
                System.out.println("权限添加成功: " + permission.getPermissionName());
            } catch (Exception e) {
                System.out.println("权限添加失败: " + permission.getPermissionName() + ", 错误: " + e.getMessage());
            }
        }
    }
    
    /**
     * 为管理员角色分配所有权限
     * @param adminRoleID 管理员角色ID（通常为1）
     */
    public void assignAdminPermissions(int adminRoleID) {
        try {
            // 获取所有权限
            List<Permission> allPermissions = permissionController.getAllPermissions();
            List<Integer> permissionIDs = new ArrayList<>();
            
            for (Permission permission : allPermissions) {
                permissionIDs.add(permission.getPermissionID());
            }
            
            // 为管理员角色分配所有权限
            boolean success = rolePermissionController.assignPermissions(adminRoleID, permissionIDs);
            
            if (success) {
                System.out.println("管理员权限分配成功，共分配 " + permissionIDs.size() + " 个权限");
            } else {
                System.out.println("管理员权限分配失败");
            }
            
        } catch (Exception e) {
            System.out.println("管理员权限分配异常: " + e.getMessage());
        }
    }
    
    /**
     * 获取系统所有权限定义
     * 基于现有功能模块定义
     */
    private List<Permission> getSystemPermissions() {
        List<Permission> permissions = new ArrayList<>();
        
        // 系统管理权限
        permissions.add(new Permission(1001, "系统管理", "SYSTEM_MANAGE", "系统管理菜单访问权限", 1));
        permissions.add(new Permission(1002, "角色管理", "ROLE_MANAGE", "角色管理功能权限", 1));
        permissions.add(new Permission(1003, "角色权限分配", "ROLE_PERMISSION_ASSIGN", "角色权限分配功能权限", 1));
        permissions.add(new Permission(1004, "用户管理", "USER_MANAGE", "用户管理功能权限", 1));
        
        // 楼栋管理权限（管家分配功能暂未实现）
        permissions.add(new Permission(2001, "楼栋管理", "BUILDING_MANAGE", "楼栋管理菜单访问权限", 1));
        permissions.add(new Permission(2002, "楼栋信息管理", "BUILDING_INFO_MANAGE", "楼栋信息管理功能权限", 1));
        // permissions.add(new Permission(2003, "管家分配", "MANAGER_ASSIGN", "管家分配功能权限", 1));
        


        
        // 业主管理权限（物业管家功能）
        permissions.add(new Permission(4001, "业主管理", "OWNER_MANAGE", "业主管理菜单访问权限", 1));
        permissions.add(new Permission(4002, "业主录入", "OWNER_ENTRY", "业主录入功能权限", 2));
        permissions.add(new Permission(4003, "业主查询", "OWNER_QUERY", "业主查询功能权限", 2));
        permissions.add(new Permission(4004, "业主信息编辑", "OWNER_EDIT", "业主信息编辑权限", 2));
        permissions.add(new Permission(4005, "业主信息删除", "OWNER_DELETE", "业主信息删除权限", 2));
        
        // 访客管理权限
        permissions.add(new Permission(5001, "访客管理", "VISITOR_MANAGE", "访客管理菜单访问权限", 1));

        permissions.add(new Permission(5003, "访客同步", "VISITOR_SYNC", "访客同步功能权限", 2));
        permissions.add(new Permission(5004, "访客导出", "VISITOR_EXPORT", "访客导出功能权限", 2));
        
        // 报修管理权限
        permissions.add(new Permission(6001, "报修管理", "REPAIR_MANAGE", "报修管理菜单访问权限", 1));
        permissions.add(new Permission(6002, "报修处理", "REPAIR_PROCESS", "报修处理功能权限", 2));
        permissions.add(new Permission(6003, "报修查询", "REPAIR_QUERY", "报修查询功能权限", 2));
        permissions.add(new Permission(6004, "报修状态更新", "REPAIR_STATUS_UPDATE", "报修状态更新权限", 2));
        
        // 缴费管理权限
        permissions.add(new Permission(7001, "缴费管理", "PAYMENT_MANAGE", "缴费管理菜单访问权限", 1));
        permissions.add(new Permission(7002, "物业费管理", "PROPERTY_FEE_MANAGE", "物业费管理功能权限", 2));

        permissions.add(new Permission(7005, "收费记录查询", "PAYMENT_RECORD_QUERY", "收费记录查询权限", 2));
        
        // 车位管理权限
        permissions.add(new Permission(8001, "车位管理", "PARKING_MANAGE", "车位管理菜单访问权限", 1));
        permissions.add(new Permission(8002, "车位信息管理", "PARKING_INFO_MANAGE", "车位信息管理功能权限", 2));
        permissions.add(new Permission(8003, "车位分配", "PARKING_ASSIGN", "车位分配功能权限", 2));
        permissions.add(new Permission(8004, "车牌号修改", "LICENSE_PLATE_EDIT", "车牌号修改权限（仅管家和管理员）", 2));
        permissions.add(new Permission(8005, "车位解除分配", "PARKING_UNASSIGN", "车位解除分配权限", 2));
        
        // 公告管理权限（暂未实现，注释掉）
        // permissions.add(new Permission(9001, "公告管理", "ANNOUNCEMENT_MANAGE", "公告管理功能权限", 1));
        // permissions.add(new Permission(9002, "公告发布", "ANNOUNCEMENT_PUBLISH", "公告发布权限", 2));
        // permissions.add(new Permission(9003, "公告编辑", "ANNOUNCEMENT_EDIT", "公告编辑权限", 2));
        // permissions.add(new Permission(9004, "公告删除", "ANNOUNCEMENT_DELETE", "公告删除权限", 2));
        
        // 业主个人功能权限
        permissions.add(new Permission(9001, "提交报修", "REPAIR_SUBMIT", "业主提交报修权限", 2));
        permissions.add(new Permission(9002, "查询个人报修", "REPAIR_QUERY_OWN", "业主查询自己的报修记录权限", 2));
        permissions.add(new Permission(9003, "查询个人缴费", "PAYMENT_QUERY_OWN", "业主查询自己的缴费账单权限", 2));
        permissions.add(new Permission(9004, "查询个人缴费记录", "PAYMENT_HISTORY_OWN", "业主查询自己的缴费记录权限", 2));
        permissions.add(new Permission(9005, "查看个人车位", "PARKING_VIEW_OWN", "业主查看自己的车位信息权限", 2));
        permissions.add(new Permission(9006, "查看个人信息", "PROFILE_VIEW", "业主查看个人信息权限", 2));
        permissions.add(new Permission(9007, "修改密码", "PASSWORD_CHANGE", "用户修改密码权限", 2));
        
        // 系统操作权限
        permissions.add(new Permission(10001, "系统退出", "SYSTEM_EXIT", "系统退出权限", 2));
        
        return permissions;
    }
    
    /**
     * 为业主角色分配权限
     * @param ownerRoleID 业主角色ID（通常为3）
     */
    public void assignOwnerPermissions(int ownerRoleID) {
        try {
            // 获取业主相关的权限ID列表
            List<Integer> ownerPermissionIDs = new ArrayList<>();
            
            // 业主个人功能权限
            ownerPermissionIDs.add(9001); // 提交报修
            ownerPermissionIDs.add(9002); // 查询个人报修
            ownerPermissionIDs.add(9003); // 查询个人缴费
            ownerPermissionIDs.add(9004); // 查询个人缴费记录
            ownerPermissionIDs.add(9005); // 查看个人车位
            ownerPermissionIDs.add(9006); // 查看个人信息
            ownerPermissionIDs.add(9007); // 修改密码
            ownerPermissionIDs.add(10001); // 系统退出
            
            // 为业主角色分配权限
            boolean success = rolePermissionController.assignPermissions(ownerRoleID, ownerPermissionIDs);
            
            if (success) {
                System.out.println("业主权限分配成功，共分配 " + ownerPermissionIDs.size() + " 个权限");
            } else {
                System.out.println("业主权限分配失败");
            }
            
        } catch (Exception e) {
            System.out.println("业主权限分配异常: " + e.getMessage());
        }
    }
    
    /**
     * 主方法，用于执行权限初始化
     */
    public static void main(String[] args) {
        AdminPermissionInitializer initializer = new AdminPermissionInitializer();
        
        System.out.println("开始初始化系统权限...");
        initializer.initializeSystemPermissions();
        
        System.out.println("\n开始为管理员角色分配权限...");
        initializer.assignAdminPermissions(1); // 假设管理员角色ID为1
        
        System.out.println("\n开始为业主角色分配权限...");
        initializer.assignOwnerPermissions(3); // 业主角色ID为3
        
        System.out.println("\n权限初始化完成！");
    }
}