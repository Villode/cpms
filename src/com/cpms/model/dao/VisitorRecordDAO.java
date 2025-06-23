package com.cpms.model.dao;

import com.cpms.model.entity.VisitorRecord;
import com.cpms.util.db.DatabaseConnection;
import com.cpms.util.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 访客记录数据访问对象类
 * 提供对VisitorRecord表的增删改查操作
 */
public class VisitorRecordDAO {
    private DatabaseConnection dbc = null;
    private Connection conn = null;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public VisitorRecordDAO() throws SQLException {
        dbc = new DatabaseConnection();
        conn = dbc.getConnection();
    }
    
    /**
     * 添加访客记录
     * @param visitorRecord 访客记录对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean addVisitorRecord(VisitorRecord visitorRecord) throws SQLException {
        String sql = "INSERT INTO VisitorRecord(BookerUserID, VisitorName, LicensePlate, BookTime, SyncedToSecurity) VALUES(?, ?, ?, ?, ?)";
        int result = DatabaseUtil.executeUpdate(conn, sql, 
                visitorRecord.getBookerUserID(), 
                visitorRecord.getVisitorName(), 
                visitorRecord.getLicensePlate(), 
                visitorRecord.getBookTime(), 
                visitorRecord.isSyncedToSecurity());
        return result > 0;
    }
    
    /**
     * 更新访客记录
     * @param visitorRecord 访客记录对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updateVisitorRecord(VisitorRecord visitorRecord) throws SQLException {
        String sql = "UPDATE VisitorRecord SET BookerUserID=?, VisitorName=?, LicensePlate=?, BookTime=?, SyncedToSecurity=? WHERE VisitorID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, 
                visitorRecord.getBookerUserID(), 
                visitorRecord.getVisitorName(), 
                visitorRecord.getLicensePlate(), 
                visitorRecord.getBookTime(), 
                visitorRecord.isSyncedToSecurity(), 
                visitorRecord.getVisitorID());
        return result > 0;
    }
    
    /**
     * 更新访客记录同步状态
     * @param visitorID 访客记录ID
     * @param syncedToSecurity 是否已同步至安保处
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updateSyncStatus(int visitorID, boolean syncedToSecurity) throws SQLException {
        String sql = "UPDATE VisitorRecord SET SyncedToSecurity=? WHERE VisitorID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, syncedToSecurity, visitorID);
        return result > 0;
    }
    
    /**
     * 根据ID删除访客记录
     * @param visitorID 访客记录ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deleteVisitorRecord(int visitorID) throws SQLException {
        String sql = "DELETE FROM VisitorRecord WHERE VisitorID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, visitorID);
        return result > 0;
    }
    
    /**
     * 根据ID查找访客记录
     * @param visitorID 访客记录ID
     * @return VisitorRecord 访客记录对象
     * @throws SQLException 数据库操作异常
     */
    public VisitorRecord findByID(int visitorID) throws SQLException {
        String sql = "SELECT * FROM VisitorRecord WHERE VisitorID=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, visitorID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToVisitorRecord(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 根据预约用户ID查找访客记录
     * @param bookerUserID 预约用户ID
     * @return List<VisitorRecord> 访客记录列表
     * @throws SQLException 数据库操作异常
     */
    public List<VisitorRecord> findByBookerUserID(int bookerUserID) throws SQLException {
        String sql = "SELECT * FROM VisitorRecord WHERE BookerUserID=? ORDER BY BookTime DESC";
        List<VisitorRecord> visitorRecordList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookerUserID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                visitorRecordList.add(mapResultSetToVisitorRecord(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return visitorRecordList;
    }
    
    /**
     * 查询当日访客记录
     * @param buildingID 楼栋ID
     * @return List<VisitorRecord> 访客记录列表
     * @throws SQLException 数据库操作异常
     */
    public List<VisitorRecord> findTodayRecordsByBuildingID(int buildingID) throws SQLException {
        String sql = "SELECT vr.* FROM VisitorRecord vr " +
                "JOIN User u ON vr.BookerUserID = u.UserID " +
                "WHERE u.BuildingID=? AND DATE(vr.BookTime)=CURDATE() " +
                "ORDER BY vr.BookTime DESC";
        List<VisitorRecord> visitorRecordList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buildingID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                visitorRecordList.add(mapResultSetToVisitorRecord(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return visitorRecordList;
    }
    
    /**
     * 查询指定日期范围内的访客记录
     * @param buildingID 楼栋ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return List<VisitorRecord> 访客记录列表
     * @throws SQLException 数据库操作异常
     */
    public List<VisitorRecord> findRecordsByDateRange(int buildingID, Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT vr.* FROM VisitorRecord vr " +
                "JOIN User u ON vr.BookerUserID = u.UserID " +
                "WHERE u.BuildingID=? AND vr.BookTime BETWEEN ? AND ? " +
                "ORDER BY vr.BookTime DESC";
        List<VisitorRecord> visitorRecordList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buildingID);
            pstmt.setTimestamp(2, new java.sql.Timestamp(startDate.getTime()));
            pstmt.setTimestamp(3, new java.sql.Timestamp(endDate.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                visitorRecordList.add(mapResultSetToVisitorRecord(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return visitorRecordList;
    }
    
    /**
     * 查询未同步至安保处的访客记录
     * @param buildingID 楼栋ID
     * @return List<VisitorRecord> 访客记录列表
     * @throws SQLException 数据库操作异常
     */
    public List<VisitorRecord> findUnsyncedRecords(int buildingID) throws SQLException {
        String sql = "SELECT vr.* FROM VisitorRecord vr " +
                "JOIN User u ON vr.BookerUserID = u.UserID " +
                "WHERE u.BuildingID=? AND vr.SyncedToSecurity=FALSE " +
                "ORDER BY vr.BookTime DESC";
        List<VisitorRecord> visitorRecordList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buildingID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                visitorRecordList.add(mapResultSetToVisitorRecord(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return visitorRecordList;
    }
    
    /**
     * 查询所有访客记录
     * @return List<VisitorRecord> 访客记录列表
     * @throws SQLException 数据库操作异常
     */
    public List<VisitorRecord> findAll() throws SQLException {
        String sql = "SELECT * FROM VisitorRecord ORDER BY BookTime DESC";
        List<VisitorRecord> visitorRecordList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                visitorRecordList.add(mapResultSetToVisitorRecord(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return visitorRecordList;
    }
    
    /**
     * 将ResultSet映射为VisitorRecord对象
     * @param rs ResultSet对象
     * @return VisitorRecord 访客记录对象
     * @throws SQLException 数据库操作异常
     */
    private VisitorRecord mapResultSetToVisitorRecord(ResultSet rs) throws SQLException {
        VisitorRecord visitorRecord = new VisitorRecord();
        visitorRecord.setVisitorID(rs.getInt("VisitorID"));
        visitorRecord.setBookerUserID(rs.getInt("BookerUserID"));
        visitorRecord.setVisitorName(rs.getString("VisitorName"));
        visitorRecord.setLicensePlate(rs.getString("LicensePlate"));
        visitorRecord.setBookTime(rs.getTimestamp("BookTime"));
        visitorRecord.setSyncedToSecurity(rs.getBoolean("SyncedToSecurity"));
        visitorRecord.setCreateTime(rs.getTimestamp("CreateTime"));
        visitorRecord.setUpdateTime(rs.getTimestamp("UpdateTime"));
        return visitorRecord;
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