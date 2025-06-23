package com.cpms.model.entity;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 报修实体类
 * 用于存储报修信息
 */
public class Repair {
    private int repairID;          // 报修ID
    private int userID;            // 报修用户ID
    private int buildingID;        // 楼栋ID
    private String roomNumber;     // 房间号
    private int repairType;        // 报修类型（1-水电维修，2-家具维修，3-门窗维修，4-其他）
    private String repairDesc;     // 报修描述
    private int repairStatus;      // 报修状态（0-待处理，1-处理中，2-已完成，3-已取消）
    private int handlerID;         // 处理人ID
    private String handleOpinion;  // 处理意见
    private Timestamp createTime;  // 创建时间
    private Timestamp updateTime;  // 更新时间
    private String imagePath;      // 图片路径
    
    // 额外属性，不对应数据库字段，用于显示
    private String userName;       // 报修用户姓名
    private String buildingName;   // 楼栋名称
    private String handlerName;    // 处理人姓名
    
    /**
     * 默认构造方法
     */
    public Repair() {
    }
    
    /**
     * 带参数的构造方法
     * @param repairID 报修ID
     * @param userID 用户ID
     * @param buildingID 楼栋ID
     * @param roomNumber 房间号
     * @param repairType 报修类型
     * @param repairDesc 报修描述
     * @param repairStatus 报修状态
     * @param handlerID 处理人ID
     * @param handleOpinion 处理意见
     * @param createTime 创建时间
     * @param updateTime 更新时间
     * @param imagePath 图片路径
     */
    public Repair(int repairID, int userID, int buildingID, String roomNumber, int repairType, 
                 String repairDesc, int repairStatus, int handlerID, String handleOpinion, 
                 Timestamp createTime, Timestamp updateTime, String imagePath) {
        this.repairID = repairID;
        this.userID = userID;
        this.buildingID = buildingID;
        this.roomNumber = roomNumber;
        this.repairType = repairType;
        this.repairDesc = repairDesc;
        this.repairStatus = repairStatus;
        this.handlerID = handlerID;
        this.handleOpinion = handleOpinion;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.imagePath = imagePath;
    }
    
    /**
     * 获取报修ID
     * @return 报修ID
     */
    public int getRepairID() {
        return repairID;
    }
    
    /**
     * 设置报修ID
     * @param repairID 报修ID
     */
    public void setRepairID(int repairID) {
        this.repairID = repairID;
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
     * 获取报修类型
     * @return 报修类型
     */
    public int getRepairType() {
        return repairType;
    }
    
    /**
     * 设置报修类型
     * @param repairType 报修类型
     */
    public void setRepairType(int repairType) {
        this.repairType = repairType;
    }
    
    /**
     * 获取报修描述
     * @return 报修描述
     */
    public String getRepairDesc() {
        return repairDesc;
    }
    
    /**
     * 设置报修描述
     * @param repairDesc 报修描述
     */
    public void setRepairDesc(String repairDesc) {
        this.repairDesc = repairDesc;
    }
    
    /**
     * 获取报修状态
     * @return 报修状态
     */
    public int getRepairStatus() {
        return repairStatus;
    }
    
    /**
     * 设置报修状态
     * @param repairStatus 报修状态
     */
    public void setRepairStatus(int repairStatus) {
        this.repairStatus = repairStatus;
    }
    
    /**
     * 获取处理人ID
     * @return 处理人ID
     */
    public int getHandlerID() {
        return handlerID;
    }
    
    /**
     * 设置处理人ID
     * @param handlerID 处理人ID
     */
    public void setHandlerID(int handlerID) {
        this.handlerID = handlerID;
    }
    
    /**
     * 获取处理意见
     * @return 处理意见
     */
    public String getHandleOpinion() {
        return handleOpinion;
    }
    
    /**
     * 设置处理意见
     * @param handleOpinion 处理意见
     */
    public void setHandleOpinion(String handleOpinion) {
        this.handleOpinion = handleOpinion;
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
     * 获取更新时间
     * @return 更新时间
     */
    public Timestamp getUpdateTime() {
        return updateTime;
    }
    
    /**
     * 设置更新时间
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
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
     * 获取处理人姓名
     * @return 处理人姓名
     */
    public String getHandlerName() {
        return handlerName;
    }
    
    /**
     * 设置处理人姓名
     * @param handlerName 处理人姓名
     */
    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }
    
    /**
     * 获取报修类型文本
     * @return 报修类型文本
     */
    public String getRepairTypeText() {
        switch (repairType) {
            case 1:
                return "水电维修";
            case 2:
                return "家具维修";
            case 3:
                return "门窗维修";
            case 4:
                return "其他";
            default:
                return "未知";
        }
    }
    
    /**
     * 获取报修状态文本
     * @return 报修状态文本
     */
    public String getRepairStatusText() {
        switch (repairStatus) {
            case 0:
                return "待处理";
            case 1:
                return "处理中";
            case 2:
                return "已完成";
            case 3:
                return "已取消";
            default:
                return "未知";
        }
    }

    /**
     * 获取图片路径
     * @return 图片路径
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * 设置图片路径
     * @param imagePath 图片路径
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * 获取提交用户ID
     * @return 提交用户ID
     */
    public int getSubmitUserID() {
        return userID;
    }

    /**
     * 设置提交用户ID
     * @param submitUserID 提交用户ID
     */
    public void setSubmitUserID(int submitUserID) {
        this.userID = submitUserID;
    }

    /**
     * 获取故障描述
     * @return 故障描述
     */
    public String getFaultDesc() {
        return repairDesc;
    }

    /**
     * 设置故障描述
     * @param faultDesc 故障描述
     */
    public void setFaultDesc(String faultDesc) {
        this.repairDesc = faultDesc;
    }

    /**
     * 获取状态
     * @return 状态
     */
    public int getStatus() {
        return repairStatus;
    }

    /**
     * 设置状态
     * @param status 状态
     */
    public void setStatus(int status) {
        this.repairStatus = status;
    }

    /**
     * 设置提交时间
     * @param submitTime 提交时间
     */
    public void setSubmitTime(Date submitTime) {
        this.createTime = new Timestamp(submitTime.getTime());
    }
} 