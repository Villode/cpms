package com.cpms.model.entity;

import java.sql.Timestamp;

/**
 * 缴费记录实体类
 * 用于存储业主缴费信息
 */
public class PaymentRecord {
    private int paymentID;        // 缴费ID
    private int userID;           // 用户ID
    private int buildingID;       // 楼栋ID
    private String roomNumber;    // 房间号
    private int paymentType;      // 缴费类型（1-物业费，2-水费，3-电费，4-其他）
    private double amount;        // 缴费金额
    private int paymentStatus;    // 缴费状态（0-未缴费，1-已缴费）
    private String paymentMethod; // 缴费方式（支付宝，微信，银行卡等）
    private String transactionNo; // 交易号
    private Timestamp dueDate;    // 截止日期
    private Timestamp paymentTime;// 缴费时间
    private Timestamp createTime; // 创建时间
    private String remark;        // 备注
    
    // 额外属性，不对应数据库字段，用于显示
    private String userName;      // 用户姓名
    private String buildingName;  // 楼栋名称
    
    /**
     * 默认构造方法
     */
    public PaymentRecord() {
    }
    
    /**
     * 带参数的构造方法
     * @param paymentID 缴费ID
     * @param userID 用户ID
     * @param buildingID 楼栋ID
     * @param roomNumber 房间号
     * @param paymentType 缴费类型
     * @param amount 缴费金额
     * @param paymentStatus 缴费状态
     * @param paymentMethod 缴费方式
     * @param transactionNo 交易号
     * @param dueDate 截止日期
     * @param paymentTime 缴费时间
     * @param createTime 创建时间
     * @param remark 备注
     */
    public PaymentRecord(int paymentID, int userID, int buildingID, String roomNumber, int paymentType,
                        double amount, int paymentStatus, String paymentMethod, String transactionNo,
                        Timestamp dueDate, Timestamp paymentTime, Timestamp createTime, String remark) {
        this.paymentID = paymentID;
        this.userID = userID;
        this.buildingID = buildingID;
        this.roomNumber = roomNumber;
        this.paymentType = paymentType;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.transactionNo = transactionNo;
        this.dueDate = dueDate;
        this.paymentTime = paymentTime;
        this.createTime = createTime;
        this.remark = remark;
    }
    
    /**
     * 获取缴费ID
     * @return 缴费ID
     */
    public int getPaymentID() {
        return paymentID;
    }
    
    /**
     * 设置缴费ID
     * @param paymentID 缴费ID
     */
    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }
    
    /**
     * 获取用户ID
     * @return 用户ID
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
     * 获取楼栋ID
     * @return 楼栋ID
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
    
    /**
     * 获取缴费类型
     * @return 缴费类型
     */
    public int getPaymentType() {
        return paymentType;
    }
    
    /**
     * 设置缴费类型
     * @param paymentType 缴费类型
     */
    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }
    
    /**
     * 获取缴费金额
     * @return 缴费金额
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * 设置缴费金额
     * @param amount 缴费金额
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /**
     * 获取缴费状态
     * @return 缴费状态
     */
    public int getPaymentStatus() {
        return paymentStatus;
    }
    
    /**
     * 设置缴费状态
     * @param paymentStatus 缴费状态
     */
    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    /**
     * 获取缴费方式
     * @return 缴费方式
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    /**
     * 设置缴费方式
     * @param paymentMethod 缴费方式
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    /**
     * 获取交易号
     * @return 交易号
     */
    public String getTransactionNo() {
        return transactionNo;
    }
    
    /**
     * 设置交易号
     * @param transactionNo 交易号
     */
    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }
    
    /**
     * 获取截止日期
     * @return 截止日期
     */
    public Timestamp getDueDate() {
        return dueDate;
    }
    
    /**
     * 设置截止日期
     * @param dueDate 截止日期
     */
    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }
    
    /**
     * 获取缴费时间
     * @return 缴费时间
     */
    public Timestamp getPaymentTime() {
        return paymentTime;
    }
    
    /**
     * 设置缴费时间
     * @param paymentTime 缴费时间
     */
    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }
    
    /**
     * 获取创建时间
     * @return 创建时间
     */
    public Timestamp getCreateTime() {
        return createTime;
    }
    
    /**
     * 设置创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    /**
     * 获取备注
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }
    
    /**
     * 设置备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    /**
     * 获取用户姓名
     * @return 用户姓名
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * 设置用户姓名
     * @param userName 用户姓名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * 获取楼栋名称
     * @return 楼栋名称
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
     * 获取缴费类型文本
     * @return 缴费类型文本
     */
    public String getPaymentTypeText() {
        switch (paymentType) {
            case 1:
                return "物业费";
            case 2:
                return "水费";
            case 3:
                return "电费";
            case 4:
                return "其他";
            default:
                return "未知";
        }
    }
    
    /**
     * 获取缴费状态文本
     * @return 缴费状态文本
     */
    public String getPaymentStatusText() {
        switch (paymentStatus) {
            case 0:
                return "未缴费";
            case 1:
                return "已缴费";
            default:
                return "未知";
        }
    }
} 