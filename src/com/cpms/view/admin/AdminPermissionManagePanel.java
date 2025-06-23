package com.cpms.view.admin;

import com.cpms.controller.admin.PermissionController;
import com.cpms.controller.admin.RolePermissionController;
import com.cpms.model.entity.Permission;
import com.cpms.util.AdminPermissionInitializer;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 系统权限初始化面板
 * 提供系统权限初始化功能，与角色权限分配功能分离
 */
public class AdminPermissionManagePanel extends JPanel {
    
    // 控制器
    private PermissionController permissionController;
    private RolePermissionController rolePermissionController;
    private AdminPermissionInitializer permissionInitializer;
    
    // 界面组件
    private JTable permissionTable;
    private DefaultTableModel tableModel;
    private JButton initPermissionsButton;
    private JButton refreshButton;
    private JLabel statusLabel;
    
    // 配置管理器
    private ConfigManager config;
    
    // 颜色常量
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    
    /**
     * 构造方法
     */
    public AdminPermissionManagePanel() {
        this.permissionController = new PermissionController();
        this.rolePermissionController = new RolePermissionController();
        this.permissionInitializer = new AdminPermissionInitializer();
        this.config = ConfigManager.getInstance();
        
        initComponents();
        loadPermissions();
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建标题面板
        add(createTitlePanel(), BorderLayout.NORTH);
        
        // 创建内容面板
        add(createContentPanel(), BorderLayout.CENTER);
        
        // 创建按钮面板
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    /**
     * 创建标题面板
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("系统权限初始化");
        titleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        
        JLabel descLabel = new JLabel("初始化系统基础权限数据，角色权限分配请使用角色权限管理功能");
        descLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        descLabel.setForeground(Color.DARK_GRAY);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(descLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * 创建内容面板
     */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 创建权限表格
        createPermissionTable();
        JScrollPane scrollPane = new JScrollPane(permissionTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建权限表格
     */
    private void createPermissionTable() {
        String[] columnNames = {"权限ID", "权限名称", "权限代码", "权限描述", "权限类型", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        permissionTable = new JTable(tableModel);
        permissionTable.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 12));
        permissionTable.setForeground(Color.BLACK);
        permissionTable.setRowHeight(30);
        permissionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        permissionTable.getTableHeader().setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 12));
        permissionTable.getTableHeader().setBackground(Color.WHITE);
        permissionTable.getTableHeader().setForeground(Color.BLACK);
        
        // 设置列宽
        permissionTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        permissionTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        permissionTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        permissionTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        permissionTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        permissionTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        // 设置状态列的渲染器
        permissionTable.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());
    }
    
    /**
     * 创建按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // 操作按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        initPermissionsButton = createStyledButton("初始化权限", SUCCESS_COLOR);
        initPermissionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializePermissions();
            }
        });
        
        refreshButton = createStyledButton("刷新", WARNING_COLOR);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPermissions();
            }
        });
        
        buttonPanel.add(initPermissionsButton);
        buttonPanel.add(refreshButton);
        
        // 状态标签
        statusLabel = new JLabel("就绪");
        statusLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 12));
        statusLabel.setForeground(Color.DARK_GRAY);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * 创建样式化按钮
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
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
        try {
            statusLabel.setText("正在加载权限列表...");
            
            // 清空表格
            tableModel.setRowCount(0);
            
            // 获取所有权限
            List<Permission> permissions = permissionController.getAllPermissions();
            
            for (Permission permission : permissions) {
                String permissionType = getPermissionTypeText(permission.getPermissionType());
                String status = "已配置";
                
                Object[] rowData = {
                    permission.getPermissionID(),
                    permission.getPermissionName(),
                    permission.getPermissionCode(),
                    permission.getPermissionDesc(),
                    permissionType,
                    status
                };
                tableModel.addRow(rowData);
            }
            
            statusLabel.setText("权限列表加载完成，共 " + permissions.size() + " 个权限");
            
        } catch (Exception e) {
            statusLabel.setText("加载权限列表失败: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "加载权限列表失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 初始化系统权限
     */
    private void initializePermissions() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "确定要初始化系统权限吗？\n这将添加所有预定义的权限到系统中。",
            "确认初始化",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                statusLabel.setText("正在初始化权限...");
                
                // 在后台线程中执行初始化
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            permissionInitializer.initializeSystemPermissions();
                            
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    statusLabel.setText("权限初始化完成");
                                    loadPermissions();
                                    JOptionPane.showMessageDialog(
                                        AdminPermissionManagePanel.this,
                                        "权限初始化完成！",
                                        "成功",
                                        JOptionPane.INFORMATION_MESSAGE
                                    );
                                }
                            });
                        } catch (Exception e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    statusLabel.setText("权限初始化失败: " + e.getMessage());
                                    JOptionPane.showMessageDialog(
                                        AdminPermissionManagePanel.this,
                                        "权限初始化失败: " + e.getMessage(),
                                        "错误",
                                        JOptionPane.ERROR_MESSAGE
                                    );
                                }
                            });
                        }
                    }
                });
                
            } catch (Exception e) {
                statusLabel.setText("权限初始化失败: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "权限初始化失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    /**
     * 获取权限类型文本
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
     * 状态列渲染器
     */
    private class StatusCellRenderer extends JLabel implements TableCellRenderer {
        
        public StatusCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            
            if ("已配置".equals(value.toString())) {
                setBackground(Color.WHITE);
                setForeground(SUCCESS_COLOR);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.GRAY);
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            
            return this;
        }
    }
}