package com.cpms.controller.owner;

import com.cpms.model.dao.PaymentRecordDAO;
import com.cpms.model.entity.PaymentRecord;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 缴费控制器
 * 处理业主缴费相关的业务逻辑
 */
public class PaymentController {
    
    /**
     * 获取业主的所有缴费记录
     * @param userID 业主ID
     * @return 缴费记录列表
     */
    public List<PaymentRecord> getAllPaymentRecords(int userID) {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            return paymentRecordDAO.findByUserID(userID);
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
     * 获取业主的未缴费账单
     * @param userID 业主ID
     * @return 未缴费账单列表
     */
    public List<PaymentRecord> getUnpaidBills(int userID) {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            return paymentRecordDAO.findByUserIDAndStatus(userID, 0); // 0表示未缴费
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
     * 获取业主的已缴费记录
     * @param userID 业主ID
     * @return 已缴费记录列表
     */
    public List<PaymentRecord> getPaidRecords(int userID) {
        PaymentRecordDAO paymentRecordDAO = null;
        try {
            paymentRecordDAO = new PaymentRecordDAO();
            return paymentRecordDAO.findByUserIDAndStatus(userID, 1); // 1表示已缴费
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
     * 支付账单
     * @param paymentID 缴费记录ID
     * @param paymentMethod 支付方式
     * @param transactionNo 交易号
     * @return 是否成功
     */
    public boolean payBill(int paymentID, String paymentMethod, String transactionNo) {
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