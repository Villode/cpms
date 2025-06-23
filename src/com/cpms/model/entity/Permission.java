package com.cpms.model.entity;

import java.util.Date;

/**
 * 权限实体类
 * 对应数据库中的Permission表
 */
public class Permission {
    private int permissionID;       // 权限ID
    private String permissionName;  // 权限名称
    private String permissionCode;  // 权限代码
    private String permissionDesc;  // 权限描述
    private int permissionType;     // 权限类型（1-菜单，2-按钮，3-接口）
    private Date createTime;        // 创建时间
    private Date updateTime;        // 更新时间
    
    /**
     * 默认构造方法
     */
    public Permission() {
    }
    
    /**
     * 带参数的构造方法
     * @param permissionID 权限ID
     * @param permissionName 权限名称
     * @param permissionCode 权限代码
     * @param permissionDesc 权限描述
     * @param permissionType 权限类型
     */
    public Permission(int permissionID, String permissionName, String permissionCode, String permissionDesc, int permissionType) {
        this.permissionID = permissionID;
        this.permissionName = permissionName;
        this.permissionCode = permissionCode;
        this.permissionDesc = permissionDesc;
        this.permissionType = permissionType;
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
     * 获取权限名称
     * @return String 权限名称
     */
    public String getPermissionName() {
        return permissionName;
    }
    
    /**
     * 设置权限名称
     * @param permissionName 权限名称
     */
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    
    /**
     * 获取权限代码
     * @return String 权限代码
     */
    public String getPermissionCode() {
        return permissionCode;
    }
    
    /**
     * 设置权限代码
     * @param permissionCode 权限代码
     */
    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
    
    /**
     * 获取权限描述
     * @return String 权限描述
     */
    public String getPermissionDesc() {
        return permissionDesc;
    }
    
    /**
     * 设置权限描述
     * @param permissionDesc 权限描述
     */
    public void setPermissionDesc(String permissionDesc) {
        this.permissionDesc = permissionDesc;
    }
    
    /**
     * 获取权限类型
     * @return int 权限类型
     */
    public int getPermissionType() {
        return permissionType;
    }
    
    /**
     * 设置权限类型
     * @param permissionType 权限类型
     */
    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
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
    
    /**
     * 获取更新时间
     * @return Date 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * 设置更新时间
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "Permission{" +
                "permissionID=" + permissionID +
                ", permissionName='" + permissionName + '\'' +
                ", permissionCode='" + permissionCode + '\'' +
                ", permissionDesc='" + permissionDesc + '\'' +
                ", permissionType=" + permissionType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 