package com.cpms.model.entity;

import java.util.Date;

/**
 * 车位实体类
 * 对应数据库中的ParkingSpot表
 */
public class ParkingSpot {
    private int parkingID;           // 车位ID
    private int buildingID;          // 楼栋ID
    private String spotNumber;       // 车位编号
    private String location;         // 车位位置
    private Integer ownerUserID;     // 业主ID
    private String licensePlate;     // 车牌号
    private int usageStatus;         // 使用状态（0-空闲，1-已占用）
    private Date createTime;         // 创建时间
    private Date updateTime;         // 更新时间
    
    /**
     * 默认构造方法
     */
    public ParkingSpot() {
    }
    
    /**
     * 带参数的构造方法
     * @param parkingID 车位ID
     * @param buildingID 楼栋ID
     * @param spotNumber 车位编号
     * @param location 车位位置
     */
    public ParkingSpot(int parkingID, int buildingID, String spotNumber, String location) {
        this.parkingID = parkingID;
        this.buildingID = buildingID;
        this.spotNumber = spotNumber;
        this.location = location;
        this.usageStatus = 0; // 默认空闲
    }
    
    /**
     * 获取车位ID
     * @return int 车位ID
     */
    public int getParkingID() {
        return parkingID;
    }
    
    /**
     * 设置车位ID
     * @param parkingID 车位ID
     */
    public void setParkingID(int parkingID) {
        this.parkingID = parkingID;
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
     * 获取车位编号
     * @return String 车位编号
     */
    public String getSpotNumber() {
        return spotNumber;
    }
    
    /**
     * 设置车位编号
     * @param spotNumber 车位编号
     */
    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }
    
    /**
     * 获取车位位置
     * @return String 车位位置
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * 设置车位位置
     * @param location 车位位置
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * 获取业主ID
     * @return Integer 业主ID
     */
    public Integer getOwnerUserID() {
        return ownerUserID;
    }
    
    /**
     * 设置业主ID
     * @param ownerUserID 业主ID
     */
    public void setOwnerUserID(Integer ownerUserID) {
        this.ownerUserID = ownerUserID;
    }
    
    /**
     * 获取车牌号
     * @return String 车牌号
     */
    public String getLicensePlate() {
        return licensePlate;
    }
    
    /**
     * 设置车牌号
     * @param licensePlate 车牌号
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    /**
     * 获取使用状态
     * @return int 使用状态
     */
    public int getUsageStatus() {
        return usageStatus;
    }
    
    /**
     * 设置使用状态
     * @param usageStatus 使用状态
     */
    public void setUsageStatus(int usageStatus) {
        this.usageStatus = usageStatus;
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
        return "ParkingSpot{" +
                "parkingID=" + parkingID +
                ", buildingID=" + buildingID +
                ", spotNumber='" + spotNumber + '\'' +
                ", location='" + location + '\'' +
                ", ownerUserID=" + ownerUserID +
                ", licensePlate='" + licensePlate + '\'' +
                ", usageStatus=" + usageStatus +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 