package com.cpms.view.admin;

import com.cpms.controller.admin.RoleController;
import com.cpms.model.entity.Role;
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
 * 角色管理面板
 * 提供角色的增删改查功能
 */
public class RoleManagementPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private RoleController roleController;
    
    // 界面组件
    private JTable roleTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextArea descArea;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    
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
     * 构造方法，初始化角色管理面板
     */
    public RoleManagementPanel() {
        // 初始化控制器
        roleController = new RoleController();
        
        // 设置布局
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 创建界面组件
        initComponents();
        
        // 加载角色数据
        loadRoleData();
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
        panel.setBorder(BorderFactory.createTitledBorder("角色列表"));
        
        // 创建表格模型
        String[] columnNames = {"ID", "角色名称", "角色描述", "创建时间", "更新时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        // 创建表格
        roleTable = new JTable(tableModel);
        roleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roleTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        roleTable.getTableHeader().setReorderingAllowed(false);
        
        // 设置列宽
        roleTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        roleTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        roleTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        roleTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        roleTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        // 添加表格选择监听器
        roleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = roleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    currentRoleID = (int) tableModel.getValueAt(selectedRow, 0);
                    nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    descArea.setText((String) tableModel.getValueAt(selectedRow, 2));
                    
                    // 更新按钮状态
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(currentRoleID > 3); // 系统内置角色不允许删除
                }
            }
        });
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(roleTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建表单面板
     * @return 表单面板
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("角色信息"));
        panel.setPreferredSize(new Dimension(350, 0));
        
        // 创建表单内容面板
        JPanel formContent = new JPanel(new GridBagLayout());
        formContent.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;
        
        // 角色名称标签和输入框
        JLabel nameLabel = new JLabel("角色名称:");
        nameLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        nameField = new JTextField(25);
        nameField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        nameField.setPreferredSize(new Dimension(0, 30));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formContent.add(nameLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formContent.add(nameField, gbc);
        
        // 角色描述标签和文本区域
        JLabel descLabel = new JLabel("角色描述:");
        descLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        descArea = new JTextArea(8, 25);
        descArea.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descArea);
        descScrollPane.setPreferredSize(new Dimension(0, 150));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 5, 8, 5);
        formContent.add(descLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        formContent.add(descScrollPane, gbc);
        
        panel.add(formContent, BorderLayout.CENTER);
        
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
                addRole();
            }
        });
        
        // 更新按钮
        updateButton = createStyledButton("更新", PRIMARY_COLOR);
        updateButton.setEnabled(false);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRole();
            }
        });
        
        // 删除按钮
        deleteButton = createStyledButton("删除", ERROR_COLOR);
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRole();
            }
        });
        
        // 清空按钮
        clearButton = createStyledButton("清空", ACCENT_COLOR);
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
     * 加载角色数据
     */
    private void loadRoleData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 获取所有角色
        List<Role> roles = roleController.getAllRoles();
        if (roles != null) {
            for (Role role : roles) {
                Object[] rowData = {
                    role.getRoleID(),
                    role.getRoleName(),
                    role.getRoleDesc(),
                    role.getCreateTime(),
                    role.getUpdateTime()
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    /**
     * 添加角色
     */
    private void addRole() {
        String roleName = nameField.getText().trim();
        String roleDesc = descArea.getText().trim();
        
        if (roleName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "角色名称不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = roleController.addRole(roleName, roleDesc);
        if (success) {
            JOptionPane.showMessageDialog(this, "角色添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadRoleData();
        } else {
            JOptionPane.showMessageDialog(this, "角色添加失败，可能是角色名已存在！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 更新角色
     */
    private void updateRole() {
        if (currentRoleID < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要更新的角色！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String roleName = nameField.getText().trim();
        String roleDesc = descArea.getText().trim();
        
        if (roleName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "角色名称不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = roleController.updateRole(currentRoleID, roleName, roleDesc);
        if (success) {
            JOptionPane.showMessageDialog(this, "角色更新成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadRoleData();
        } else {
            JOptionPane.showMessageDialog(this, "角色更新失败，可能是角色名已存在！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 删除角色
     */
    private void deleteRole() {
        if (currentRoleID < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的角色！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (currentRoleID <= 3) {
            JOptionPane.showMessageDialog(this, "系统内置角色不允许删除！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该角色吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = roleController.deleteRole(currentRoleID);
            if (success) {
                JOptionPane.showMessageDialog(this, "角色删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadRoleData();
            } else {
                JOptionPane.showMessageDialog(this, "角色删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 清空表单
     */
    private void clearForm() {
        currentRoleID = -1;
        nameField.setText("");
        descArea.setText("");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        roleTable.clearSelection();
    }
} 