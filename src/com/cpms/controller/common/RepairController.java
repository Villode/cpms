package com.cpms.controller.common;

import com.cpms.model.dao.BuildingDAO;
import com.cpms.model.dao.RepairDAO;
import com.cpms.model.dao.UserDAO;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.Repair;
import com.cpms.model.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 报修控制器
 * 处理报修相关的业务逻辑
 */
public class RepairController {
    
    /**
     * 提交报修
     * @param userID 用户ID
     * @param buildingID 楼栋ID
     * @param roomNumber 房间号
     * @param repairType 报修类型
     * @param repairDesc 报修描述
     * @return boolean 是否成功
     */
    public boolean submitRepair(int userID, int buildingID, String roomNumber, int repairType, String repairDesc) {
        if (userID <= 0 || buildingID <= 0 || roomNumber == null || roomNumber.trim().isEmpty() || 
            repairType <= 0 || repairType > 4 || repairDesc == null || repairDesc.trim().isEmpty()) {
            return false;
        }
        
        RepairDAO repairDAO = null;
        UserDAO userDAO = null;
        BuildingDAO buildingDAO = null;
        
        try {
            repairDAO = new RepairDAO();
            userDAO = new UserDAO();
            buildingDAO = new BuildingDAO();
            
            // 检查用户是否存在
            User user = userDAO.findByID(userID);
            if (user == null) {
                return false;
            }
            
            // 检查楼栋是否存在
            Building building = buildingDAO.findByID(buildingID);
            if (building == null) {
                return false;
            }
            
            // 创建报修对象
            Repair repair = new Repair();
            repair.setUserID(userID);
            repair.setBuildingID(buildingID);
            repair.setRoomNumber(roomNumber);
            repair.setRepairType(repairType);
            repair.setRepairDesc(repairDesc);
            repair.setRepairStatus(0); // 初始状态为待处理
            
            // 添加报修
            return repairDAO.addRepair(repair);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(repairDAO, userDAO, buildingDAO);
        }
    }
    
    /**
     * 处理报修
     * @param repairID 报修ID
     * @param handlerID 处理人ID
     * @param status 报修状态
     * @param handleOpinion 处理意见
     * @return boolean 是否成功
     */
    public boolean handleRepair(int repairID, int handlerID, int status, String handleOpinion) {
        if (repairID <= 0 || handlerID <= 0 || status < 0 || status > 3) {
            return false;
        }
        
        RepairDAO repairDAO = null;
        UserDAO userDAO = null;
        
        try {
            repairDAO = new RepairDAO();
            userDAO = new UserDAO();
            
            // 检查报修是否存在
            Repair repair = repairDAO.findByID(repairID);
            if (repair == null) {
                return false;
            }
            
            // 检查处理人是否存在
            User handler = userDAO.findByID(handlerID);
            if (handler == null) {
                return false;
            }
            
            // 更新报修状态
            return repairDAO.updateRepairStatus(repairID, status, handlerID, handleOpinion);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(repairDAO, userDAO, null);
        }
    }
    
    /**
     * 取消报修
     * @param repairID 报修ID
     * @param userID 用户ID
     * @return boolean 是否成功
     */
    public boolean cancelRepair(int repairID, int userID) {
        if (repairID <= 0 || userID <= 0) {
            return false;
        }
        
        RepairDAO repairDAO = null;
        
        try {
            repairDAO = new RepairDAO();
            
            // 检查报修是否存在
            Repair repair = repairDAO.findByID(repairID);
            if (repair == null) {
                return false;
            }
            
            // 检查是否是报修人
            if (repair.getUserID() != userID) {
                return false;
            }
            
            // 检查状态是否允许取消
            if (repair.getRepairStatus() == 2 || repair.getRepairStatus() == 3) {
                return false; // 已完成或已取消的报修不能取消
            }
            
            // 更新报修状态为已取消
            return repairDAO.updateRepairStatus(repairID, 3, 0, "用户取消报修");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(repairDAO, null, null);
        }
    }
    
    /**
     * 获取所有报修
     * @return List<Repair> 报修列表
     */
    public List<Repair> getAllRepairs() {
        RepairDAO repairDAO = null;
        
        try {
            repairDAO = new RepairDAO();
            return repairDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            closeResources(repairDAO, null, null);
        }
    }
    
    /**
     * 获取用户的报修
     * @param userID 用户ID
     * @return List<Repair> 报修列表
     */
    public List<Repair> getRepairsByUserID(int userID) {
        if (userID <= 0) {
            return new ArrayList<>();
        }
        
        RepairDAO repairDAO = null;
        
        try {
            repairDAO = new RepairDAO();
            return repairDAO.findByUserID(userID);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            closeResources(repairDAO, null, null);
        }
    }
    
    /**
     * 获取楼栋的报修
     * @param buildingID 楼栋ID
     * @return List<Repair> 报修列表
     */
    public List<Repair> getRepairsByBuildingID(int buildingID) {
        if (buildingID <= 0) {
            return new ArrayList<>();
        }
        
        RepairDAO repairDAO = null;
        
        try {
            repairDAO = new RepairDAO();
            return repairDAO.findByBuildingID(buildingID);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            closeResources(repairDAO, null, null);
        }
    }
    
    /**
     * 获取处理人的报修
     * @param handlerID 处理人ID
     * @return List<Repair> 报修列表
     */
    public List<Repair> getRepairsByHandlerID(int handlerID) {
        if (handlerID <= 0) {
            return new ArrayList<>();
        }
        
        RepairDAO repairDAO = null;
        
        try {
            repairDAO = new RepairDAO();
            return repairDAO.findByHandlerID(handlerID);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            closeResources(repairDAO, null, null);
        }
    }
    
    /**
     * 获取指定状态的报修
     * @param status 报修状态
     * @return List<Repair> 报修列表
     */
    public List<Repair> getRepairsByStatus(int status) {
        if (status < 0 || status > 3) {
            return new ArrayList<>();
        }
        
        RepairDAO repairDAO = null;
        
        try {
            repairDAO = new RepairDAO();
            return repairDAO.findByStatus(status);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            closeResources(repairDAO, null, null);
        }
    }
    
    /**
     * 关闭资源
     * @param repairDAO 报修DAO
     * @param userDAO 用户DAO
     * @param buildingDAO 楼栋DAO
     */
    private void closeResources(RepairDAO repairDAO, UserDAO userDAO, BuildingDAO buildingDAO) {
        if (repairDAO != null) {
            repairDAO.close();
        }
        if (userDAO != null) {
            userDAO.close();
        }
        if (buildingDAO != null) {
            buildingDAO.close();
        }
    }
} 