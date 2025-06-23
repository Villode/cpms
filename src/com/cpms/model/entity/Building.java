package com.cpms.model.entity;

import java.util.Date;

/**
 * 楼栋实体类
 * 对应数据库中的Building表
 */
public class Building {
    private int buildingID;        // 楼栋ID
    private String buildingName;   // 楼栋名称
    private String buildingCode;   // 楼栋编码
    private String address;        // 楼栋地址
    private int totalFloors;       // 总楼层数
    private int totalUnits;        // 总单元数
    private int totalRooms;        // 总房间数
    private int managerID;         // 管家ID
    private Date createTime;       // 创建时间
    private Date updateTime;       // 更新时间
    
    /**
     * 默认构造方法
     */
    public Building() {
    }
    
    /**
     * 带参数的构造方法
     * @param buildingID 楼栋ID
     * @param buildingName 楼栋名称
     * @param buildingCode 楼栋编码
     * @param address 楼栋地址
     * @param totalFloors 总楼层数
     * @param totalUnits 总单元数
     * @param totalRooms 总房间数
     * @param managerID 管家ID
     */
    public Building(int buildingID, String buildingName, String buildingCode, String address, 
                    int totalFloors, int totalUnits, int totalRooms, int managerID) {
        this.buildingID = buildingID;
        this.buildingName = buildingName;
        this.buildingCode = buildingCode;
        this.address = address;
        this.totalFloors = totalFloors;
        this.totalUnits = totalUnits;
        this.totalRooms = totalRooms;
        this.managerID = managerID;
    }
    
    /**
     * 获取楼栋ID
     * @return int 楼栋ID
     */
    public int getBuildingID() {
        return buildingID;
    }
    
    /**
     * 设置楼栋ID
     * @param buildingID 楼栋ID
     */
    public void setBuildingID(int buildingID) {
        this.buildingID = buildingID;
    }
    
    /**
     * 获取楼栋名称
     * @return String 楼栋名称
     */
    public String getBuildingName() {
        return buildingName;
    }
    
    /**
     * 设置楼栋名称
     * @param buildingName 楼栋名称
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
    
    /**
     * 获取楼栋编码
     * @return String 楼栋编码
     */
    public String getBuildingCode() {
        return buildingCode;
    }
    
    /**
     * 设置楼栋编码
     * @param buildingCode 楼栋编码
     */
    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }
    
    /**
     * 获取楼栋地址
     * @return String 楼栋地址
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * 设置楼栋地址
     * @param address 楼栋地址
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * 获取总楼层数
     * @return int 总楼层数
     */
    public int getTotalFloors() {
        return totalFloors;
    }
    
    /**
     * 设置总楼层数
     * @param totalFloors 总楼层数
     */
    public void setTotalFloors(int totalFloors) {
        this.totalFloors = totalFloors;
    }
    
    /**
     * 获取总单元数
     * @return int 总单元数
     */
    public int getTotalUnits() {
        return totalUnits;
    }
    
    /**
     * 设置总单元数
     * @param totalUnits 总单元数
     */
    public void setTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
    }
    
    /**
     * 获取总房间数
     * @return int 总房间数
     */
    public int getTotalRooms() {
        return totalRooms;
    }
    
    /**
     * 设置总房间数
     * @param totalRooms 总房间数
     */
    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }
    
    /**
     * 获取管家ID
     * @return int 管家ID
     */
    public int getManagerID() {
        return managerID;
    }
    
    /**
     * 设置管家ID
     * @param managerID 管家ID
     */
    public void setManagerID(int managerID) {
        this.managerID = managerID;
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
        return "Building{" +
                "buildingID=" + buildingID +
                ", buildingName='" + buildingName + '\'' +
                ", buildingCode='" + buildingCode + '\'' +
                ", address='" + address + '\'' +
                ", totalFloors=" + totalFloors +
                ", totalUnits=" + totalUnits +
                ", totalRooms=" + totalRooms +
                ", managerID=" + managerID +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 