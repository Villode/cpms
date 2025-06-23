package com.cpms.view.manager;

import com.cpms.controller.admin.BuildingController;
import com.cpms.controller.common.RepairController;
import com.cpms.model.dao.UserDAO;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.Repair;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报修查询面板
 * 物业管家查询和统计报修信息的界面
 */
public class RepairQueryPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private RepairController repairController;
    private BuildingController buildingController;
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTable repairTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilterComboBox;
    private JComboBox<String> typeFilterComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel totalCountLabel;
    private JLabel pendingCountLabel;
    private JLabel processingCountLabel;
    private JLabel completedCountLabel;
    private JLabel canceledCountLabel;
    
    // 数据
    private List<Repair> repairList;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法，初始化报修查询面板
     * @param user 当前用户
     */
    public RepairQueryPanel(User user) {
        this.currentUser = user;
        
        // 初始化控制器
        repairController = new RepairController();
        buildingController = new BuildingController();
        
        // 设置布局
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 创建界面组件
        initComponents();
        
        // 加载数据
        loadRepairs();
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建顶部过滤面板
        JPanel filterPanel = createFilterPanel();
        
        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        
        // 创建统计面板
        JPanel statsPanel = createStatsPanel();
        
        // 添加到主面板
        add(filterPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建过滤面板
     * @return 过滤面板
     */
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // 状态过滤
        JLabel statusLabel = new JLabel("报修状态:");
        statusLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        statusFilterComboBox = new JComboBox<>(new String[]{"全部", "待处理", "处理中", "已完成", "已取消"});
        statusFilterComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 类型过滤
        JLabel typeLabel = new JLabel("报修类型:");
        typeLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        typeFilterComboBox = new JComboBox<>(new String[]{"全部", "水电维修", "家具维修", "门窗维修", "其他"});
        typeFilterComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 搜索框
        JLabel searchLabel = new JLabel("搜索:");
        searchLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        searchField = new JTextField(15);
        searchField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 搜索按钮
        searchButton = new JButton("搜索");
        searchButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRepairs();
            }
        });
        
        panel.add(statusLabel);
        panel.add(statusFilterComboBox);
        panel.add(typeLabel);
        panel.add(typeFilterComboBox);
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);
        
        return panel;
    }
    
    /**
     * 创建表格面板
     * @return 表格面板
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                "报修列表",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                        config.getIntProperty("ui.font.size.large", 16)),
                PRIMARY_COLOR));
        
        // 创建表格模型
        String[] columnNames = {"ID", "楼栋", "房间号", "报修类型", "报修描述", "状态", "报修人", "处理人", "报修时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };
        
        // 创建表格
        repairTable = new JTable(tableModel);
        repairTable.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        repairTable.setRowHeight(30);
        repairTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        repairTable.getTableHeader().setReorderingAllowed(false);
        repairTable.getTableHeader().setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 设置列宽
        repairTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        repairTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        repairTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        repairTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        repairTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        repairTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        repairTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        repairTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        repairTable.getColumnModel().getColumn(8).setPreferredWidth(150);
        
        // 居中显示
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < repairTable.getColumnCount(); i++) {
            repairTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // 添加到滚动面板
        JScrollPane scrollPane = new JScrollPane(repairTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建统计面板
     * @return 统计面板
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                "报修统计",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                        config.getIntProperty("ui.font.size.large", 16)),
                PRIMARY_COLOR));
        
        // 总数统计
        JLabel totalLabel = new JLabel("总报修数:");
        totalLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        totalCountLabel = new JLabel("0");
        totalCountLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 待处理统计
        JLabel pendingLabel = new JLabel("待处理:");
        pendingLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        pendingCountLabel = new JLabel("0");
        pendingCountLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        pendingCountLabel.setForeground(ACCENT_COLOR);
        
        // 处理中统计
        JLabel processingLabel = new JLabel("处理中:");
        processingLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        processingCountLabel = new JLabel("0");
        processingCountLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        processingCountLabel.setForeground(PRIMARY_COLOR);
        
        // 已完成统计
        JLabel completedLabel = new JLabel("已完成:");
        completedLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        completedCountLabel = new JLabel("0");
        completedCountLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        completedCountLabel.setForeground(SUCCESS_COLOR);
        
        // 已取消统计
        JLabel canceledLabel = new JLabel("已取消:");
        canceledLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        canceledCountLabel = new JLabel("0");
        canceledCountLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        canceledCountLabel.setForeground(ERROR_COLOR);
        
        // 添加组件到面板
        panel.add(totalLabel);
        panel.add(totalCountLabel);
        panel.add(pendingLabel);
        panel.add(pendingCountLabel);
        panel.add(processingLabel);
        panel.add(processingCountLabel);
        panel.add(completedLabel);
        panel.add(completedCountLabel);
        panel.add(canceledLabel);
        panel.add(canceledCountLabel);
        
        return panel;
    }
    
    /**
     * 加载报修数据
     */
    private void loadRepairs() {
        // 清空表格
        tableModel.setRowCount(0);
        
        try {
            // 根据过滤条件获取报修列表
            int statusFilter = statusFilterComboBox.getSelectedIndex() - 1;
            int typeFilter = typeFilterComboBox.getSelectedIndex();
            String searchText = searchField.getText().trim().toLowerCase();
            
            // 获取管家负责的楼栋ID
            Integer managedBuildingID = currentUser.getManagedBuildingID();
            
            // 获取报修列表
            if (statusFilter == -1) {
                if (managedBuildingID != null && managedBuildingID > 0) {
                    repairList = repairController.getRepairsByBuildingID(managedBuildingID);
                } else {
                    repairList = repairController.getAllRepairs();
                }
            } else {
                repairList = repairController.getRepairsByStatus(statusFilter);
                
                // 过滤出当前管家负责的楼栋的报修
                if (managedBuildingID != null && managedBuildingID > 0) {
                    repairList.removeIf(repair -> repair.getBuildingID() != managedBuildingID);
                }
            }
            
            // 根据类型过滤
            if (typeFilter > 0) {
                repairList.removeIf(repair -> repair.getRepairType() != typeFilter);
            }
            
            // 根据搜索文本过滤
            if (!searchText.isEmpty()) {
                repairList.removeIf(repair -> {
                    try {
                        String roomNumber = repair.getRoomNumber().toLowerCase();
                        String repairDesc = repair.getRepairDesc().toLowerCase();
                        String userName = getUserName(repair.getUserID()).toLowerCase();
                        String buildingName = getBuildingName(repair.getBuildingID()).toLowerCase();
                        
                        return !roomNumber.contains(searchText) && 
                               !repairDesc.contains(searchText) && 
                               !userName.contains(searchText) && 
                               !buildingName.contains(searchText);
                    } catch (Exception e) {
                        return true;
                    }
                });
            }
            
            // 填充表格数据
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (Repair repair : repairList) {
                // 获取楼栋名称
                String buildingName = getBuildingName(repair.getBuildingID());
                
                // 获取报修类型名称
                String repairTypeName = getRepairTypeName(repair.getRepairType());
                
                // 获取状态名称
                String statusName = getStatusName(repair.getRepairStatus());
                
                // 获取报修人姓名
                String userName = getUserName(repair.getUserID());
                
                // 获取处理人姓名
                String handlerName = repair.getHandlerID() > 0 ? getUserName(repair.getHandlerID()) : "-";
                
                // 添加到表格
                tableModel.addRow(new Object[]{
                    repair.getRepairID(),
                    buildingName,
                    repair.getRoomNumber(),
                    repairTypeName,
                    repair.getRepairDesc(),
                    statusName,
                    userName,
                    handlerName,
                    dateFormat.format(repair.getCreateTime())
                });
            }
            
            // 更新统计数据
            updateStatistics();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载报修数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 更新统计数据
     */
    private void updateStatistics() {
        if (repairList == null) {
            return;
        }
        
        int total = repairList.size();
        int pending = 0;
        int processing = 0;
        int completed = 0;
        int canceled = 0;
        
        for (Repair repair : repairList) {
            switch (repair.getRepairStatus()) {
                case 0:
                    pending++;
                    break;
                case 1:
                    processing++;
                    break;
                case 2:
                    completed++;
                    break;
                case 3:
                    canceled++;
                    break;
            }
        }
        
        totalCountLabel.setText(String.valueOf(total));
        pendingCountLabel.setText(String.valueOf(pending));
        processingCountLabel.setText(String.valueOf(processing));
        completedCountLabel.setText(String.valueOf(completed));
        canceledCountLabel.setText(String.valueOf(canceled));
    }
    
    /**
     * 获取楼栋名称
     * @param buildingID 楼栋ID
     * @return 楼栋名称
     */
    private String getBuildingName(int buildingID) {
        try {
            Building building = buildingController.getBuildingByID(buildingID);
            return building != null ? building.getBuildingName() : "未知楼栋";
        } catch (Exception e) {
            return "未知楼栋";
        }
    }
    
    /**
     * 获取报修类型名称
     * @param repairType 报修类型
     * @return 报修类型名称
     */
    private String getRepairTypeName(int repairType) {
        switch (repairType) {
            case 1:
                return "水电维修";
            case 2:
                return "家具维修";
            case 3:
                return "门窗维修";
            case 4:
                return "其他";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 获取状态名称
     * @param status 状态
     * @return 状态名称
     */
    private String getStatusName(int status) {
        switch (status) {
            case 0:
                return "待处理";
            case 1:
                return "处理中";
            case 2:
                return "已完成";
            case 3:
                return "已取消";
            default:
                return "未知状态";
        }
    }
    
    /**
     * 获取用户姓名
     * @param userID 用户ID
     * @return 用户姓名
     */
    private String getUserName(int userID) {
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.findByID(userID);
            userDAO.close();
            return user != null ? user.getRealName() : "未知用户";
        } catch (SQLException e) {
            return "未知用户";
        }
    }
} 