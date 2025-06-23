package com.cpms.controller.owner;

import com.cpms.model.dao.RepairDAO;
import com.cpms.model.entity.Repair;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 报修控制器
 * 处理业主报修相关的业务逻辑
 */
public class RepairController {
    
    /**
     * 提交报修
     * @param repair 报修对象
     * @return 是否成功
     */
    public boolean submitRepair(Repair repair) {
        RepairDAO repairDAO = null;
        try {
            repairDAO = new RepairDAO();
            
            // 设置提交时间
            repair.setSubmitTime(new Date());
            
            // 插入报修记录
            return repairDAO.insertRepair(repair);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (repairDAO != null) {
                repairDAO.close();
            }
        }
    }
    
    /**
     * 获取业主的报修记录
     * @param userID 业主ID
     * @return 报修记录列表
     */
    public List<Repair> getRepairsByUserID(int userID) {
        RepairDAO repairDAO = null;
        try {
            repairDAO = new RepairDAO();
            return repairDAO.findByUserID(userID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (repairDAO != null) {
                repairDAO.close();
            }
        }
    }
    
    /**
     * 取消报修
     * @param repairID 报修ID
     * @param userID 业主ID（用于验证权限）
     * @return 是否成功
     */
    public boolean cancelRepair(int repairID, int userID) {
        RepairDAO repairDAO = null;
        try {
            repairDAO = new RepairDAO();
            
            // 查询报修记录
            Repair repair = repairDAO.findByID(repairID);
            if (repair == null) {
                return false;
            }
            
            // 验证权限
            if (repair.getSubmitUserID() != userID) {
                return false;
            }
            
            // 验证状态（只有待处理的报修才能取消）
            if (repair.getStatus() != 0) {
                return false;
            }
            
            // 更新状态为已取消
            repair.setStatus(3); // 3表示已取消
            return repairDAO.updateRepair(repair);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (repairDAO != null) {
                repairDAO.close();
            }
        }
    }
} 