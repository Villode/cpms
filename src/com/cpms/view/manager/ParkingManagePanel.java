package com.cpms.view.manager;

import com.cpms.controller.manager.OwnerController;
import com.cpms.model.dao.BuildingDAO;
import com.cpms.model.dao.ParkingSpotDAO;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.ParkingSpot;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * 物业管家车位管理面板
 * 用于管理小区车位信息
 */
public class ParkingManagePanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTable parkingTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton assignButton;
    private JComboBox<String> statusFilterComboBox;
    private JComboBox<String> buildingFilterComboBox;
    
    // 数据
    private List<ParkingSpot> parkingList;
    private List<Building> buildingList;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法
     * @param user 当前用户
     */
    public ParkingManagePanel(User user) {
        this.currentUser = user;
        
        // 设置面板属性
        setLayout(new BorderLayout());
        setBackground(SECONDARY_COLOR);
        
        // 初始化界面组件
        initComponents();
        
        // 加载数据
        loadData();
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建标题面板
        JPanel titlePanel = createTitlePanel();
        
        // 创建筛选面板
        JPanel filterPanel = createFilterPanel();
        
        // 创建工具栏面板
        JPanel toolbarPanel = createToolbarPanel();
        
        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        
        // 组装界面
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(toolbarPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }
    
    /**
     * 创建标题面板
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("车位管理");
        titleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.large", 18)));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        panel.add(titleLabel);
        return panel;
    }
    
    /**
     * 创建筛选面板
     */
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(5, 20, 5, 20));
        
        // 楼栋筛选
        JLabel buildingLabel = new JLabel("楼栋:");
        buildingLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        
        buildingFilterComboBox = new JComboBox<>();
        buildingFilterComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        buildingFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        
        // 状态筛选
        JLabel statusLabel = new JLabel("状态:");
        statusLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        
        statusFilterComboBox = new JComboBox<>(new String[]{"全部", "空闲", "已占用"});
        statusFilterComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        statusFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        
        panel.add(buildingLabel);
        panel.add(buildingFilterComboBox);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(statusLabel);
        panel.add(statusFilterComboBox);
        
        return panel;
    }
    
    /**
     * 创建工具栏面板
     */
    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // 刷新按钮
        refreshButton = new JButton("刷新");
        refreshButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        refreshButton.setBackground(PRIMARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        
        // 新增按钮
        addButton = new JButton("新增车位");
        addButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddParkingDialog();
            }
        });
        
        // 编辑按钮
        editButton = new JButton("编辑");
        editButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        editButton.setBackground(ACCENT_COLOR);
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setBorderPainted(false);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editParking();
            }
        });
        
        // 分配按钮
        assignButton = new JButton("分配车位");
        assignButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        assignButton.setBackground(new Color(156, 39, 176));
        assignButton.setForeground(Color.WHITE);
        assignButton.setFocusPainted(false);
        assignButton.setBorderPainted(false);
        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignParking();
            }
        });
        
        // 删除按钮
        deleteButton = new JButton("删除");
        deleteButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteParking();
            }
        });
        
        panel.add(refreshButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(addButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(editButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(assignButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(deleteButton);
        
        return panel;
    }
    
    /**
     * 创建表格面板
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        // 创建表格模型
        String[] columnNames = {"车位ID", "楼栋", "车位编号", "位置", "业主", "车牌号", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // 创建表格
        parkingTable = new JTable(tableModel);
        parkingTable.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        parkingTable.setRowHeight(30);
        parkingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        parkingTable.setGridColor(Color.LIGHT_GRAY);
        parkingTable.setShowGrid(true);
        
        // 设置表格头样式
        parkingTable.getTableHeader().setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 14));
        parkingTable.getTableHeader().setBackground(PRIMARY_COLOR);
        parkingTable.getTableHeader().setForeground(Color.WHITE);
        
        // 设置列宽
        parkingTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        parkingTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        parkingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        parkingTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        parkingTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        parkingTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        parkingTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        
        // 设置状态列的渲染器
        parkingTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if ("已占用".equals(value)) {
                        c.setBackground(new Color(255, 235, 235));
                        c.setForeground(new Color(200, 0, 0));
                    } else {
                        c.setBackground(new Color(235, 255, 235));
                        c.setForeground(new Color(0, 150, 0));
                    }
                }
                
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(parkingTable);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * 加载数据
     */
    private void loadData() {
        // 加载楼栋数据
        loadBuildingData();
        
        // 清空表格
        tableModel.setRowCount(0);
        
        try {
            ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
            
            // 获取筛选条件
            int selectedBuildingIndex = buildingFilterComboBox.getSelectedIndex();
            int selectedStatusIndex = statusFilterComboBox.getSelectedIndex();
            
            if (selectedBuildingIndex > 0 && buildingList != null && selectedBuildingIndex <= buildingList.size()) {
                // 按楼栋筛选
                Building selectedBuilding = buildingList.get(selectedBuildingIndex - 1);
                parkingList = parkingSpotDAO.findByBuildingID(selectedBuilding.getBuildingID());
            } else {
                // 获取所有车位
                parkingList = parkingSpotDAO.findAll();
            }
            
            if (parkingList != null) {
                for (ParkingSpot parking : parkingList) {
                    // 状态筛选
                    if (selectedStatusIndex == 1 && parking.getUsageStatus() != 0) continue; // 只显示空闲
                    if (selectedStatusIndex == 2 && parking.getUsageStatus() != 1) continue; // 只显示已占用
                    
                    // 获取楼栋名称
                    String buildingName = getBuildingName(parking.getBuildingID());
                    
                    // 获取业主信息
                    String ownerName = "未分配";
                    if (parking.getOwnerUserID() != null) {
                        ownerName = getOwnerName(parking.getOwnerUserID());
                    }
                    
                    Object[] rowData = {
                        parking.getParkingID(),
                        buildingName,
                        parking.getSpotNumber(),
                        parking.getLocation(),
                        ownerName,
                        parking.getLicensePlate() != null ? parking.getLicensePlate() : "未设置",
                        parking.getUsageStatus() == 1 ? "已占用" : "空闲"
                    };
                    tableModel.addRow(rowData);
                }
            }
            
            parkingSpotDAO.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载车位数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 加载楼栋数据
     */
    private void loadBuildingData() {
        try {
            BuildingDAO buildingDAO = new BuildingDAO();
            buildingList = buildingDAO.findAll();
            
            buildingFilterComboBox.removeAllItems();
            buildingFilterComboBox.addItem("全部楼栋");
            
            if (buildingList != null) {
                for (Building building : buildingList) {
                    buildingFilterComboBox.addItem(building.getBuildingName());
                }
            }
            
            buildingDAO.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取楼栋名称
     */
    private String getBuildingName(int buildingID) {
        if (buildingList != null) {
            for (Building building : buildingList) {
                if (building.getBuildingID() == buildingID) {
                    return building.getBuildingName();
                }
            }
        }
        return "未知楼栋";
    }
    
    /**
     * 获取业主姓名
     */
    private String getOwnerName(int ownerUserID) {
        OwnerController ownerController = new OwnerController();
        User owner = ownerController.getOwnerByID(ownerUserID);
        if (owner != null) {
            return owner.getRealName() != null ? owner.getRealName() : owner.getUsername();
        }
        return "未知业主";
    }
    
    /**
     * 显示新增车位对话框
     */
    private void showAddParkingDialog() {
        AddParkingDialog dialog = new AddParkingDialog(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            loadData();
        }
    }
    
    /**
     * 编辑车位
     */
    private void editParking() {
        int selectedRow = parkingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个车位", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (parkingList == null || selectedRow >= parkingList.size()) {
            JOptionPane.showMessageDialog(this, "车位信息异常", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ParkingSpot selectedParking = parkingList.get(selectedRow);
        EditParkingDialog dialog = new EditParkingDialog(SwingUtilities.getWindowAncestor(this), selectedParking);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            loadData();
        }
    }
    
    /**
     * 分配车位
     */
    private void assignParking() {
        int selectedRow = parkingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个车位", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (parkingList == null || selectedRow >= parkingList.size()) {
            JOptionPane.showMessageDialog(this, "车位信息异常", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ParkingSpot selectedParking = parkingList.get(selectedRow);
        AssignParkingDialog dialog = new AssignParkingDialog(SwingUtilities.getWindowAncestor(this), selectedParking);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            loadData();
        }
    }
    
    /**
     * 删除车位
     */
    private void deleteParking() {
        int selectedRow = parkingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个车位", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (parkingList == null || selectedRow >= parkingList.size()) {
            JOptionPane.showMessageDialog(this, "车位信息异常", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ParkingSpot selectedParking = parkingList.get(selectedRow);
        
        // 确认删除
        int result = JOptionPane.showConfirmDialog(this, 
                "确定要删除车位 " + selectedParking.getSpotNumber() + " 吗？\n此操作不可撤销！", 
                "确认删除", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
                boolean success = parkingSpotDAO.deleteParkingSpot(selectedParking.getParkingID());
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "车位删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "车位删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
                }
                
                parkingSpotDAO.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "删除车位时发生错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}