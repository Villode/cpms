package com.cpms.model.entity;

import java.util.Date;

/**
 * 角色实体类
 * 对应数据库中的Role表
 */
public class Role {
    private int roleID;          // 角色ID
    private String roleName;     // 角色名称
    private String roleDesc;     // 角色描述
    private Date createTime;     // 创建时间
    private Date updateTime;     // 更新时间
    
    /**
     * 默认构造方法
     */
    public Role() {
    }
    
    /**
     * 带参数的构造方法
     * @param roleID 角色ID
     * @param roleName 角色名称
     * @param roleDesc 角色描述
     */
    public Role(int roleID, String roleName, String roleDesc) {
        this.roleID = roleID;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
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
     * 获取角色名称
     * @return String 角色名称
     */
    public String getRoleName() {
        return roleName;
    }
    
    /**
     * 设置角色名称
     * @param roleName 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    /**
     * 获取角色描述
     * @return String 角色描述
     */
    public String getRoleDesc() {
        return roleDesc;
    }
    
    /**
     * 设置角色描述
     * @param roleDesc 角色描述
     */
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
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
        return "Role{" +
                "roleID=" + roleID +
                ", roleName='" + roleName + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 