package com.cpms.model.entity;

import java.util.Date;

/**
 * 角色权限关联实体类
 * 对应数据库中的RolePermission表
 */
public class RolePermission {
    private int id;             // 主键ID
    private int roleID;         // 角色ID
    private int permissionID;   // 权限ID
    private Date createTime;    // 创建时间
    
    /**
     * 默认构造方法
     */
    public RolePermission() {
    }
    
    /**
     * 带参数的构造方法
     * @param roleID 角色ID
     * @param permissionID 权限ID
     */
    public RolePermission(int roleID, int permissionID) {
        this.roleID = roleID;
        this.permissionID = permissionID;
    }
    
    /**
     * 获取主键ID
     * @return int 主键ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * 设置主键ID
     * @param id 主键ID
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * 获取角色ID
     * @return int 角色ID
     */
    public int getRoleID() {
        return roleID;
    }
    
    /**
     * 设置角色ID
     * @param roleID 角色ID
     */
    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
    
    /**
     * 获取权限ID
     * @return int 权限ID
     */
    public int getPermissionID() {
        return permissionID;
    }
    
    /**
     * 设置权限ID
     * @param permissionID 权限ID
     */
    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }
    
    /**
     * 获取创建时间
     * @return Date 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }
    
    /**
     * 设置创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString() {
        return "RolePermission{" +
                "id=" + id +
                ", roleID=" + roleID +
                ", permissionID=" + permissionID +
                ", createTime=" + createTime +
                '}';
    }
} 