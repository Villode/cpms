package com.cpms.view.admin;

import com.cpms.controller.admin.PermissionController;
import com.cpms.controller.admin.RoleController;
import com.cpms.controller.admin.RolePermissionController;
import com.cpms.model.entity.Permission;
import com.cpms.model.entity.Role;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限分配面板
 * 提供角色权限分配功能
 */
public class RolePermissionPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private RoleController roleController;
    private PermissionController permissionController;
    private RolePermissionController rolePermissionController;
    
    // 界面组件
    private ButtonGroup roleButtonGroup;
    private JPanel roleRadioPanel;
    private JTable permissionTable;
    private DefaultTableModel tableModel;
    private JButton saveButton;
    private JButton selectAllButton;
    private JButton deselectAllButton;
    
    // 角色和权限数据
    private List<Role> roleList;
    private List<Permission> permissionList;
    private List<Integer> selectedPermissionIDs;
    
    // 当前选中的角色ID
    private int currentRoleID = -1;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法，初始化角色权限分配面板
     */
    public RolePermissionPanel() {
        // 初始化控制器
        roleController = new RoleController();
        permissionController = new PermissionController();
        rolePermissionController = new RolePermissionController();
        
        // 初始化数据
        roleList = roleController.getAllRoles();
        permissionList = permissionController.getAllPermissions();
        selectedPermissionIDs = new ArrayList<>();
        
        // 设置布局
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 创建界面组件
        initComponents();
        
        // 默认选择管理员角色（通常是第一个角色）并加载权限
        if (roleList != null && !roleList.isEmpty()) {
            // 默认选择第一个角色
            currentRoleID = roleList.get(0).getRoleID();
            loadPermissions(); // 加载该角色的权限
        }
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建角色选择面板
        JPanel rolePanel = createRolePanel();
        
        // 创建权限表格面板
        JPanel permissionPanel = createPermissionPanel();
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加到主面板
        add(rolePanel, BorderLayout.NORTH);
        add(permissionPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建角色选择面板
     * @return 角色选择面板
     */
    private JPanel createRolePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("选择角色"));
        
        JLabel roleLabel = new JLabel("角色:");
        roleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        roleLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        // 创建单选按钮组
        roleButtonGroup = new ButtonGroup();
        roleRadioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // 添加角色单选按钮
        boolean isFirst = true;
        for (Role role : roleList) {
            JRadioButton radioButton = new JRadioButton(role.getRoleName());
            radioButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                    config.getIntProperty("ui.font.size.normal", 14)));
            
            // 设置默认选中第一个角色
            if (isFirst) {
                radioButton.setSelected(true);
                isFirst = false;
            }
            
            // 添加选择监听器
            radioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 找到选中的角色
                    for (int i = 0; i < roleList.size(); i++) {
                        if (roleList.get(i).getRoleName().equals(radioButton.getText())) {
                            currentRoleID = roleList.get(i).getRoleID();
                            loadPermissions();
                            break;
                        }
                    }
                }
            });
            
            roleButtonGroup.add(radioButton);
            roleRadioPanel.add(radioButton);
        }
        
        panel.add(roleLabel, BorderLayout.WEST);
        panel.add(roleRadioPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建权限表格面板
     * @return 权限表格面板
     */
    private JPanel createPermissionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("权限列表"));
        
        // 创建表格模型
        String[] columnNames = {"选择", "ID", "权限名称", "权限代码", "权限描述", "权限类型"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // 只有复选框列可编辑
            }
        };
        
        // 创建表格
        permissionTable = new JTable(tableModel);
        permissionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        permissionTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        permissionTable.getTableHeader().setReorderingAllowed(false);
        
        // 设置列宽
        permissionTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        permissionTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        permissionTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        permissionTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        permissionTable.getColumnModel().getColumn(4).setPreferredWidth(250);
        permissionTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        // 添加表格模型监听器来处理复选框状态变化
        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == 0) { // 复选框列
                int row = e.getFirstRow();
                if (row >= 0 && row < tableModel.getRowCount()) {
                    boolean selected = (boolean) tableModel.getValueAt(row, 0);
                    int permissionID = (int) tableModel.getValueAt(row, 1);
                    
                    if (selected) {
                        if (!selectedPermissionIDs.contains(permissionID)) {
                            selectedPermissionIDs.add(permissionID);
                        }
                    } else {
                        selectedPermissionIDs.remove(Integer.valueOf(permissionID));
                    }
                }
            }
        });
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(permissionTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建按钮面板
     * @return 按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // 全选按钮
        selectAllButton = createStyledButton("全选", ACCENT_COLOR);
        selectAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAll();
            }
        });
        
        // 全不选按钮
        deselectAllButton = createStyledButton("全不选", Color.GRAY);
        deselectAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deselectAll();
            }
        });
        
        // 保存按钮
        saveButton = createStyledButton("保存", SUCCESS_COLOR);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePermissions();
            }
        });
        
        panel.add(selectAllButton);
        panel.add(deselectAllButton);
        panel.add(saveButton);
        
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
     * 加载权限列表
     */
    private void loadPermissions() {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 清空已选权限
        selectedPermissionIDs.clear();
        
        // 获取角色的权限ID列表
        if (currentRoleID > 0) {
            selectedPermissionIDs = permissionController.getPermissionIDsByRoleID(currentRoleID);
        }
        
        // 根据角色过滤权限列表
        List<Permission> filteredPermissions = getPermissionsByRole(currentRoleID);
        
        // 加载权限列表
        for (Permission permission : filteredPermissions) {
            boolean selected = selectedPermissionIDs.contains(permission.getPermissionID());
            String permissionType = getPermissionTypeText(permission.getPermissionType());
            
            Object[] rowData = {
                selected,
                permission.getPermissionID(),
                permission.getPermissionName(),
                permission.getPermissionCode(),
                permission.getPermissionDesc(),
                permissionType
            };
            tableModel.addRow(rowData);
        }
    }
    
    /**
     * 根据角色获取对应的权限列表
     * @param roleID 角色ID
     * @return 权限列表
     */
    private List<Permission> getPermissionsByRole(int roleID) {
        List<Permission> filteredPermissions = new ArrayList<>();
        
        for (Permission permission : permissionList) {
            String permissionCode = permission.getPermissionCode();
            
            if (roleID == 1) {
                // 管理员：显示所有权限
                filteredPermissions.add(permission);
            } else if (roleID == 2) {
                // 管家：显示管理相关权限
                if (isManagerPermission(permissionCode)) {
                    filteredPermissions.add(permission);
                }
            } else if (roleID == 3) {
                // 业主：显示个人相关权限
                if (isOwnerPermission(permissionCode)) {
                    filteredPermissions.add(permission);
                }
            }
        }
        
        return filteredPermissions;
    }
    
    /**
     * 判断是否为管家权限
     * @param permissionCode 权限代码
     * @return 是否为管家权限
     */
    private boolean isManagerPermission(String permissionCode) {
        // 管家可以使用的权限
        return permissionCode.startsWith("OWNER_") ||      // 业主管理
               permissionCode.startsWith("VISITOR_") ||    // 访客管理
               permissionCode.startsWith("REPAIR_") ||     // 报修管理
               permissionCode.startsWith("PAYMENT_") ||    // 缴费管理
               permissionCode.startsWith("PARKING_") ||    // 车位管理
               permissionCode.equals("LICENSE_PLATE_EDIT") || // 车牌号修改
               permissionCode.equals("SYSTEM_EXIT");       // 系统退出
    }
    
    /**
     * 判断是否为业主权限
     * @param permissionCode 权限代码
     * @return 是否为业主权限
     */
    private boolean isOwnerPermission(String permissionCode) {
        return permissionCode.equals("REPAIR_SUBMIT") ||        // 提交报修
               permissionCode.equals("REPAIR_QUERY_OWN") ||     // 查询个人报修
               permissionCode.equals("PAYMENT_QUERY_OWN") ||   // 查询个人缴费
               permissionCode.equals("PAYMENT_HISTORY_OWN") || // 查询个人缴费记录
               permissionCode.equals("PARKING_VIEW_OWN") ||    // 查看个人车位
               permissionCode.equals("PROFILE_VIEW") ||         // 查看个人信息
               permissionCode.equals("PASSWORD_CHANGE") ||      // 修改密码
               permissionCode.equals("SYSTEM_EXIT");            // 系统退出
    }
    
    /**
     * 获取权限类型文本
     * @param permissionType 权限类型
     * @return 权限类型文本
     */
    private String getPermissionTypeText(int permissionType) {
        switch (permissionType) {
            case 1:
                return "菜单";
            case 2:
                return "按钮";
            case 3:
                return "接口";
            default:
                return "未知";
        }
    }
    
    /**
     * 全选
     */
    private void selectAll() {
        selectedPermissionIDs.clear();
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int permissionID = (int) tableModel.getValueAt(i, 1);
            selectedPermissionIDs.add(permissionID);
            tableModel.setValueAt(true, i, 0);
        }
    }
    
    /**
     * 全不选
     */
    private void deselectAll() {
        selectedPermissionIDs.clear();
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(false, i, 0);
        }
    }
    
    /**
     * 保存权限
     */
    private void savePermissions() {
        if (currentRoleID <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择角色！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 保存权限
        boolean success = rolePermissionController.assignPermissions(currentRoleID, selectedPermissionIDs);
        if (success) {
            JOptionPane.showMessageDialog(this, "权限分配成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "权限分配失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}