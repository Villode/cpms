package com.cpms.controller.steward;

import com.cpms.model.dao.PaymentRecordDAO;
import com.cpms.model.dao.UserDAO;
import com.cpms.model.entity.PaymentRecord;
import com.cpms.model.entity.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 物业管家物业费管理控制器
 * 处理物业费账单的创建、查询和管理
 */
public class PaymentManageController {
    
    /**
     * 获取所有缴费记录
     * @return 缴费记录列表
     */
    public List<PaymentRecord> getAllPaymentRecords() {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            return paymentRecordDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (paymentRecordDAO != null) {
                paymentRecordDAO.close();
            }
        }
    }
    
    /**
     * 根据缴费状态获取缴费记录
     * @param status 缴费状态（0-未缴费，1-已缴费）
     * @return 缴费记录列表
     */
    public List<PaymentRecord> getPaymentRecordsByStatus(int status) {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            return paymentRecordDAO.findByStatus(status);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (paymentRecordDAO != null) {
                paymentRecordDAO.close();
            }
        }
    }
    
    /**
     * 根据楼栋ID获取缴费记录
     * @param buildingID 楼栋ID
     * @return 缴费记录列表
     */
    public List<PaymentRecord> getPaymentRecordsByBuilding(int buildingID) {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            return paymentRecordDAO.findByBuildingID(buildingID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (paymentRecordDAO != null) {
                paymentRecordDAO.close();
            }
        }
    }
    
    /**
     * 根据ID获取缴费记录
     * @param paymentID 缴费记录ID
     * @return 缴费记录
     */
    public PaymentRecord getPaymentRecordByID(int paymentID) {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            return paymentRecordDAO.findByID(paymentID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (paymentRecordDAO != null) {
                paymentRecordDAO.close();
            }
        }
    }
    
    /**
     * 创建新的缴费账单
     * @param userID 用户ID
     * @param buildingID 楼栋ID
     * @param roomNumber 房间号
     * @param paymentType 缴费类型
     * @param amount 金额
     * @param dueDate 截止日期
     * @param remark 备注
     * @return 是否成功
     */
    public boolean createPaymentBill(int userID, int buildingID, String roomNumber, int paymentType, 
                                    double amount, Timestamp dueDate, String remark) {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            
            // 创建缴费记录对象
            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setUserID(userID);
            paymentRecord.setBuildingID(buildingID);
            paymentRecord.setRoomNumber(roomNumber);
            paymentRecord.setPaymentType(paymentType);
            paymentRecord.setAmount(amount);
            paymentRecord.setPaymentStatus(0); // 未缴费
            paymentRecord.setDueDate(dueDate);
            paymentRecord.setCreateTime(new Timestamp(new Date().getTime()));
            paymentRecord.setRemark(remark);
            
            // 添加缴费记录
            return paymentRecordDAO.addPaymentRecord(paymentRecord);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (paymentRecordDAO != null) {
                paymentRecordDAO.close();
            }
        }
    }
    
    /**
     * 批量创建物业费账单
     * @param buildingID 楼栋ID
     * @param paymentType 缴费类型
     * @param amount 金额
     * @param dueDate 截止日期
     * @param remark 备注
     * @return 成功创建的账单数量
     */
    public int createBatchPaymentBills(int buildingID, int paymentType, double amount, 
                                     Timestamp dueDate, String remark) {
        UserDAO userDAO = null;
        PaymentRecordDAO paymentRecordDAO = null;
        int successCount = 0;
        
        try {
            userDAO = new UserDAO();
            paymentRecordDAO = new PaymentRecordDAO();
            
            // 获取指定楼栋的所有业主
            List<User> ownerList = userDAO.findOwnersByBuildingID(buildingID);
            
            // 为每个业主创建账单
            for (User owner : ownerList) {
                PaymentRecord paymentRecord = new PaymentRecord();
                paymentRecord.setUserID(owner.getUserID());
                paymentRecord.setBuildingID(buildingID);
                paymentRecord.setRoomNumber(owner.getRoomNumber());
                paymentRecord.setPaymentType(paymentType);
                paymentRecord.setAmount(amount);
                paymentRecord.setPaymentStatus(0); // 未缴费
                paymentRecord.setDueDate(dueDate);
                paymentRecord.setCreateTime(new Timestamp(new Date().getTime()));
                paymentRecord.setRemark(remark);
                
                // 添加缴费记录
                boolean result = paymentRecordDAO.addPaymentRecord(paymentRecord);
                if (result) {
                    successCount++;
                }
            }
            
            return successCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (userDAO != null) {
                userDAO.close();
            }
            if (paymentRecordDAO != null) {
                paymentRecordDAO.close();
            }
        }
    }
    
    /**
     * 更新缴费记录
     * @param paymentRecord 缴费记录
     * @return 是否成功
     */
    public boolean updatePaymentRecord(PaymentRecord paymentRecord) {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            return paymentRecordDAO.updatePaymentRecord(paymentRecord);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (paymentRecordDAO != null) {
                paymentRecordDAO.close();
            }
        }
    }
    
    /**
     * 删除缴费记录
     * @param paymentID 缴费记录ID
     * @return 是否成功
     */
    public boolean deletePaymentRecord(int paymentID) {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            return paymentRecordDAO.deletePaymentRecord(paymentID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (paymentRecordDAO != null) {
                paymentRecordDAO.close();
            }
        }
    }
    
    /**
     * 确认缴费
     * @param paymentID 缴费记录ID
     * @param paymentMethod 支付方式
     * @param transactionNo 交易号
     * @return 是否成功
     */
    public boolean confirmPayment(int paymentID, String paymentMethod, String transactionNo) {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            
            // 获取当前时间作为支付时间
            Timestamp paymentTime = new Timestamp(new Date().getTime());
            
            // 更新缴费状态
            return paymentRecordDAO.updatePaymentStatus(paymentID, 1, paymentMethod, transactionNo, paymentTime);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (paymentRecordDAO != null) {
                paymentRecordDAO.close();
            }
        }
    }
    
    /**
     * 获取缴费类型文本
     * @param paymentType 缴费类型
     * @return 缴费类型文本
     */
    public String getPaymentTypeText(int paymentType) {
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
     * @param paymentStatus 缴费状态
     * @return 缴费状态文本
     */
    public String getPaymentStatusText(int paymentStatus) {
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