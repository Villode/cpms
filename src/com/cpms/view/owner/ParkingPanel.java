package com.cpms.view.owner;

import com.cpms.controller.owner.ParkingController;
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
import java.util.List;

/**
 * 业主车位管理面板
 * 用于查看和管理业主的车位信息
 */
public class ParkingPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private ParkingController parkingController;
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTable parkingTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    // 移除了editPlateButton，业主不能直接修改车牌号
    
    // 数据
    private List<ParkingSpot> parkingList;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法
     * @param user 当前用户
     */
    public ParkingPanel(User user) {
        this.currentUser = user;
        this.parkingController = new ParkingController();
        
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
        
        // 创建工具栏面板
        JPanel toolbarPanel = createToolbarPanel();
        
        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        
        // 创建顶部面板（标题+工具栏）
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(SECONDARY_COLOR);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(toolbarPanel, BorderLayout.SOUTH);
        
        // 组装界面
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
        
        JLabel titleLabel = new JLabel("我的车位");
        titleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.large", 18)));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        panel.add(titleLabel);
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
        refreshButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        refreshButton.setBackground(PRIMARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        
        // 提示标签
        JLabel tipLabel = new JLabel("如需修改车牌号，请联系物业管家");
        tipLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 12)));
        tipLabel.setForeground(Color.GRAY);
        
        panel.add(refreshButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(tipLabel);
        
        return panel;
    }
    
    /**
     * 创建表格面板
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        // 创建表格模型
        String[] columnNames = {"车位编号", "位置", "车牌号", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 禁止编辑
            }
        };
        
        // 创建表格
        parkingTable = new JTable(tableModel);
        parkingTable.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        parkingTable.setRowHeight(config.getIntProperty("ui.table.row.height", 30));
        parkingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        parkingTable.setGridColor(Color.LIGHT_GRAY);
        parkingTable.setShowGrid(true);
        
        // 设置表格头样式
        parkingTable.getTableHeader().setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        parkingTable.getTableHeader().setBackground(PRIMARY_COLOR);
        parkingTable.getTableHeader().setForeground(Color.WHITE);
        
        // 设置列宽
        parkingTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        parkingTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        parkingTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        parkingTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        
        // 设置状态列的渲染器
        parkingTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if ("已占用".equals(value)) {
                        c.setBackground(new Color(255, 235, 235)); // 浅红色
                        c.setForeground(new Color(200, 0, 0)); // 深红色
                    } else {
                        c.setBackground(new Color(235, 255, 235)); // 浅绿色
                        c.setForeground(new Color(0, 150, 0)); // 深绿色
                    }
                }
                
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(parkingTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // 添加车位信息统计面板
        JPanel infoPanel = createInfoPanel();
        
        // 创建主内容面板
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(SECONDARY_COLOR);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(infoPanel, BorderLayout.SOUTH);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * 创建信息统计面板
     */
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // 统计信息标签
        JLabel infoLabel = new JLabel();
        infoLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 12)));
        infoLabel.setForeground(Color.GRAY);
        
        // 更新统计信息
        updateInfoLabel(infoLabel);
        
        panel.add(infoLabel);
        return panel;
    }
    
    /**
     * 更新统计信息标签
     */
    private void updateInfoLabel(JLabel infoLabel) {
        if (parkingList != null && !parkingList.isEmpty()) {
            int totalSpots = parkingList.size();
            int occupiedSpots = 0;
            int spotsWithPlate = 0;
            
            for (ParkingSpot spot : parkingList) {
                if (spot.getUsageStatus() == 1) {
                    occupiedSpots++;
                }
                if (spot.getLicensePlate() != null && !spot.getLicensePlate().trim().isEmpty()) {
                    spotsWithPlate++;
                }
            }
            
            infoLabel.setText(String.format("共 %d 个车位 | 已占用 %d 个 | 已绑定车牌 %d 个", 
                    totalSpots, occupiedSpots, spotsWithPlate));
        } else {
            infoLabel.setText("暂无车位信息");
        }
    }
    
    /**
     * 加载数据
     */
    private void loadData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 获取车位数据
        parkingList = parkingController.getOwnerParkingSpots(currentUser.getUserID());
        
        if (parkingList != null && !parkingList.isEmpty()) {
            for (ParkingSpot parking : parkingList) {
                Object[] rowData = {
                    parking.getSpotNumber(),
                    parking.getLocation(),
                    parking.getLicensePlate() != null ? parking.getLicensePlate() : "未设置",
                    parking.getUsageStatus() == 1 ? "已占用" : "空闲"
                };
                tableModel.addRow(rowData);
            }
        } else {
            // 显示无数据提示
            Object[] rowData = {"暂无车位信息", "", "", ""};
            tableModel.addRow(rowData);
        }
        
        // 更新统计信息
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                updateInfoPanelRecursively((JPanel) comp);
            }
        }
    }
    
    /**
     * 递归更新信息面板
     */
    private void updateInfoPanelRecursively(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                updateInfoPanelRecursively((JPanel) comp);
            } else if (comp instanceof JLabel && ((JLabel) comp).getForeground().equals(Color.GRAY)) {
                updateInfoLabel((JLabel) comp);
            }
        }
    }
    
    // 移除了editLicensePlate方法，业主不能直接修改车牌号
    
    // 移除了EditLicensePlateDialog类，业主不能直接修改车牌号
}