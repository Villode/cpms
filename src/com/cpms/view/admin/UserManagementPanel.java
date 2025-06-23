package com.cpms.view.admin;

import com.cpms.controller.admin.UserController;
import com.cpms.model.entity.Role;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理面板
 * 提供用户的增删改查功能
 */
public class UserManagementPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private UserController userController;
    
    // 界面组件
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField realNameField;
    private JTextField phoneField;
    private JTextField roomNumberField;
    private JComboBox<String> roleComboBox;
    private JComboBox<String> statusComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton resetPasswordButton;
    private JButton clearButton;
    
    // 搜索相关组件
    private JTextField searchField;
    private JButton searchButton;
    private JButton resetSearchButton;
    
    // 角色ID与名称映射
    private Map<String, Integer> roleNameToIdMap;
    private Map<Integer, String> roleIdToNameMap;
    
    // 当前选中的用户ID
    private int currentUserID = -1;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法，初始化用户管理面板
     */
    public UserManagementPanel() {
        // 初始化控制器
        userController = new UserController();
        
        // 初始化角色映射
        initRoleMaps();
        
        // 设置布局
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 创建界面组件
        initComponents();
        
        // 加载用户数据
        loadUserData();
    }
    
    /**
     * 初始化角色ID与名称映射
     */
    private void initRoleMaps() {
        roleNameToIdMap = new HashMap<>();
        roleIdToNameMap = new HashMap<>();
        
        List<Role> roles = userController.getAllRoles();
        for (Role role : roles) {
            roleNameToIdMap.put(role.getRoleName(), role.getRoleID());
            roleIdToNameMap.put(role.getRoleID(), role.getRoleName());
        }
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
        panel.setBorder(BorderFactory.createTitledBorder("用户列表"));
        
        // 创建搜索面板
        JPanel searchPanel = createSearchPanel();
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // 创建表格模型
        String[] columnNames = {"ID", "用户名", "真实姓名", "联系电话", "房间号", "角色", "状态", "创建时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        // 创建表格
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        userTable.getTableHeader().setReorderingAllowed(false);
        
        // 设置列宽
        userTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        userTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        userTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        userTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        userTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        
        // 设置状态列的渲染器
        userTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    if ("启用".equals(value.toString())) {
                        c.setForeground(SUCCESS_COLOR);
                    } else {
                        c.setForeground(ERROR_COLOR);
                    }
                }
                return c;
            }
        });
        
        // 添加表格选择监听器
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    currentUserID = (int) tableModel.getValueAt(selectedRow, 0);
                    usernameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    passwordField.setText(""); // 清空密码字段
                    realNameField.setText((String) tableModel.getValueAt(selectedRow, 2));
                    phoneField.setText((String) tableModel.getValueAt(selectedRow, 3));
                    roomNumberField.setText((String) tableModel.getValueAt(selectedRow, 4));
                    
                    String roleName = (String) tableModel.getValueAt(selectedRow, 5);
                    roleComboBox.setSelectedItem(roleName);
                    
                    String status = (String) tableModel.getValueAt(selectedRow, 6);
                    statusComboBox.setSelectedItem(status);
                    
                    // 更新按钮状态
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(currentUserID > 1); // 管理员账号不允许删除
                    resetPasswordButton.setEnabled(true);
                }
            }
        });
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建搜索面板
     * @return 搜索面板
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // 搜索标签
        JLabel searchLabel = new JLabel("搜索用户:");
        searchLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 搜索输入框
        searchField = new JTextField(20);
        searchField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setToolTipText("输入用户名、真实姓名或联系电话进行搜索");
        
        // 搜索按钮
        searchButton = createStyledButton("搜索", PRIMARY_COLOR);
        searchButton.setPreferredSize(new Dimension(80, 30));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchUsers();
            }
        });
        
        // 重置搜索按钮
        resetSearchButton = createStyledButton("重置", SECONDARY_COLOR);
        resetSearchButton.setPreferredSize(new Dimension(80, 30));
        resetSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSearch();
            }
        });
        
        // 添加回车键搜索功能
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchUsers();
            }
        });
        
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(resetSearchButton);
        
        return panel;
    }
    
    /**
     * 创建表单面板
     * @return 表单面板
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("用户信息"));
        panel.setPreferredSize(new Dimension(350, 0)); // 设置表单面板宽度
        
        // 创建表单内容面板
        JPanel formContent = new JPanel(new GridBagLayout());
        formContent.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;
        
        // 用户名标签和输入框
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        usernameField = new JTextField(25);
        usernameField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        usernameField.setPreferredSize(new Dimension(0, 30));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formContent.add(usernameLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formContent.add(usernameField, gbc);
        
        // 密码标签和输入框
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        passwordField = new JPasswordField(25);
        passwordField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        passwordField.setPreferredSize(new Dimension(0, 30));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formContent.add(passwordLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formContent.add(passwordField, gbc);
        
        // 真实姓名标签和输入框
        JLabel realNameLabel = new JLabel("真实姓名:");
        realNameLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        realNameField = new JTextField(25);
        realNameField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        realNameField.setPreferredSize(new Dimension(0, 30));
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        formContent.add(realNameLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        formContent.add(realNameField, gbc);
        
        // 联系电话标签和输入框
        JLabel phoneLabel = new JLabel("联系电话:");
        phoneLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        phoneField = new JTextField(25);
        phoneField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        phoneField.setPreferredSize(new Dimension(0, 30));
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        formContent.add(phoneLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        formContent.add(phoneField, gbc);
        
        // 房间号标签和输入框
        JLabel roomNumberTitleLabel = new JLabel("房间号:");
        roomNumberTitleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        roomNumberField = new JTextField(25);
        roomNumberField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        roomNumberField.setPreferredSize(new Dimension(0, 30));
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        formContent.add(roomNumberTitleLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 9;
        formContent.add(roomNumberField, gbc);
        
        // 角色标签和下拉框
        JLabel roleLabel = new JLabel("角色:");
        roleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        roleComboBox = new JComboBox<>();
        roleComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        roleComboBox.setPreferredSize(new Dimension(0, 30));
        
        // 添加角色选项
        for (String roleName : roleNameToIdMap.keySet()) {
            roleComboBox.addItem(roleName);
        }
        
        gbc.gridx = 0;
        gbc.gridy = 10;
        formContent.add(roleLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 11;
        formContent.add(roleComboBox, gbc);
        
        // 状态标签和下拉框
        JLabel statusLabel = new JLabel("状态:");
        statusLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        statusComboBox = new JComboBox<>(new String[]{"启用", "禁用"});
        statusComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        statusComboBox.setPreferredSize(new Dimension(0, 30));
        
        gbc.gridx = 0;
        gbc.gridy = 12;
        formContent.add(statusLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 13;
        formContent.add(statusComboBox, gbc);
        
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
                addUser();
            }
        });
        
        // 更新按钮
        updateButton = createStyledButton("更新", PRIMARY_COLOR);
        updateButton.setEnabled(false);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });
        
        // 删除按钮
        deleteButton = createStyledButton("删除", ERROR_COLOR);
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        
        // 重置密码按钮
        resetPasswordButton = createStyledButton("重置密码", ACCENT_COLOR);
        resetPasswordButton.setEnabled(false);
        resetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetPassword();
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
        panel.add(resetPasswordButton);
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
     * 加载用户数据
     */
    private void loadUserData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 获取所有用户
        List<User> users = userController.getAllUsers();
        if (users != null) {
            for (User user : users) {
                Object[] rowData = {
                    user.getUserID(),
                    user.getUsername(),
                    user.getRealName(),
                    user.getPhone(),
                    user.getRoomNumber() != null ? user.getRoomNumber() : "",
                    roleIdToNameMap.getOrDefault(user.getRoleID(), "未知"),
                    user.getAccountStatus() == 1 ? "启用" : "禁用",
                    user.getCreateTime()
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    /**
     * 添加用户
     */
    private void addUser() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String realName = realNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String roomNumber = roomNumberField.getText().trim();
        String roleName = (String) roleComboBox.getSelectedItem();
        String status = (String) statusComboBox.getSelectedItem();
        
        // 输入验证
        if (username.isEmpty() || password.isEmpty() || realName.isEmpty() || phone.isEmpty() || roleName == null || status == null) {
            JOptionPane.showMessageDialog(this, "用户名、密码、真实姓名和联系电话不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 获取角色ID和状态值
        int roleID = roleNameToIdMap.getOrDefault(roleName, 0);
        int accountStatus = "启用".equals(status) ? 1 : 0;
        
        // 添加用户
        boolean success = userController.addUser(username, password, realName, phone, roleID, null, null, accountStatus, roomNumber);
        if (success) {
            JOptionPane.showMessageDialog(this, "用户添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadUserData();
        } else {
            JOptionPane.showMessageDialog(this, "用户添加失败，可能是用户名已存在！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 更新用户
     */
    private void updateUser() {
        if (currentUserID < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要更新的用户！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String username = usernameField.getText().trim();
        String realName = realNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String roomNumber = roomNumberField.getText().trim();
        String roleName = (String) roleComboBox.getSelectedItem();
        String status = (String) statusComboBox.getSelectedItem();
        
        // 输入验证
        if (username.isEmpty() || realName.isEmpty() || phone.isEmpty() || roleName == null || status == null) {
            JOptionPane.showMessageDialog(this, "用户名、真实姓名和联系电话不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 获取角色ID和状态值
        int roleID = roleNameToIdMap.getOrDefault(roleName, 0);
        int accountStatus = "启用".equals(status) ? 1 : 0;
        
        // 更新用户
        boolean success = userController.updateUser(currentUserID, username, realName, phone, roleID, null, null, accountStatus, roomNumber);
        if (success) {
            JOptionPane.showMessageDialog(this, "用户更新成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadUserData();
        } else {
            JOptionPane.showMessageDialog(this, "用户更新失败，可能是用户名已存在！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 删除用户
     */
    private void deleteUser() {
        if (currentUserID < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的用户！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (currentUserID == 1) {
            JOptionPane.showMessageDialog(this, "管理员账号不允许删除！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该用户吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = userController.deleteUser(currentUserID);
            if (success) {
                JOptionPane.showMessageDialog(this, "用户删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, "用户删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 重置密码
     */
    private void resetPassword() {
        if (currentUserID < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要重置密码的用户！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String newPassword = JOptionPane.showInputDialog(this, "请输入新密码:", "重置密码", JOptionPane.QUESTION_MESSAGE);
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            boolean success = userController.resetPassword(currentUserID, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(this, "密码重置成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "密码重置失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 搜索用户
     */
    private void searchUsers() {
        String keyword = searchField.getText().trim();
        
        // 清空表格
        tableModel.setRowCount(0);
        
        // 执行搜索
        List<User> users = userController.searchUsers(keyword);
        if (users != null) {
            for (User user : users) {
                Object[] rowData = {
                    user.getUserID(),
                    user.getUsername(),
                    user.getRealName(),
                    user.getPhone(),
                    user.getRoomNumber() != null ? user.getRoomNumber() : "",
                    roleIdToNameMap.getOrDefault(user.getRoleID(), "未知"),
                    user.getAccountStatus() == 1 ? "启用" : "禁用",
                    user.getCreateTime()
                };
                tableModel.addRow(rowData);
            }
        }
        
        // 显示搜索结果统计
        if (keyword.isEmpty()) {
            // 如果搜索关键词为空，显示所有用户
        } else {
            int resultCount = users != null ? users.size() : 0;
            JOptionPane.showMessageDialog(this, 
                "搜索完成，找到 " + resultCount + " 个匹配的用户", 
                "搜索结果", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * 重置搜索
     */
    private void resetSearch() {
        searchField.setText("");
        loadUserData();
    }
    
    /**
     * 清空表单
     */
    private void clearForm() {
        currentUserID = -1;
        usernameField.setText("");
        passwordField.setText("");
        realNameField.setText("");
        phoneField.setText("");
        roomNumberField.setText("");
        if (roleComboBox.getItemCount() > 0) {
            roleComboBox.setSelectedIndex(0);
        }
        statusComboBox.setSelectedIndex(0);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        resetPasswordButton.setEnabled(false);
        userTable.clearSelection();
    }
}
