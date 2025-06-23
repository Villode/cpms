package com.cpms.view.admin;

import com.cpms.controller.admin.BuildingController;
import com.cpms.controller.admin.UserController;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 楼栋管理面板
 * 提供楼栋管理功能
 */
public class BuildingManagementPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private BuildingController buildingController;
    private UserController userController;
    
    // 界面组件
    private JTable buildingTable;
    private DefaultTableModel tableModel;
    private JTextField buildingNameField;
    private JTextField buildingCodeField;
    private JTextField addressField;
    private JTextField totalFloorsField;
    private JTextField totalUnitsField;
    private JTextField totalRoomsField;
    private JComboBox<String> managerComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    
    // 楼栋和管家数据
    private List<Building> buildingList;
    private List<User> managerList;
    private int[] managerIDs;
    
    // 当前选中的楼栋ID
    private int currentBuildingID = -1;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法，初始化楼栋管理面板
     */
    public BuildingManagementPanel() {
        // 初始化控制器
        buildingController = new BuildingController();
        userController = new UserController();
        
        // 初始化数据
        buildingList = buildingController.getAllBuildings();
        managerList = userController.getUsersByRoleID(2); // 假设角色ID 2是管家角色
        
        // 初始化管家ID数组，长度为管家列表大小+1（多一个是"请选择管家"选项）
        managerIDs = new int[managerList.size() + 1];
        
        // 设置布局
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 创建界面组件
        initComponents();
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        
        // 创建表单面板
        JPanel formPanel = createFormPanel();
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加到主面板
        add(tablePanel, BorderLayout.CENTER);
        add(formPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建表格面板
     * @return 表格面板
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("楼栋列表"));
        
        // 创建表格模型
        String[] columnNames = {"ID", "楼栋名称", "楼栋编码", "地址", "总楼层", "总单元", "总房间", "管家"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        // 创建表格
        buildingTable = new JTable(tableModel);
        buildingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        buildingTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        buildingTable.getTableHeader().setReorderingAllowed(false);
        
        // 设置列宽
        buildingTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        buildingTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        buildingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        buildingTable.getColumnModel().getColumn(3).setPreferredWidth(250);
        buildingTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        buildingTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        buildingTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        buildingTable.getColumnModel().getColumn(7).setPreferredWidth(120);
        
        // 添加表格选择监听器
        buildingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = buildingTable.getSelectedRow();
                if (row >= 0) {
                    currentBuildingID = (int) tableModel.getValueAt(row, 0);
                    fillFormWithSelectedBuilding();
                }
            }
        });
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(buildingTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // 加载楼栋数据
        loadBuildingData();
        
        return panel;
    }
    
    /**
     * 创建表单面板
     * @return 表单面板
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("楼栋信息"));
        panel.setPreferredSize(new Dimension(350, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 楼栋名称
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel buildingNameLabel = new JLabel("楼栋名称:");
        buildingNameLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(buildingNameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        buildingNameField = new JTextField(20);
        buildingNameField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(buildingNameField, gbc);
        
        // 楼栋编码
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel buildingCodeLabel = new JLabel("楼栋编码:");
        buildingCodeLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(buildingCodeLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        buildingCodeField = new JTextField(20);
        buildingCodeField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(buildingCodeField, gbc);
        
        // 楼栋地址
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel addressLabel = new JLabel("楼栋地址:");
        addressLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(addressLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        addressField = new JTextField(20);
        addressField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(addressField, gbc);
        
        // 总楼层数
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel totalFloorsLabel = new JLabel("总楼层数:");
        totalFloorsLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(totalFloorsLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        totalFloorsField = new JTextField(20);
        totalFloorsField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(totalFloorsField, gbc);
        
        // 总单元数
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel totalUnitsLabel = new JLabel("总单元数:");
        totalUnitsLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(totalUnitsLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        totalUnitsField = new JTextField(20);
        totalUnitsField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(totalUnitsField, gbc);
        
        // 总房间数
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel totalRoomsLabel = new JLabel("总房间数:");
        totalRoomsLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(totalRoomsLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        totalRoomsField = new JTextField(20);
        totalRoomsField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(totalRoomsField, gbc);
        
        // 管家
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel managerLabel = new JLabel("管家:");
        managerLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        panel.add(managerLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        managerComboBox = new JComboBox<>();
        managerComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 添加管家选项
        managerComboBox.addItem("-- 请选择管家 --");
        managerIDs[0] = 0; // 第一个选项对应的管家ID为0
        
        for (int i = 0; i < managerList.size(); i++) {
            User manager = managerList.get(i);
            managerComboBox.addItem(manager.getRealName() + " (" + manager.getUsername() + ")");
            managerIDs[i + 1] = manager.getUserID();
        }
        
        panel.add(managerComboBox, gbc);
        
        return panel;
    }
    
    /**
     * 创建按钮面板
     * @return 按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // 添加按钮
        addButton = createStyledButton("添加", SUCCESS_COLOR);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBuilding();
            }
        });
        
        // 更新按钮
        updateButton = createStyledButton("更新", ACCENT_COLOR);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBuilding();
            }
        });
        
        // 删除按钮
        deleteButton = createStyledButton("删除", ERROR_COLOR);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBuilding();
            }
        });
        
        // 清空按钮
        clearButton = createStyledButton("清空", Color.GRAY);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        
        return panel;
    }
    
    /**
     * 创建样式化按钮
     * @param text 按钮文本
     * @param bgColor 背景颜色
     * @return 样式化的按钮
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        button.setPreferredSize(new Dimension(100, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * 加载楼栋数据
     */
    private void loadBuildingData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 重新获取楼栋列表
        buildingList = buildingController.getAllBuildings();
        
        // 加载楼栋数据到表格
        for (Building building : buildingList) {
            String managerName = "未分配";
            
            // 获取管家信息
            if (building.getManagerID() > 0) {
                User manager = buildingController.getBuildingManager(building.getBuildingID());
                if (manager != null) {
                    managerName = manager.getRealName();
                }
            }
            
            Object[] rowData = {
                building.getBuildingID(),
                building.getBuildingName(),
                building.getBuildingCode(),
                building.getAddress(),
                building.getTotalFloors(),
                building.getTotalUnits(),
                building.getTotalRooms(),
                managerName
            };
            tableModel.addRow(rowData);
        }
    }
    
    /**
     * 填充表单
     */
    private void fillFormWithSelectedBuilding() {
        for (Building building : buildingList) {
            if (building.getBuildingID() == currentBuildingID) {
                buildingNameField.setText(building.getBuildingName());
                buildingCodeField.setText(building.getBuildingCode());
                addressField.setText(building.getAddress());
                totalFloorsField.setText(String.valueOf(building.getTotalFloors()));
                totalUnitsField.setText(String.valueOf(building.getTotalUnits()));
                totalRoomsField.setText(String.valueOf(building.getTotalRooms()));
                
                // 设置管家选择框
                int managerID = building.getManagerID();
                for (int i = 0; i < managerIDs.length; i++) {
                    if (managerIDs[i] == managerID) {
                        managerComboBox.setSelectedIndex(i);
                        break;
                    }
                }
                
                break;
            }
        }
    }
    
    /**
     * 清空表单
     */
    private void clearForm() {
        currentBuildingID = -1;
        buildingNameField.setText("");
        buildingCodeField.setText("");
        addressField.setText("");
        totalFloorsField.setText("");
        totalUnitsField.setText("");
        totalRoomsField.setText("");
        managerComboBox.setSelectedIndex(0);
        buildingTable.clearSelection();
    }
    
    /**
     * 添加楼栋
     */
    private void addBuilding() {
        // 获取表单数据
        String buildingName = buildingNameField.getText().trim();
        String buildingCode = buildingCodeField.getText().trim();
        String address = addressField.getText().trim();
        
        // 验证必填字段
        if (buildingName.isEmpty() || buildingCode.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "楼栋名称、编码和地址不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 验证数字字段
        int totalFloors, totalUnits, totalRooms;
        try {
            totalFloors = Integer.parseInt(totalFloorsField.getText().trim());
            totalUnits = Integer.parseInt(totalUnitsField.getText().trim());
            totalRooms = Integer.parseInt(totalRoomsField.getText().trim());
            
            if (totalFloors <= 0 || totalUnits <= 0 || totalRooms <= 0) {
                JOptionPane.showMessageDialog(this, "总楼层数、总单元数和总房间数必须大于0！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "总楼层数、总单元数和总房间数必须是有效的整数！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 获取管家ID
        int selectedManagerIndex = managerComboBox.getSelectedIndex();
        int managerID = 0;
        if (selectedManagerIndex > 0 && selectedManagerIndex < managerIDs.length) {
            managerID = managerIDs[selectedManagerIndex];
        }
        
        // 添加楼栋
        boolean success = buildingController.addBuilding(buildingName, buildingCode, address, 
                                                        totalFloors, totalUnits, totalRooms, managerID);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "楼栋添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadBuildingData();
        } else {
            JOptionPane.showMessageDialog(this, "楼栋添加失败！可能楼栋编码已存在。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 更新楼栋
     */
    private void updateBuilding() {
        // 检查是否选择了楼栋
        if (currentBuildingID <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择要更新的楼栋！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 获取表单数据
        String buildingName = buildingNameField.getText().trim();
        String buildingCode = buildingCodeField.getText().trim();
        String address = addressField.getText().trim();
        
        // 验证必填字段
        if (buildingName.isEmpty() || buildingCode.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "楼栋名称、编码和地址不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 验证数字字段
        int totalFloors, totalUnits, totalRooms;
        try {
            totalFloors = Integer.parseInt(totalFloorsField.getText().trim());
            totalUnits = Integer.parseInt(totalUnitsField.getText().trim());
            totalRooms = Integer.parseInt(totalRoomsField.getText().trim());
            
            if (totalFloors <= 0 || totalUnits <= 0 || totalRooms <= 0) {
                JOptionPane.showMessageDialog(this, "总楼层数、总单元数和总房间数必须大于0！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "总楼层数、总单元数和总房间数必须是有效的整数！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 获取管家ID
        int selectedManagerIndex = managerComboBox.getSelectedIndex();
        int managerID = 0;
        if (selectedManagerIndex > 0 && selectedManagerIndex < managerIDs.length) {
            managerID = managerIDs[selectedManagerIndex];
        }
        
        // 更新楼栋
        boolean success = buildingController.updateBuilding(currentBuildingID, buildingName, buildingCode, address, 
                                                           totalFloors, totalUnits, totalRooms, managerID);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "楼栋更新成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadBuildingData();
        } else {
            JOptionPane.showMessageDialog(this, "楼栋更新失败！可能楼栋编码已被其他楼栋使用。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 删除楼栋
     */
    private void deleteBuilding() {
        // 检查是否选择了楼栋
        if (currentBuildingID <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的楼栋！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 确认删除
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该楼栋吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // 删除楼栋
        boolean success = buildingController.deleteBuilding(currentBuildingID);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "楼栋删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadBuildingData();
        } else {
            JOptionPane.showMessageDialog(this, "楼栋删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}