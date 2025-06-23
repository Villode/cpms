package com.cpms.controller.owner;

import com.cpms.model.dao.ParkingSpotDAO;
import com.cpms.model.entity.ParkingSpot;

import java.sql.SQLException;
import java.util.List;

/**
 * 业主车位控制器
 * 处理业主车位相关的业务逻辑
 */
public class ParkingController {
    
    /**
     * 获取业主的车位信息
     * @param ownerUserID 业主ID
     * @return 车位列表
     */
    public List<ParkingSpot> getOwnerParkingSpots(int ownerUserID) {
        ParkingSpotDAO parkingSpotDAO = null;
        try {
            parkingSpotDAO = new ParkingSpotDAO();
            return parkingSpotDAO.findByOwnerUserID(ownerUserID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (parkingSpotDAO != null) {
                parkingSpotDAO.close();
            }
        }
    }
    
    /**
     * 更新车位车牌号
     * @param parkingID 车位ID
     * @param licensePlate 新车牌号
     * @return 是否成功
     */
    public boolean updateLicensePlate(int parkingID, String licensePlate) {
        if (licensePlate == null) {
            licensePlate = "";
        }
        
        ParkingSpotDAO parkingSpotDAO = null;
        try {
            parkingSpotDAO = new ParkingSpotDAO();
            
            // 获取当前车位信息
            ParkingSpot parkingSpot = parkingSpotDAO.findByID(parkingID);
            if (parkingSpot == null) {
                return false;
            }
            
            // 更新车牌号
            parkingSpot.setLicensePlate(licensePlate.trim());
            
            // 保存更新
            return parkingSpotDAO.updateParkingSpot(parkingSpot);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (parkingSpotDAO != null) {
                parkingSpotDAO.close();
            }
        }
    }
    
    /**
     * 验证车牌号格式
     * @param licensePlate 车牌号
     * @return 是否有效
     */
    public boolean isValidLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return true; // 允许空车牌号
        }
        
        String plate = licensePlate.trim();
        
        // 基本长度检查
        if (plate.length() < 6 || plate.length() > 8) {
            return false;
        }
        
        // 简单的车牌号格式检查（支持新能源车牌）
        // 普通车牌：省份简称+字母+5位数字或字母
        // 新能源车牌：省份简称+字母+6位数字或字母
        return plate.matches("^[\u4e00-\u9fa5][A-Z][A-Z0-9]{5,6}$");
    }
    
    /**
     * 获取车位详细信息
     * @param parkingID 车位ID
     * @return 车位信息
     */
    public ParkingSpot getParkingSpotDetails(int parkingID) {
        ParkingSpotDAO parkingSpotDAO = null;
        try {
            parkingSpotDAO = new ParkingSpotDAO();
            return parkingSpotDAO.findByID(parkingID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (parkingSpotDAO != null) {
                parkingSpotDAO.close();
            }
        }
    }
}