package com.cpms.model.dao;

import com.cpms.model.entity.PaymentRecord;
import com.cpms.util.db.DatabaseConnection;
import com.cpms.util.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 缴费记录数据访问对象类
 * 提供对PaymentRecord表的增删改查操作
 */
public class PaymentRecordDAO {
    private DatabaseConnection dbc = null;
    private Connection conn = null;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public PaymentRecordDAO() throws SQLException {
        dbc = new DatabaseConnection();
        conn = dbc.getConnection();
    }
    
    /**
     * 添加缴费记录
     * @param paymentRecord 缴费记录对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean addPaymentRecord(PaymentRecord paymentRecord) throws SQLException {
        String sql = "INSERT INTO payment_record(UserID, BuildingID, RoomNumber, PaymentType, Amount, PaymentStatus, " +
                     "PaymentMethod, TransactionNo, DueDate, PaymentTime, CreateTime, Remark) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int result = DatabaseUtil.executeUpdate(conn, sql, 
                paymentRecord.getUserID(), 
                paymentRecord.getBuildingID(), 
                paymentRecord.getRoomNumber(),
                paymentRecord.getPaymentType(), 
                paymentRecord.getAmount(), 
                paymentRecord.getPaymentStatus(),
                paymentRecord.getPaymentMethod(),
                paymentRecord.getTransactionNo(),
                paymentRecord.getDueDate(),
                paymentRecord.getPaymentTime(),
                paymentRecord.getCreateTime(),
                paymentRecord.getRemark());
        return result > 0;
    }
    
    /**
     * 更新缴费记录
     * @param paymentRecord 缴费记录对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updatePaymentRecord(PaymentRecord paymentRecord) throws SQLException {
        String sql = "UPDATE payment_record SET UserID=?, BuildingID=?, RoomNumber=?, PaymentType=?, Amount=?, " +
                     "PaymentStatus=?, PaymentMethod=?, TransactionNo=?, DueDate=?, PaymentTime=?, Remark=? " +
                     "WHERE PaymentID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, 
                paymentRecord.getUserID(), 
                paymentRecord.getBuildingID(), 
                paymentRecord.getRoomNumber(),
                paymentRecord.getPaymentType(), 
                paymentRecord.getAmount(), 
                paymentRecord.getPaymentStatus(),
                paymentRecord.getPaymentMethod(),
                paymentRecord.getTransactionNo(),
                paymentRecord.getDueDate(),
                paymentRecord.getPaymentTime(),
                paymentRecord.getRemark(),
                paymentRecord.getPaymentID());
        return result > 0;
    }
    
    /**
     * 更新缴费状态
     * @param paymentID 缴费记录ID
     * @param status 缴费状态
     * @param paymentMethod 支付方式
     * @param transactionNo 交易号
     * @param paymentTime 缴费时间
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updatePaymentStatus(int paymentID, int status, String paymentMethod, 
                                      String transactionNo, java.sql.Timestamp paymentTime) throws SQLException {
        String sql = "UPDATE payment_record SET PaymentStatus=?, PaymentMethod=?, TransactionNo=?, PaymentTime=? " +
                     "WHERE PaymentID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, 
                status, paymentMethod, transactionNo, paymentTime, paymentID);
        return result > 0;
    }
    
    /**
     * 删除缴费记录
     * @param paymentID 缴费记录ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deletePaymentRecord(int paymentID) throws SQLException {
        String sql = "DELETE FROM payment_record WHERE PaymentID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, paymentID);
        return result > 0;
    }
    
    /**
     * 根据ID查找缴费记录
     * @param paymentID 缴费记录ID
     * @return PaymentRecord 缴费记录对象
     * @throws SQLException 数据库操作异常
     */
    public PaymentRecord findByID(int paymentID) throws SQLException {
        String sql = "SELECT p.*, u.RealName AS UserName, b.BuildingName " +
                     "FROM payment_record p " +
                     "LEFT JOIN user u ON p.UserID = u.UserID " +
                     "LEFT JOIN building b ON p.BuildingID = b.BuildingID " +
                     "WHERE p.PaymentID=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, paymentID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPaymentRecord(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 根据用户ID查找缴费记录
     * @param userID 用户ID
     * @return List<PaymentRecord> 缴费记录列表
     * @throws SQLException 数据库操作异常
     */
    public List<PaymentRecord> findByUserID(int userID) throws SQLException {
        String sql = "SELECT p.*, u.RealName AS UserName, b.BuildingName " +
                     "FROM payment_record p " +
                     "LEFT JOIN user u ON p.UserID = u.UserID " +
                     "LEFT JOIN building b ON p.BuildingID = b.BuildingID " +
                     "WHERE p.UserID=? " +
                     "ORDER BY p.CreateTime DESC";
        List<PaymentRecord> paymentRecordList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                paymentRecordList.add(mapResultSetToPaymentRecord(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return paymentRecordList;
    }
    
    /**
     * 根据用户ID和缴费状态查找缴费记录
     * @param userID 用户ID
     * @param status 缴费状态
     * @return List<PaymentRecord> 缴费记录列表
     * @throws SQLException 数据库操作异常
     */
    public List<PaymentRecord> findByUserIDAndStatus(int userID, int status) throws SQLException {
        String sql = "SELECT p.*, u.RealName AS UserName, b.BuildingName " +
                     "FROM payment_record p " +
                     "LEFT JOIN user u ON p.UserID = u.UserID " +
                     "LEFT JOIN building b ON p.BuildingID = b.BuildingID " +
                     "WHERE p.UserID=? AND p.PaymentStatus=? " +
                     "ORDER BY p.DueDate ASC";
        List<PaymentRecord> paymentRecordList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userID);
            pstmt.setInt(2, status);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                paymentRecordList.add(mapResultSetToPaymentRecord(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return paymentRecordList;
    }
    
    /**
     * 根据楼栋ID查找缴费记录
     * @param buildingID 楼栋ID
     * @return List<PaymentRecord> 缴费记录列表
     * @throws SQLException 数据库操作异常
     */
    public List<PaymentRecord> findByBuildingID(int buildingID) throws SQLException {
        String sql = "SELECT p.*, u.RealName AS UserName, b.BuildingName " +
                     "FROM payment_record p " +
                     "LEFT JOIN user u ON p.UserID = u.UserID " +
                     "LEFT JOIN building b ON p.BuildingID = b.BuildingID " +
                     "WHERE p.BuildingID=? " +
                     "ORDER BY p.CreateTime DESC";
        List<PaymentRecord> paymentRecordList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buildingID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                paymentRecordList.add(mapResultSetToPaymentRecord(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return paymentRecordList;
    }
    
    /**
     * 根据缴费状态查找缴费记录
     * @param status 缴费状态
     * @return List<PaymentRecord> 缴费记录列表
     * @throws SQLException 数据库操作异常
     */
    public List<PaymentRecord> findByStatus(int status) throws SQLException {
        String sql = "SELECT p.*, u.RealName AS UserName, b.BuildingName " +
                     "FROM payment_record p " +
                     "LEFT JOIN user u ON p.UserID = u.UserID " +
                     "LEFT JOIN building b ON p.BuildingID = b.BuildingID " +
                     "WHERE p.PaymentStatus=? " +
                     "ORDER BY p.DueDate ASC";
        List<PaymentRecord> paymentRecordList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                paymentRecordList.add(mapResultSetToPaymentRecord(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return paymentRecordList;
    }
    
    /**
     * 查询所有缴费记录
     * @return List<PaymentRecord> 缴费记录列表
     * @throws SQLException 数据库操作异常
     */
    public List<PaymentRecord> findAll() throws SQLException {
        String sql = "SELECT p.*, u.RealName AS UserName, b.BuildingName " +
                     "FROM payment_record p " +
                     "LEFT JOIN user u ON p.UserID = u.UserID " +
                     "LEFT JOIN building b ON p.BuildingID = b.BuildingID " +
                     "ORDER BY p.CreateTime DESC";
        List<PaymentRecord> paymentRecordList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                paymentRecordList.add(mapResultSetToPaymentRecord(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return paymentRecordList;
    }
    
    /**
     * 将ResultSet映射为PaymentRecord对象
     * @param rs ResultSet对象
     * @return PaymentRecord对象
     * @throws SQLException 数据库操作异常
     */
    private PaymentRecord mapResultSetToPaymentRecord(ResultSet rs) throws SQLException {
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setPaymentID(rs.getInt("PaymentID"));
        paymentRecord.setUserID(rs.getInt("UserID"));
        paymentRecord.setBuildingID(rs.getInt("BuildingID"));
        paymentRecord.setRoomNumber(rs.getString("RoomNumber"));
        paymentRecord.setPaymentType(rs.getInt("PaymentType"));
        paymentRecord.setAmount(rs.getDouble("Amount"));
        paymentRecord.setPaymentStatus(rs.getInt("PaymentStatus"));
        paymentRecord.setPaymentMethod(rs.getString("PaymentMethod"));
        paymentRecord.setTransactionNo(rs.getString("TransactionNo"));
        paymentRecord.setDueDate(rs.getTimestamp("DueDate"));
        paymentRecord.setPaymentTime(rs.getTimestamp("PaymentTime"));
        paymentRecord.setCreateTime(rs.getTimestamp("CreateTime"));
        paymentRecord.setRemark(rs.getString("Remark"));
        
        // 设置额外属性
        paymentRecord.setUserName(rs.getString("UserName"));
        paymentRecord.setBuildingName(rs.getString("BuildingName"));
        
        return paymentRecord;
    }
    
    /**
     * 关闭数据库连接
     */
    public void close() {
        if (dbc != null) {
            dbc.close();
        }
    }
}