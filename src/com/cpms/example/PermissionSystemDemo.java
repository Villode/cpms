package com.cpms.example;

import com.cpms.model.entity.User;
import com.cpms.util.AdminPermissionInitializer;
import com.cpms.util.PermissionValidator;

/**
 * 权限系统演示程序
 * 展示如何使用权限验证功能
 */
public class PermissionSystemDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 小区物业管理系统 - 权限系统演示 ===");
        
        // 1. 初始化权限系统
        System.out.println("\n1. 初始化权限系统...");
        AdminPermissionInitializer initializer = new AdminPermissionInitializer();
        initializer.initializeSystemPermissions();
        initializer.assignAdminPermissions(1);
        
        // 2. 创建测试用户
        User admin = createTestUser("admin", "系统管理员", "管理员");
        admin.setUserID(1);
        admin.setRoleID(1); // 管理员角色ID
        
        User manager = createTestUser("manager", "张管家", "物业管家");
        manager.setUserID(2);
        manager.setRoleID(2); // 物业管家角色ID
        
        User owner = createTestUser("owner", "李业主", "业主");
        owner.setUserID(3);
        owner.setRoleID(3); // 业主角色ID
        
        System.out.println("\n3. 测试用户权限验证...");
        
        // 3. 测试管理员权限
        System.out.println("\n--- 管理员权限测试 ---");
        testUserPermissions(admin, "管理员用户 (角色ID: " + admin.getRoleID() + ")");
        
        // 4. 测试物业管家权限
        System.out.println("\n--- 物业管家权限测试 ---");
        testUserPermissions(manager, "物业管家用户 (角色ID: " + manager.getRoleID() + ")");
        
        // 5. 测试业主权限
        System.out.println("\n--- 业主权限测试 ---");
        testUserPermissions(owner, "业主用户 (角色ID: " + owner.getRoleID() + ")");
        
        System.out.println("\n=== 权限系统演示完成 ===");
    }
    
    /**
     * 创建测试用户
     */
    private static User createTestUser(String username, String realName, String role) {
        User user = new User();
        user.setUsername(username);
        user.setRealName(realName);
        // 根据角色名称设置角色ID
        if ("管理员".equals(role)) {
            user.setRoleID(1);
        } else if ("物业管家".equals(role)) {
            user.setRoleID(2);
        } else if ("业主".equals(role)) {
            user.setRoleID(3);
        }
        return user;
    }
    
    /**
     * 测试用户权限
     */
    private static void testUserPermissions(User user, String userDesc) {
        System.out.println("用户: " + user.getRealName() + " (" + userDesc + ")");
        
        // 测试基本角色判断
        System.out.println("  是否为管理员: " + PermissionValidator.isAdmin(user));
        System.out.println("  是否为物业管家: " + PermissionValidator.isPropertyManager(user));
        System.out.println("  是否为业主: " + PermissionValidator.isOwner(user));
        
        // 测试功能权限
        System.out.println("  系统管理权限: " + PermissionValidator.canManageSystem(user));
        System.out.println("  业主管理权限: " + PermissionValidator.canManageOwner(user));
        System.out.println("  车位管理权限: " + PermissionValidator.canManageParking(user));
        System.out.println("  报修管理权限: " + PermissionValidator.canManageRepair(user));
        System.out.println("  缴费管理权限: " + PermissionValidator.canManagePayment(user));
        System.out.println("  车牌号修改权限: " + PermissionValidator.canEditLicensePlate(user));
        
        // 测试具体权限代码
        System.out.println("  角色管理权限: " + PermissionValidator.hasPermission(user, "ROLE_MANAGE"));
        System.out.println("  用户管理权限: " + PermissionValidator.hasPermission(user, "USER_MANAGE"));
        System.out.println("  楼栋管理权限: " + PermissionValidator.hasPermission(user, "BUILDING_MANAGE"));
    }
    
    /**
     * 演示权限验证在实际功能中的使用
     */
    public static void demonstratePermissionUsage() {
        // 模拟当前登录用户
        User currentUser = createTestUser("owner", "张三", "业主");
        
        // 在车牌号修改功能中使用权限验证
        if (PermissionValidator.canEditLicensePlate(currentUser)) {
            System.out.println("允许修改车牌号");
            // 执行车牌号修改逻辑
        } else {
            PermissionValidator.showPermissionDeniedDialog("车牌号修改");
            return;
        }
        
        // 在业主管理功能中使用权限验证
        if (PermissionValidator.canManageOwner(currentUser)) {
            System.out.println("允许管理业主信息");
            // 执行业主管理逻辑
        } else {
            PermissionValidator.showPermissionDeniedDialog("业主管理");
            return;
        }
        
        // 在系统管理功能中使用权限验证
        if (PermissionValidator.canManageSystem(currentUser)) {
            System.out.println("允许访问系统管理");
            // 执行系统管理逻辑
        } else {
            PermissionValidator.showPermissionDeniedDialog("系统管理");
            return;
        }
    }
}