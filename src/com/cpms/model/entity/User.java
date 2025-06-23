package com.cpms.model.entity;

import java.util.Date;

/**
 * 用户实体类
 * 对应数据库中的User表
 */
public class User {
    private int userID;              // 用户ID
    private String username;         // 用户名
    private String password;         // 密码
    private String realName;         // 真实姓名
    private String phone;            // 联系电话
    private int roleID;              // 角色ID
    private Integer buildingID;      // 所属楼栋ID（业主使用）
    private Integer managedBuildingID; // 管理的楼栋ID（管家使用）
    private int accountStatus;       // 账号状态（0-禁用，1-启用）
    private Date createTime;         // 创建时间
    private Date updateTime;         // 更新时间
    private String roomNumber;       // 房间号
    
    /**
     * 默认构造方法
     */
    public User() {
    }
    
    /**
     * 带参数的构造方法
     * @param userID 用户ID
     * @param username 用户名
     * @param password 密码
     * @param realName 真实姓名
     * @param phone 联系电话
     * @param roleID 角色ID
     */
    public User(int userID, String username, String password, String realName, String phone, int roleID) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.phone = phone;
        this.roleID = roleID;
        this.accountStatus = 1; // 默认启用
    }
    
    /**
     * 获取用户ID
     * @return int 用户ID
     */
    public int getUserID() {
        return userID;
    }
    
    /**
     * 设置用户ID
     * @param userID 用户ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    /**
     * 获取用户名
     * @return String 用户名
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * 设置用户名
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 获取密码
     * @return String 密码
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * 获取真实姓名
     * @return String 真实姓名
     */
    public String getRealName() {
        return realName;
    }
    
    /**
     * 设置真实姓名
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    /**
     * 获取联系电话
     * @return String 联系电话
     */
    public String getPhone() {
        return phone;
    }
    
    /**
     * 设置联系电话
     * @param phone 联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
     * 获取所属楼栋ID
     * @return Integer 所属楼栋ID
     */
    public Integer getBuildingID() {
        return buildingID;
    }
    
    /**
     * 设置所属楼栋ID
     * @param buildingID 所属楼栋ID
     */
    public void setBuildingID(Integer buildingID) {
        this.buildingID = buildingID;
    }
    
    /**
     * 获取管理的楼栋ID
     * @return Integer 管理的楼栋ID
     */
    public Integer getManagedBuildingID() {
        return managedBuildingID;
    }
    
    /**
     * 设置管理的楼栋ID
     * @param managedBuildingID 管理的楼栋ID
     */
    public void setManagedBuildingID(Integer managedBuildingID) {
        this.managedBuildingID = managedBuildingID;
    }
    
    /**
     * 获取账号状态
     * @return int 账号状态
     */
    public int getAccountStatus() {
        return accountStatus;
    }
    
    /**
     * 设置账号状态
     * @param accountStatus 账号状态
     */
    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
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
    
    /**
     * 获取房间号
     * @return 房间号
     */
    public String getRoomNumber() {
        return roomNumber;
    }
    
    /**
     * 设置房间号
     * @param roomNumber 房间号
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", roleID=" + roleID +
                ", buildingID=" + buildingID +
                ", managedBuildingID=" + managedBuildingID +
                ", accountStatus=" + accountStatus +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", roomNumber='" + roomNumber + '\'' +
                '}';
    }
} 