package com.cpms.model.entity;

import java.util.Date;

/**
 * 访客记录实体类
 * 对应数据库中的VisitorRecord表
 */
public class VisitorRecord {
    private int visitorID;           // 访客记录ID
    private int bookerUserID;        // 预约用户ID
    private String visitorName;      // 访客姓名
    private String licensePlate;     // 车牌号
    private Date bookTime;           // 预约时间
    private boolean syncedToSecurity; // 是否已同步至安保处
    private Date createTime;         // 创建时间
    private Date updateTime;         // 更新时间
    
    /**
     * 默认构造方法
     */
    public VisitorRecord() {
    }
    
    /**
     * 带参数的构造方法
     * @param visitorID 访客记录ID
     * @param bookerUserID 预约用户ID
     * @param visitorName 访客姓名
     * @param licensePlate 车牌号
     * @param bookTime 预约时间
     */
    public VisitorRecord(int visitorID, int bookerUserID, String visitorName, String licensePlate, Date bookTime) {
        this.visitorID = visitorID;
        this.bookerUserID = bookerUserID;
        this.visitorName = visitorName;
        this.licensePlate = licensePlate;
        this.bookTime = bookTime;
        this.syncedToSecurity = false;
    }
    
    /**
     * 获取访客记录ID
     * @return int 访客记录ID
     */
    public int getVisitorID() {
        return visitorID;
    }
    
    /**
     * 设置访客记录ID
     * @param visitorID 访客记录ID
     */
    public void setVisitorID(int visitorID) {
        this.visitorID = visitorID;
    }
    
    /**
     * 获取预约用户ID
     * @return int 预约用户ID
     */
    public int getBookerUserID() {
        return bookerUserID;
    }
    
    /**
     * 设置预约用户ID
     * @param bookerUserID 预约用户ID
     */
    public void setBookerUserID(int bookerUserID) {
        this.bookerUserID = bookerUserID;
    }
    
    /**
     * 获取访客姓名
     * @return String 访客姓名
     */
    public String getVisitorName() {
        return visitorName;
    }
    
    /**
     * 设置访客姓名
     * @param visitorName 访客姓名
     */
    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
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
     * 获取预约时间
     * @return Date 预约时间
     */
    public Date getBookTime() {
        return bookTime;
    }
    
    /**
     * 设置预约时间
     * @param bookTime 预约时间
     */
    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }
    
    /**
     * 获取是否已同步至安保处
     * @return boolean 是否已同步至安保处
     */
    public boolean isSyncedToSecurity() {
        return syncedToSecurity;
    }
    
    /**
     * 设置是否已同步至安保处
     * @param syncedToSecurity 是否已同步至安保处
     */
    public void setSyncedToSecurity(boolean syncedToSecurity) {
        this.syncedToSecurity = syncedToSecurity;
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
        return "VisitorRecord{" +
                "visitorID=" + visitorID +
                ", bookerUserID=" + bookerUserID +
                ", visitorName='" + visitorName + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", bookTime=" + bookTime +
                ", syncedToSecurity=" + syncedToSecurity +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 