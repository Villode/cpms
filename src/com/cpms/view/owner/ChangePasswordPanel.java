package com.cpms.view.owner;

import com.cpms.controller.common.UserController;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 修改密码面板
 */
public class ChangePasswordPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton submitButton;
    private JButton resetButton;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    
    /**
     * 构造方法
     * @param user 当前用户
     */
    public ChangePasswordPanel(User user) {
        this.currentUser = user;
        
        // 设置面板属性
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // 初始化组件
        initComponents();
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        // 创建标题面板
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("修改密码");
        titleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.large", 18)));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        titlePanel.add(titleLabel);
        
        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        
        // 原密码
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel oldPasswordLabel = new JLabel("原密码:");
        oldPasswordLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        oldPasswordLabel.setForeground(TEXT_COLOR);
        formPanel.add(oldPasswordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        oldPasswordField = new JPasswordField();
        oldPasswordField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        formPanel.add(oldPasswordField, gbc);
        
        // 新密码
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel newPasswordLabel = new JLabel("新密码:");
        newPasswordLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        newPasswordLabel.setForeground(TEXT_COLOR);
        formPanel.add(newPasswordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        newPasswordField = new JPasswordField();
        newPasswordField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        formPanel.add(newPasswordField, gbc);
        
        // 确认新密码
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel confirmPasswordLabel = new JLabel("确认新密码:");
        confirmPasswordLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        confirmPasswordLabel.setForeground(TEXT_COLOR);
        formPanel.add(confirmPasswordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        formPanel.add(confirmPasswordField, gbc);
        
        // 提示信息
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel tipLabel = new JLabel("* 密码长度不少于6位，建议使用字母、数字和特殊字符组合");
        tipLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.ITALIC, 
                config.getIntProperty("ui.font.size.small", 12)));
        tipLabel.setForeground(TEXT_COLOR);
        formPanel.add(tipLabel, gbc);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        resetButton = new JButton("重置");
        resetButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        resetButton.setBackground(Color.LIGHT_GRAY);
        resetButton.setForeground(Color.BLACK);
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        submitButton = new JButton("提交");
        submitButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        submitButton.setBackground(PRIMARY_COLOR);
        submitButton.setForeground(Color.WHITE);
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 添加按钮点击事件
        resetButton.addActionListener(e -> {
            oldPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        });
        
        submitButton.addActionListener(e -> {
            changePassword();
        });
        
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(submitButton);
        
        // 添加组件到面板
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 修改密码
     */
    private void changePassword() {
        // 获取输入
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // 验证输入
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写所有密码字段", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "新密码长度不能少于6位", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "两次输入的新密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // 创建用户控制器
            UserController userController = new UserController();
            
            // 修改密码
            boolean result = userController.changePassword(currentUser.getUserID(), oldPassword, newPassword);
            
            if (result) {
                JOptionPane.showMessageDialog(this, "密码修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                // 清空输入框
                oldPasswordField.setText("");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "密码修改失败，原密码可能不正确", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "发生错误：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}