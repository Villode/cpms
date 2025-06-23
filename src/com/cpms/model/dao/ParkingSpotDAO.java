package com.cpms.model.dao;

import com.cpms.model.entity.ParkingSpot;
import com.cpms.util.db.DatabaseConnection;
import com.cpms.util.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 车位数据访问对象类
 * 提供对ParkingSpot表的增删改查操作
 */
public class ParkingSpotDAO {
    private DatabaseConnection dbc = null;
    private Connection conn = null;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public ParkingSpotDAO() throws SQLException {
        dbc = new DatabaseConnection();
        conn = dbc.getConnection();
    }
    
    /**
     * 添加车位
     * @param parkingSpot 车位对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean addParkingSpot(ParkingSpot parkingSpot) throws SQLException {
        String sql = "INSERT INTO ParkingSpot(BuildingID, SpotNumber, Location, OwnerUserID, LicensePlate, UsageStatus) VALUES(?, ?, ?, ?, ?, ?)";
        int result = DatabaseUtil.executeUpdate(conn, sql, 
                parkingSpot.getBuildingID(), 
                parkingSpot.getSpotNumber(), 
                parkingSpot.getLocation(), 
                parkingSpot.getOwnerUserID(), 
                parkingSpot.getLicensePlate(), 
                parkingSpot.getUsageStatus());
        return result > 0;
    }
    
    /**
     * 更新车位信息
     * @param parkingSpot 车位对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updateParkingSpot(ParkingSpot parkingSpot) throws SQLException {
        String sql = "UPDATE ParkingSpot SET BuildingID=?, SpotNumber=?, Location=?, OwnerUserID=?, LicensePlate=?, UsageStatus=? WHERE ParkingID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, 
                parkingSpot.getBuildingID(), 
                parkingSpot.getSpotNumber(), 
                parkingSpot.getLocation(), 
                parkingSpot.getOwnerUserID(), 
                parkingSpot.getLicensePlate(), 
                parkingSpot.getUsageStatus(), 
                parkingSpot.getParkingID());
        return result > 0;
    }
    
    /**
     * 更新车位使用状态
     * @param parkingID 车位ID
     * @param ownerUserID 业主ID
     * @param licensePlate 车牌号
     * @param usageStatus 使用状态
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updateUsageStatus(int parkingID, Integer ownerUserID, String licensePlate, int usageStatus) throws SQLException {
        String sql = "UPDATE ParkingSpot SET OwnerUserID=?, LicensePlate=?, UsageStatus=? WHERE ParkingID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, ownerUserID, licensePlate, usageStatus, parkingID);
        return result > 0;
    }
    
    /**
     * 根据ID删除车位
     * @param parkingID 车位ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deleteParkingSpot(int parkingID) throws SQLException {
        String sql = "DELETE FROM ParkingSpot WHERE ParkingID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, parkingID);
        return result > 0;
    }
    
    /**
     * 根据ID查找车位
     * @param parkingID 车位ID
     * @return ParkingSpot 车位对象
     * @throws SQLException 数据库操作异常
     */
    public ParkingSpot findByID(int parkingID) throws SQLException {
        String sql = "SELECT * FROM ParkingSpot WHERE ParkingID=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, parkingID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToParkingSpot(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 根据楼栋ID查找车位
     * @param buildingID 楼栋ID
     * @return List<ParkingSpot> 车位列表
     * @throws SQLException 数据库操作异常
     */
    public List<ParkingSpot> findByBuildingID(int buildingID) throws SQLException {
        String sql = "SELECT * FROM ParkingSpot WHERE BuildingID=? ORDER BY SpotNumber ASC";
        List<ParkingSpot> parkingSpotList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buildingID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                parkingSpotList.add(mapResultSetToParkingSpot(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return parkingSpotList;
    }
    
    /**
     * 根据业主ID查找车位
     * @param ownerUserID 业主ID
     * @return List<ParkingSpot> 车位列表
     * @throws SQLException 数据库操作异常
     */
    public List<ParkingSpot> findByOwnerUserID(int ownerUserID) throws SQLException {
        String sql = "SELECT * FROM ParkingSpot WHERE OwnerUserID=? ORDER BY SpotNumber ASC";
        List<ParkingSpot> parkingSpotList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ownerUserID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                parkingSpotList.add(mapResultSetToParkingSpot(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return parkingSpotList;
    }
    
    /**
     * 根据使用状态查找车位
     * @param buildingID 楼栋ID
     * @param usageStatus 使用状态
     * @return List<ParkingSpot> 车位列表
     * @throws SQLException 数据库操作异常
     */
    public List<ParkingSpot> findByUsageStatus(int buildingID, int usageStatus) throws SQLException {
        String sql = "SELECT * FROM ParkingSpot WHERE BuildingID=? AND UsageStatus=? ORDER BY SpotNumber ASC";
        List<ParkingSpot> parkingSpotList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buildingID);
            pstmt.setInt(2, usageStatus);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                parkingSpotList.add(mapResultSetToParkingSpot(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return parkingSpotList;
    }
    
    /**
     * 查询所有车位
     * @return List<ParkingSpot> 车位列表
     * @throws SQLException 数据库操作异常
     */
    public List<ParkingSpot> findAll() throws SQLException {
        String sql = "SELECT * FROM ParkingSpot ORDER BY BuildingID, SpotNumber ASC";
        List<ParkingSpot> parkingSpotList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                parkingSpotList.add(mapResultSetToParkingSpot(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return parkingSpotList;
    }
    
    /**
     * 将ResultSet映射为ParkingSpot对象
     * @param rs ResultSet对象
     * @return ParkingSpot 车位对象
     * @throws SQLException 数据库操作异常
     */
    private ParkingSpot mapResultSetToParkingSpot(ResultSet rs) throws SQLException {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setParkingID(rs.getInt("ParkingID"));
        parkingSpot.setBuildingID(rs.getInt("BuildingID"));
        parkingSpot.setSpotNumber(rs.getString("SpotNumber"));
        parkingSpot.setLocation(rs.getString("Location"));
        
        int ownerUserID = rs.getInt("OwnerUserID");
        if (!rs.wasNull()) {
            parkingSpot.setOwnerUserID(ownerUserID);
        }
        
        parkingSpot.setLicensePlate(rs.getString("LicensePlate"));
        parkingSpot.setUsageStatus(rs.getInt("UsageStatus"));
        parkingSpot.setCreateTime(rs.getTimestamp("CreateTime"));
        parkingSpot.setUpdateTime(rs.getTimestamp("UpdateTime"));
        return parkingSpot;
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