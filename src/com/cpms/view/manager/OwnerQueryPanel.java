package com.cpms.view.manager;

import com.cpms.controller.admin.BuildingController;
import com.cpms.controller.manager.OwnerController;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;
import com.cpms.util.security.PasswordUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业主查询面板
 * 用于管家查询和管理业主信息
 */
public class OwnerQueryPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private OwnerController ownerController;
    private BuildingController buildingController;
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTable ownerTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> buildingComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JButton resetPasswordButton;
    private JButton enableButton;
    private JButton disableButton;
    private JButton editButton;
    
    // 数据
    private List<User> ownerList;
    private Map<Integer, String> buildingMap;
    private int selectedOwnerID = -1;
    
    // 楼栋数据
    private List<Building> buildingList;
    private int[] buildingIDs;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法，初始化业主查询面板
     * @param user 当前用户
     */
    public OwnerQueryPanel(User user) {
        this.currentUser = user;
        
        // 初始化控制器
        ownerController = new OwnerController();
        buildingController = new BuildingController();
        
        // 初始化数据
        buildingMap = new HashMap<>();
        loadBuildingData();
        
        // 设置布局
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 创建界面组件
        initComponents();
        
        // 加载业主数据
        loadOwnerData();
    }
    
    /**
     * 加载楼栋数据
     */
    private void loadBuildingData() {
        // 获取管家负责的楼栋ID
        Integer managedBuildingID = currentUser.getManagedBuildingID();
        
        // 如果管家有指定负责的楼栋，则只显示该楼栋
        if (managedBuildingID != null && managedBuildingID > 0) {
            Building building = buildingController.getBuildingByID(managedBuildingID);
            if (building != null) {
                buildingList = new ArrayList<>();
                buildingList.add(building);
            } else {
                buildingList = buildingController.getAllBuildings();
            }
        } else {
            // 否则显示所有楼栋
            buildingList = buildingController.getAllBuildings();
        }
        
        // 初始化楼栋ID数组
        buildingIDs = new int[buildingList.size() + 1];
        buildingIDs[0] = 0; // 第一个选项为"全部楼栋"
        
        // 创建楼栋映射
        for (int i = 0; i < buildingList.size(); i++) {
            Building building = buildingList.get(i);
            buildingIDs[i + 1] = building.getBuildingID();
            buildingMap.put(building.getBuildingID(), building.getBuildingName());
        }
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建顶部过滤面板
        JPanel filterPanel = createFilterPanel();
        
        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加到主面板
        add(filterPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建过滤面板
     * @return 过滤面板
     */
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // 楼栋过滤
        JLabel buildingLabel = new JLabel("楼栋:");
        buildingLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        buildingComboBox = new JComboBox<>();
        buildingComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 添加楼栋选项
        buildingComboBox.addItem("全部楼栋");
        for (Building building : buildingList) {
            buildingComboBox.addItem(building.getBuildingName());
        }
        
        // 如果管家只负责一个楼栋，则默认选中该楼栋
        Integer managedBuildingID = currentUser.getManagedBuildingID();
        if (managedBuildingID != null && managedBuildingID > 0 && buildingList.size() == 1) {
            buildingComboBox.setSelectedIndex(1);
        }
        
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
                loadOwnerData();
            }
        });
        
        panel.add(buildingLabel);
        panel.add(buildingComboBox);
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
                "业主列表",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                        config.getIntProperty("ui.font.size.large", 16)),
                PRIMARY_COLOR));
        
        // 创建表格模型
        String[] columnNames = {"ID", "用户名", "真实姓名", "联系电话", "房间号", "所属楼栋", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };
        
        // 创建表格
        ownerTable = new JTable(tableModel);
        ownerTable.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        ownerTable.setRowHeight(30);
        ownerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ownerTable.getTableHeader().setReorderingAllowed(false);
        ownerTable.getTableHeader().setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 设置列宽
        ownerTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        ownerTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        ownerTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        ownerTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        ownerTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        ownerTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        ownerTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        
        // 居中显示
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < ownerTable.getColumnCount(); i++) {
            ownerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // 添加表格选择事件
        ownerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = ownerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedOwnerID = (int) tableModel.getValueAt(selectedRow, 0);
                    updateButtonStatus();
                }
            }
        });
        
        // 添加到滚动面板
        JScrollPane scrollPane = new JScrollPane(ownerTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建按钮面板
     * @return 按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // 重置密码按钮
        resetPasswordButton = createStyledButton("重置密码", ACCENT_COLOR);
        resetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetOwnerPassword();
            }
        });
        resetPasswordButton.setEnabled(false);
        
        // 启用按钮
        enableButton = createStyledButton("启用", SUCCESS_COLOR);
        enableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableOwner();
            }
        });
        enableButton.setEnabled(false);
        
        // 禁用按钮
        disableButton = createStyledButton("禁用", ERROR_COLOR);
        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableOwner();
            }
        });
        disableButton.setEnabled(false);
        
        // 修改信息按钮
        JButton editButton = createStyledButton("修改信息", PRIMARY_COLOR);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editOwnerInfo();
            }
        });
        editButton.setEnabled(false);
        
        panel.add(resetPasswordButton);
        panel.add(enableButton);
        panel.add(disableButton);
        panel.add(editButton);
        
        // 保存修改信息按钮的引用
        this.editButton = editButton;
        
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
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
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
     * 加载业主数据
     */
    private void loadOwnerData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        try {
            // 获取过滤条件
            int buildingIndex = buildingComboBox.getSelectedIndex();
            String searchText = searchField.getText().trim().toLowerCase();
            
            // 根据楼栋过滤获取业主列表
            if (buildingIndex <= 0) {
                // 获取管家负责的楼栋ID
                Integer managedBuildingID = currentUser.getManagedBuildingID();
                if (managedBuildingID != null && managedBuildingID > 0) {
                    ownerList = ownerController.getOwnersByBuildingID(managedBuildingID);
                } else {
                    ownerList = ownerController.getAllOwners();
                }
            } else {
                int buildingID = buildingIDs[buildingIndex];
                ownerList = ownerController.getOwnersByBuildingID(buildingID);
            }
            
            // 根据搜索文本过滤
            if (!searchText.isEmpty()) {
                List<User> filteredList = new ArrayList<>();
                for (User owner : ownerList) {
                    if (owner.getUsername().toLowerCase().contains(searchText) || 
                        (owner.getRealName() != null && owner.getRealName().toLowerCase().contains(searchText)) || 
                        (owner.getPhone() != null && owner.getPhone().toLowerCase().contains(searchText))) {
                        filteredList.add(owner);
                    }
                }
                ownerList = filteredList;
            }
            
            // 填充表格数据
            for (User owner : ownerList) {
                // 获取楼栋名称
                String buildingName = "未知";
                if (owner.getBuildingID() != null) {
                    buildingName = buildingMap.getOrDefault(owner.getBuildingID(), "未知");
                }
                
                // 获取状态名称
                String statusName = owner.getAccountStatus() == 1 ? "启用" : "禁用";
                
                // 添加到表格
                tableModel.addRow(new Object[]{
                    owner.getUserID(),
                    owner.getUsername(),
                    owner.getRealName(),
                    owner.getPhone(),
                    owner.getRoomNumber() != null ? owner.getRoomNumber() : "",
                    buildingName,
                    statusName
                });
            }
            
            // 重置选中状态
            selectedOwnerID = -1;
            updateButtonStatus();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载业主数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 更新按钮状态
     */
    private void updateButtonStatus() {
        boolean hasSelection = selectedOwnerID > 0;
        resetPasswordButton.setEnabled(hasSelection);
        editButton.setEnabled(hasSelection);
        
        if (hasSelection) {
            // 获取选中业主的状态
            User selectedOwner = null;
            for (User owner : ownerList) {
                if (owner.getUserID() == selectedOwnerID) {
                    selectedOwner = owner;
                    break;
                }
            }
            
            if (selectedOwner != null) {
                enableButton.setEnabled(selectedOwner.getAccountStatus() != 1);
                disableButton.setEnabled(selectedOwner.getAccountStatus() == 1);
            } else {
                enableButton.setEnabled(false);
                disableButton.setEnabled(false);
            }
        } else {
            enableButton.setEnabled(false);
            disableButton.setEnabled(false);
        }
    }
    
    /**
     * 重置业主密码
     */
    private void resetOwnerPassword() {
        if (selectedOwnerID <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择业主！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 生成随机密码
        String newPassword = PasswordUtil.generateRandomPassword(8);
        
        // 确认重置密码
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要重置该业主的密码吗？\n新密码将设置为：" + newPassword,
                "确认重置密码",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 重置密码
            boolean success = ownerController.resetOwnerPassword(selectedOwnerID, newPassword);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "密码重置成功！\n新密码：" + newPassword, "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "密码重置失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 启用业主账号
     */
    private void enableOwner() {
        if (selectedOwnerID <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择业主！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 确认启用账号
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要启用该业主的账号吗？",
                "确认启用",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 启用账号
            boolean success = ownerController.enableOwner(selectedOwnerID);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "账号启用成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadOwnerData(); // 重新加载数据
            } else {
                JOptionPane.showMessageDialog(this, "账号启用失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 禁用业主账号
     */
    private void disableOwner() {
        if (selectedOwnerID <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择业主！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 确认禁用账号
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要禁用该业主的账号吗？\n禁用后该业主将无法登录系统。",
                "确认禁用",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 禁用账号
            boolean success = ownerController.disableOwner(selectedOwnerID);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "账号禁用成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadOwnerData(); // 重新加载数据
            } else {
                JOptionPane.showMessageDialog(this, "账号禁用失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 修改业主信息
     */
    private void editOwnerInfo() {
        if (selectedOwnerID <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择业主！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 获取选中的业主信息
        User selectedOwner = null;
        for (User owner : ownerList) {
            if (owner.getUserID() == selectedOwnerID) {
                selectedOwner = owner;
                break;
            }
        }
        
        if (selectedOwner == null) {
            JOptionPane.showMessageDialog(this, "获取业主信息失败！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 创建修改业主信息对话框
        EditOwnerDialog dialog = new EditOwnerDialog(SwingUtilities.getWindowAncestor(this), selectedOwner, buildingList);
        dialog.setVisible(true);
        
        // 如果修改成功，重新加载数据
        if (dialog.isSuccess()) {
            loadOwnerData();
            JOptionPane.showMessageDialog(this, "业主信息修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}