package com.cpms.view.common;

import com.cpms.controller.common.LoginController;
import com.cpms.model.dao.RoleDAO;
import com.cpms.model.entity.Role;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;
import com.cpms.view.admin.AdminMainFrame;
import com.cpms.view.manager.ManagerMainFrame;
import com.cpms.view.owner.OwnerMainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录界面
 * 系统入口界面，提供用户登录功能
 */
public class LoginFrame extends JFrame {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 界面组件
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ButtonGroup roleButtonGroup;
    private Map<String, JRadioButton> roleButtons;
    private Map<String, Integer> roleNameToIdMap;
    private JButton loginButton;
    private JCheckBox rememberCheckBox;
    
    // 控制器
    private LoginController loginController;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    private final Color TEXT_LIGHT_COLOR = Color.decode(config.getProperty("ui.color.text.light", "#9E9E9E"));
    
    /**
     * 构造方法，初始化登录界面
     */
    public LoginFrame() {
        // 初始化控制器
        loginController = new LoginController();
        
        // 设置窗口属性
        setTitle(config.getProperty("system.name", "小区物业管理系统") + " - 登录");
        setSize(500, 450); // 增加窗口高度
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // 创建界面
        initComponents();
        
        // 显示界面
        setVisible(true);
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // 创建标题面板
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JLabel titleLabel = new JLabel(config.getProperty("system.name", "小区物业管理系统"), JLabel.CENTER);
        titleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.title", 28)));
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // 创建表单面板
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null); // 使用绝对布局以便精确定位
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        formPanel.setPreferredSize(new Dimension(400, 280)); // 设置表单面板的首选大小
        
        // 用户名标签和输入框
        JLabel usernameLabel = new JLabel("用户名");
        usernameLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        usernameLabel.setBounds(80, 20, 80, 25);
        usernameLabel.setForeground(TEXT_COLOR);
        
        usernameField = new JTextField();
        usernameField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        usernameField.setBounds(160, 20, 200, 30);
        
        // 密码标签和输入框
        JLabel passwordLabel = new JLabel("密码");
        passwordLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        passwordLabel.setBounds(80, 70, 80, 25);
        passwordLabel.setForeground(TEXT_COLOR);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        passwordField.setBounds(160, 70, 200, 30);
        
        // 角色选择标签和单选按钮组
        JLabel roleLabel = new JLabel("登录角色");
        roleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        roleLabel.setBounds(80, 120, 80, 25);
        roleLabel.setForeground(TEXT_COLOR);
        
        // 创建角色单选按钮面板
        JPanel rolePanel = new JPanel();
        rolePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rolePanel.setBackground(Color.WHITE);
        rolePanel.setBounds(160, 120, 280, 30);
        
        // 初始化角色按钮组和映射
        roleButtonGroup = new ButtonGroup();
        roleButtons = new HashMap<>();
        roleNameToIdMap = new HashMap<>();
        
        // 加载角色列表
        loadRoles(rolePanel);
        
        // 记住密码复选框
        rememberCheckBox = new JCheckBox("记住密码");
        rememberCheckBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        rememberCheckBox.setBounds(160, 170, 100, 25);
        rememberCheckBox.setBackground(Color.WHITE);
        rememberCheckBox.setForeground(TEXT_COLOR);
        
        // 登录按钮
        loginButton = new JButton("登录");
        loginButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        loginButton.setBounds(160, 210, 200, 40);
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 鼠标悬停效果
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(ACCENT_COLOR);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        // 添加组件到表单面板
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(roleLabel);
        formPanel.add(rolePanel);
        formPanel.add(rememberCheckBox);
        formPanel.add(loginButton);
        

        
        // 添加到主面板
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        
        // 设置内容面板
        setContentPane(mainPanel);
        
        // 添加事件监听
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        // 设置回车键登录
        getRootPane().setDefaultButton(loginButton);
    }
    
    /**
     * 加载角色列表并创建单选按钮
     * @param rolePanel 角色面板
     */
    private void loadRoles(JPanel rolePanel) {
        try {
            RoleDAO roleDAO = new RoleDAO();
            List<Role> roles = roleDAO.findAll();
            
            boolean firstRole = true;
            for (Role role : roles) {
                JRadioButton radioButton = new JRadioButton(role.getRoleName());
                radioButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                        config.getIntProperty("ui.font.size.normal", 14)));
                radioButton.setBackground(Color.WHITE);
                radioButton.setForeground(TEXT_COLOR);
                
                // 第一个角色默认选中
                if (firstRole) {
                    radioButton.setSelected(true);
                    firstRole = false;
                }
                
                roleButtonGroup.add(radioButton);
                rolePanel.add(radioButton);
                
                // 保存角色名称和ID的映射关系
                roleButtons.put(role.getRoleName(), radioButton);
                roleNameToIdMap.put(role.getRoleName(), role.getRoleID());
            }
            
            roleDAO.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载角色列表失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 获取选中的角色名称
     * @return 选中的角色名称，如果没有选中则返回null
     */
    private String getSelectedRoleName() {
        for (Map.Entry<String, JRadioButton> entry : roleButtons.entrySet()) {
            if (entry.getValue().isSelected()) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * 登录方法
     */
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String roleName = getSelectedRoleName();
        
        System.out.println("尝试登录 - 用户名: " + username + ", 角色: " + roleName);
        
        // 输入验证
        if (username.isEmpty() || password.isEmpty() || roleName == null) {
            JOptionPane.showMessageDialog(this, "用户名、密码和角色不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 调用控制器进行登录验证
        User user = loginController.loginWithRole(username, password, roleName);
        
        if (user != null) {
            // 登录成功
            System.out.println("登录成功 - 用户: " + user.getUsername() + ", 角色ID: " + user.getRoleID());
            openMainFrame(user);
        } else {
            // 登录失败
            System.out.println("登录失败 - 用户名或密码错误，或所选角色不匹配");
            JOptionPane.showMessageDialog(this, 
                "登录失败！\n\n可能的原因：\n" +
                "1. 用户名或密码错误\n" + 
                "2. 所选角色不匹配\n\n" +
                "请尝试使用默认密码：\n" +
                "- 管理员账号: admin/admin123\n" +
                "- 管家账号: 002/123456\n" +
                "- 业主账号: 用户名/123456", 
                "登录失败", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
    
    /**
     * 根据用户角色打开对应的主界面
     * @param user 用户对象
     */
    private void openMainFrame(User user) {
        // 关闭登录窗口
        dispose();
        
        // 根据角色打开对应主界面
        switch (user.getRoleID()) {
            case 1: // 管理员
                new AdminMainFrame(user);
                break;
            case 2: // 管家
                new ManagerMainFrame(user);
                break;
            case 3: // 业主
                new OwnerMainFrame(user);
                break;
            default:
                JOptionPane.showMessageDialog(null, "未知的用户角色！", "错误", JOptionPane.ERROR_MESSAGE);
                new LoginFrame(); // 重新打开登录界面
                break;
        }
    }
} 