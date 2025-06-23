package com.cpms.model.dao;

import com.cpms.model.entity.Building;
import com.cpms.model.entity.User;
import com.cpms.util.db.DatabaseConnection;
import com.cpms.util.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 楼栋数据访问对象类
 * 提供对Building表的增删改查操作
 */
public class BuildingDAO {
    private DatabaseConnection dbc = null;
    private Connection conn = null;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public BuildingDAO() throws SQLException {
        dbc = new DatabaseConnection();
        conn = dbc.getConnection();
    }
    
    /**
     * 添加楼栋
     * @param building 楼栋对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean addBuilding(Building building) throws SQLException {
        // 如果楼栋ID已设置，使用指定的ID插入
        if (building.getBuildingID() > 0) {
            String sql = "INSERT INTO building(BuildingID, BuildingName, BuildingCode, Address, TotalFloors, TotalUnits, TotalRooms, ManagerID) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            int result = DatabaseUtil.executeUpdate(conn, sql, 
                    building.getBuildingID(), 
                    building.getBuildingName(), 
                    building.getBuildingCode(), 
                    building.getAddress(), 
                    building.getTotalFloors(), 
                    building.getTotalUnits(), 
                    building.getTotalRooms(), 
                    building.getManagerID());
            return result > 0;
        } else {
            // 否则使用自动递增ID
            String sql = "INSERT INTO building(BuildingName, BuildingCode, Address, TotalFloors, TotalUnits, TotalRooms, ManagerID) VALUES(?, ?, ?, ?, ?, ?, ?)";
            int result = DatabaseUtil.executeUpdate(conn, sql, 
                    building.getBuildingName(), 
                    building.getBuildingCode(), 
                    building.getAddress(), 
                    building.getTotalFloors(), 
                    building.getTotalUnits(), 
                    building.getTotalRooms(), 
                    building.getManagerID());
            return result > 0;
        }
    }
    
    /**
     * 更新楼栋
     * @param building 楼栋对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updateBuilding(Building building) throws SQLException {
        String sql = "UPDATE building SET BuildingName=?, BuildingCode=?, Address=?, TotalFloors=?, TotalUnits=?, TotalRooms=?, ManagerID=? WHERE BuildingID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, 
                building.getBuildingName(), 
                building.getBuildingCode(), 
                building.getAddress(), 
                building.getTotalFloors(), 
                building.getTotalUnits(), 
                building.getTotalRooms(), 
                building.getManagerID(), 
                building.getBuildingID());
        return result > 0;
    }
    
    /**
     * 根据ID删除楼栋
     * @param buildingID 楼栋ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deleteBuilding(int buildingID) throws SQLException {
        String sql = "DELETE FROM building WHERE BuildingID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, buildingID);
        return result > 0;
    }
    
    /**
     * 根据ID查找楼栋
     * @param buildingID 楼栋ID
     * @return Building 楼栋对象
     * @throws SQLException 数据库操作异常
     */
    public Building findByID(int buildingID) throws SQLException {
        String sql = "SELECT * FROM building WHERE BuildingID=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buildingID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBuilding(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 根据楼栋编码查找楼栋
     * @param buildingCode 楼栋编码
     * @return Building 楼栋对象
     * @throws SQLException 数据库操作异常
     */
    public Building findByCode(String buildingCode) throws SQLException {
        String sql = "SELECT * FROM building WHERE BuildingCode=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, buildingCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBuilding(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 根据楼栋名称查找楼栋
     * @param buildingName 楼栋名称
     * @return Building 楼栋对象
     * @throws SQLException 数据库操作异常
     */
    public Building findByName(String buildingName) throws SQLException {
        String sql = "SELECT * FROM building WHERE BuildingName=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, buildingName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBuilding(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 查询所有楼栋
     * @return List<Building> 楼栋列表
     * @throws SQLException 数据库操作异常
     */
    public List<Building> findAll() throws SQLException {
        String sql = "SELECT * FROM building ORDER BY BuildingID ASC";
        List<Building> buildingList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                buildingList.add(mapResultSetToBuilding(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return buildingList;
    }
    
    /**
     * 根据管家ID查询楼栋
     * @param managerID 管家ID
     * @return List<Building> 楼栋列表
     * @throws SQLException 数据库操作异常
     */
    public List<Building> findByManagerID(int managerID) throws SQLException {
        String sql = "SELECT * FROM building WHERE ManagerID=? ORDER BY BuildingID ASC";
        List<Building> buildingList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, managerID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                buildingList.add(mapResultSetToBuilding(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return buildingList;
    }
    
    /**
     * 查询楼栋的管家信息
     * @param buildingID 楼栋ID
     * @return User 管家用户对象
     * @throws SQLException 数据库操作异常
     */
    public User findManagerByBuildingID(int buildingID) throws SQLException {
        String sql = "SELECT u.* FROM user u JOIN building b ON u.UserID = b.ManagerID WHERE b.BuildingID=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buildingID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                User manager = new User();
                manager.setUserID(rs.getInt("UserID"));
                manager.setUsername(rs.getString("Username"));
                manager.setRealName(rs.getString("RealName"));
                manager.setPhone(rs.getString("Phone"));
                manager.setRoleID(rs.getInt("RoleID"));
                manager.setCreateTime(rs.getTimestamp("CreateTime"));
                manager.setUpdateTime(rs.getTimestamp("UpdateTime"));
                return manager;
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 将ResultSet映射为Building对象
     * @param rs ResultSet对象
     * @return Building 楼栋对象
     * @throws SQLException 数据库操作异常
     */
    private Building mapResultSetToBuilding(ResultSet rs) throws SQLException {
        Building building = new Building();
        building.setBuildingID(rs.getInt("BuildingID"));
        building.setBuildingName(rs.getString("BuildingName"));
        building.setBuildingCode(rs.getString("BuildingCode"));
        building.setAddress(rs.getString("Address"));
        building.setTotalFloors(rs.getInt("TotalFloors"));
        building.setTotalUnits(rs.getInt("TotalUnits"));
        building.setTotalRooms(rs.getInt("TotalRooms"));
        building.setManagerID(rs.getInt("ManagerID"));
        building.setCreateTime(rs.getTimestamp("CreateTime"));
        building.setUpdateTime(rs.getTimestamp("UpdateTime"));
        return building;
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