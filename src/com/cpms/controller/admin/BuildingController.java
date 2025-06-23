package com.cpms.controller.admin;

import com.cpms.model.dao.BuildingDAO;
import com.cpms.model.dao.UserDAO;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 楼栋管理控制器
 * 处理楼栋相关的业务逻辑
 */
public class BuildingController {
    
    /**
     * 获取所有楼栋列表
     * @return 楼栋列表
     */
    public List<Building> getAllBuildings() {
        BuildingDAO buildingDAO = null;
        try {
            buildingDAO = new BuildingDAO();
            return buildingDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (buildingDAO != null) {
                buildingDAO.close();
            }
        }
    }
    
    /**
     * 根据ID获取楼栋
     * @param buildingID 楼栋ID
     * @return 楼栋对象
     */
    public Building getBuildingByID(int buildingID) {
        if (buildingID <= 0) {
            return null;
        }
        
        BuildingDAO buildingDAO = null;
        try {
            buildingDAO = new BuildingDAO();
            return buildingDAO.findByID(buildingID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (buildingDAO != null) {
                buildingDAO.close();
            }
        }
    }
    
    /**
     * 添加楼栋
     * @param buildingName 楼栋名称
     * @param buildingCode 楼栋编码
     * @param address 楼栋地址
     * @param totalFloors 总楼层数
     * @param totalUnits 总单元数
     * @param totalRooms 总房间数
     * @param managerID 管家ID
     * @return 是否成功
     */
    public boolean addBuilding(String buildingName, String buildingCode, String address, 
                              int totalFloors, int totalUnits, int totalRooms, int managerID) {
        if (buildingName == null || buildingName.trim().isEmpty() || 
            buildingCode == null || buildingCode.trim().isEmpty() ||
            address == null || address.trim().isEmpty() ||
            totalFloors <= 0 || totalUnits <= 0 || totalRooms <= 0) {
            return false;
        }
        
        BuildingDAO buildingDAO = null;
        UserDAO userDAO = null;
        try {
            buildingDAO = new BuildingDAO();
            userDAO = new UserDAO();
            
            // 检查楼栋编码是否已存在
            Building existingBuilding = buildingDAO.findByCode(buildingCode);
            if (existingBuilding != null) {
                return false;
            }
            
            // 检查管家是否存在
            if (managerID > 0) {
                User manager = userDAO.findByID(managerID);
                if (manager == null || manager.getRoleID() != 2) { // 假设角色ID 2是管家角色
                    return false;
                }
            }
            
            // 创建新楼栋
            Building building = new Building();
            building.setBuildingName(buildingName);
            building.setBuildingCode(buildingCode);
            building.setAddress(address);
            building.setTotalFloors(totalFloors);
            building.setTotalUnits(totalUnits);
            building.setTotalRooms(totalRooms);
            building.setManagerID(managerID);
            
            return buildingDAO.addBuilding(building);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (buildingDAO != null) {
                buildingDAO.close();
            }
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
    
    /**
     * 更新楼栋
     * @param buildingID 楼栋ID
     * @param buildingName 楼栋名称
     * @param buildingCode 楼栋编码
     * @param address 楼栋地址
     * @param totalFloors 总楼层数
     * @param totalUnits 总单元数
     * @param totalRooms 总房间数
     * @param managerID 管家ID
     * @return 是否成功
     */
    public boolean updateBuilding(int buildingID, String buildingName, String buildingCode, String address, 
                                 int totalFloors, int totalUnits, int totalRooms, int managerID) {
        if (buildingID <= 0 || buildingName == null || buildingName.trim().isEmpty() || 
            buildingCode == null || buildingCode.trim().isEmpty() ||
            address == null || address.trim().isEmpty() ||
            totalFloors <= 0 || totalUnits <= 0 || totalRooms <= 0) {
            return false;
        }
        
        BuildingDAO buildingDAO = null;
        UserDAO userDAO = null;
        try {
            buildingDAO = new BuildingDAO();
            userDAO = new UserDAO();
            
            // 检查楼栋是否存在
            Building existingBuilding = buildingDAO.findByID(buildingID);
            if (existingBuilding == null) {
                return false;
            }
            
            // 检查楼栋编码是否与其他楼栋重复
            Building codeExistingBuilding = buildingDAO.findByCode(buildingCode);
            if (codeExistingBuilding != null && codeExistingBuilding.getBuildingID() != buildingID) {
                return false;
            }
            
            // 检查管家是否存在
            if (managerID > 0) {
                User manager = userDAO.findByID(managerID);
                if (manager == null || manager.getRoleID() != 2) { // 假设角色ID 2是管家角色
                    return false;
                }
            }
            
            // 更新楼栋
            existingBuilding.setBuildingName(buildingName);
            existingBuilding.setBuildingCode(buildingCode);
            existingBuilding.setAddress(address);
            existingBuilding.setTotalFloors(totalFloors);
            existingBuilding.setTotalUnits(totalUnits);
            existingBuilding.setTotalRooms(totalRooms);
            existingBuilding.setManagerID(managerID);
            
            return buildingDAO.updateBuilding(existingBuilding);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (buildingDAO != null) {
                buildingDAO.close();
            }
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
    
    /**
     * 删除楼栋
     * @param buildingID 楼栋ID
     * @return 是否成功
     */
    public boolean deleteBuilding(int buildingID) {
        if (buildingID <= 0) {
            return false;
        }
        
        BuildingDAO buildingDAO = null;
        try {
            buildingDAO = new BuildingDAO();
            
            // 检查楼栋是否存在
            Building existingBuilding = buildingDAO.findByID(buildingID);
            if (existingBuilding == null) {
                return false;
            }
            
            // 删除楼栋
            return buildingDAO.deleteBuilding(buildingID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (buildingDAO != null) {
                buildingDAO.close();
            }
        }
    }
    
    /**
     * 分配管家
     * @param buildingID 楼栋ID
     * @param managerID 管家ID
     * @return 是否成功
     */
    public boolean assignManager(int buildingID, int managerID) {
        if (buildingID <= 0 || managerID <= 0) {
            return false;
        }
        
        BuildingDAO buildingDAO = null;
        UserDAO userDAO = null;
        try {
            buildingDAO = new BuildingDAO();
            userDAO = new UserDAO();
            
            // 检查楼栋是否存在
            Building existingBuilding = buildingDAO.findByID(buildingID);
            if (existingBuilding == null) {
                return false;
            }
            
            // 检查管家是否存在
            User manager = userDAO.findByID(managerID);
            if (manager == null || manager.getRoleID() != 2) { // 假设角色ID 2是管家角色
                return false;
            }
            
            // 更新楼栋的管家ID
            existingBuilding.setManagerID(managerID);
            
            return buildingDAO.updateBuilding(existingBuilding);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (buildingDAO != null) {
                buildingDAO.close();
            }
            if (userDAO != null) {
                userDAO.close();
            }
        }
    }
    
    /**
     * 获取楼栋的管家信息
     * @param buildingID 楼栋ID
     * @return 管家用户对象
     */
    public User getBuildingManager(int buildingID) {
        if (buildingID <= 0) {
            return null;
        }
        
        BuildingDAO buildingDAO = null;
        try {
            buildingDAO = new BuildingDAO();
            return buildingDAO.findManagerByBuildingID(buildingID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (buildingDAO != null) {
                buildingDAO.close();
            }
        }
    }
    
    /**
     * 获取管家负责的所有楼栋
     * @param managerID 管家ID
     * @return 楼栋列表
     */
    public List<Building> getBuildingsByManagerID(int managerID) {
        if (managerID <= 0) {
            return new ArrayList<>();
        }
        
        BuildingDAO buildingDAO = null;
        try {
            buildingDAO = new BuildingDAO();
            return buildingDAO.findByManagerID(managerID);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (buildingDAO != null) {
                buildingDAO.close();
            }
        }
    }
} 