package com.cpms.model.dao;

import com.cpms.model.entity.Repair;
import com.cpms.util.db.DatabaseConnection;
import com.cpms.util.db.DatabaseUtil;
import com.cpms.util.constants.SQLConstants;
import com.cpms.util.log.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 报修数据访问对象类
 * 提供对Repair表的增删改查操作
 */
public class RepairDAO {
    private DatabaseConnection dbc = null;
    private Connection conn = null;
    
    /**
     * 构造方法，初始化数据库连接
     * @throws SQLException 数据库连接异常
     */
    public RepairDAO() throws SQLException {
        dbc = new DatabaseConnection();
        conn = dbc.getConnection();
    }
    
    /**
     * 添加报修
     * @param repair 报修对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean addRepair(Repair repair) throws SQLException {
        try {
            int result = DatabaseUtil.executeUpdate(conn, SQLConstants.Repair.INSERT, 
                    repair.getUserID(), 
                    repair.getBuildingID(), 
                    repair.getRoomNumber(),
                    repair.getRepairType(), 
                    repair.getRepairDesc(), 
                    repair.getRepairStatus());
            Logger.info("报修添加操作完成，影响行数: " + result);
            return result > 0;
        } catch (SQLException e) {
            Logger.error("添加报修失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 插入报修记录（addRepair方法的别名）
     * @param repair 报修对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean insertRepair(Repair repair) throws SQLException {
        return addRepair(repair);
    }
    
    /**
     * 更新报修
     * @param repair 报修对象
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updateRepair(Repair repair) throws SQLException {
        try {
            int result = DatabaseUtil.executeUpdate(conn, SQLConstants.Repair.UPDATE, 
                    repair.getBuildingID(), 
                    repair.getRoomNumber(),
                    repair.getRepairType(), 
                    repair.getRepairDesc(), 
                    repair.getRepairStatus(),
                    repair.getHandlerID(),
                    repair.getHandleOpinion(),
                    repair.getRepairID());
            Logger.info("报修更新操作完成，影响行数: " + result);
            return result > 0;
        } catch (SQLException e) {
            Logger.error("更新报修失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 更新报修状态
     * @param repairID 报修ID
     * @param status 报修状态
     * @param handlerID 处理人ID
     * @param handleOpinion 处理意见
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean updateRepairStatus(int repairID, int status, int handlerID, String handleOpinion) throws SQLException {
        try {
            int result = DatabaseUtil.executeUpdate(conn, SQLConstants.Repair.UPDATE_STATUS, status, handlerID, handleOpinion, repairID);
            Logger.info("报修状态更新操作完成，影响行数: " + result);
            return result > 0;
        } catch (SQLException e) {
            Logger.error("更新报修状态失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 删除报修
     * @param repairID 报修ID
     * @return boolean 是否成功
     * @throws SQLException 数据库操作异常
     */
    public boolean deleteRepair(int repairID) throws SQLException {
        String sql = "DELETE FROM repair WHERE RepairID=?";
        int result = DatabaseUtil.executeUpdate(conn, sql, repairID);
        return result > 0;
    }
    
    /**
     * 根据ID查找报修
     * @param repairID 报修ID
     * @return Repair 报修对象
     * @throws SQLException 数据库操作异常
     */
    public Repair findByID(int repairID) throws SQLException {
        String sql = "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
                     "FROM repair r " +
                     "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
                     "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
                     "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
                     "WHERE r.RepairID=?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, repairID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToRepair(rs);
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return null;
    }
    
    /**
     * 查询所有报修
     * @return List<Repair> 报修列表
     * @throws SQLException 数据库操作异常
     */
    public List<Repair> findAll() throws SQLException {
        String sql = "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
                     "FROM repair r " +
                     "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
                     "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
                     "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
                     "ORDER BY r.CreateTime DESC";
        List<Repair> repairList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                repairList.add(mapResultSetToRepair(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return repairList;
    }
    
    /**
     * 根据用户ID查询报修
     * @param userID 用户ID
     * @return List<Repair> 报修列表
     * @throws SQLException 数据库操作异常
     */
    public List<Repair> findByUserID(int userID) throws SQLException {
        String sql = "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
                     "FROM repair r " +
                     "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
                     "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
                     "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
                     "WHERE r.UserID=? " +
                     "ORDER BY r.CreateTime DESC";
        List<Repair> repairList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                repairList.add(mapResultSetToRepair(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return repairList;
    }
    
    /**
     * 根据楼栋ID查询报修
     * @param buildingID 楼栋ID
     * @return List<Repair> 报修列表
     * @throws SQLException 数据库操作异常
     */
    public List<Repair> findByBuildingID(int buildingID) throws SQLException {
        String sql = "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
                     "FROM repair r " +
                     "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
                     "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
                     "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
                     "WHERE r.BuildingID=? " +
                     "ORDER BY r.CreateTime DESC";
        List<Repair> repairList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buildingID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                repairList.add(mapResultSetToRepair(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return repairList;
    }
    
    /**
     * 根据处理人ID查询报修
     * @param handlerID 处理人ID
     * @return List<Repair> 报修列表
     * @throws SQLException 数据库操作异常
     */
    public List<Repair> findByHandlerID(int handlerID) throws SQLException {
        String sql = "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
                     "FROM repair r " +
                     "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
                     "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
                     "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
                     "WHERE r.HandlerID=? " +
                     "ORDER BY r.CreateTime DESC";
        List<Repair> repairList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, handlerID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                repairList.add(mapResultSetToRepair(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return repairList;
    }
    
    /**
     * 根据报修状态查询报修
     * @param status 报修状态
     * @return List<Repair> 报修列表
     * @throws SQLException 数据库操作异常
     */
    public List<Repair> findByStatus(int status) throws SQLException {
        String sql = "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
                     "FROM repair r " +
                     "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
                     "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
                     "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
                     "WHERE r.RepairStatus=? " +
                     "ORDER BY r.CreateTime DESC";
        List<Repair> repairList = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                repairList.add(mapResultSetToRepair(rs));
            }
        } finally {
            DatabaseUtil.closeResources(rs, pstmt);
        }
        return repairList;
    }
    
    /**
     * 将ResultSet映射为Repair对象
     * @param rs ResultSet对象
     * @return Repair 报修对象
     * @throws SQLException 数据库操作异常
     */
    private Repair mapResultSetToRepair(ResultSet rs) throws SQLException {
        Repair repair = new Repair();
        repair.setRepairID(rs.getInt("RepairID"));
        repair.setUserID(rs.getInt("UserID"));
        repair.setBuildingID(rs.getInt("BuildingID"));
        repair.setRoomNumber(rs.getString("RoomNumber"));
        repair.setRepairType(rs.getInt("RepairType"));
        repair.setRepairDesc(rs.getString("RepairDesc"));
        repair.setRepairStatus(rs.getInt("RepairStatus"));
        repair.setHandlerID(rs.getInt("HandlerID"));
        repair.setHandleOpinion(rs.getString("HandleOpinion"));
        repair.setCreateTime(rs.getTimestamp("CreateTime"));
        repair.setUpdateTime(rs.getTimestamp("UpdateTime"));
        
        // 设置额外属性
        repair.setUserName(rs.getString("UserName"));
        repair.setBuildingName(rs.getString("BuildingName"));
        repair.setHandlerName(rs.getString("HandlerName"));
        
        return repair;
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