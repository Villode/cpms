package com.cpms.view.owner;

import com.cpms.model.dao.BuildingDAO;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 * 业主个人信息查看面板
 */
public class ProfileViewPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 当前用户
    private User currentUser;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法
     * @param user 当前用户
     */
    public ProfileViewPanel(User user) {
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
        
        JLabel titleLabel = new JLabel("个人信息");
        titleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.large", 18)));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        titlePanel.add(titleLabel);
        
        // 创建信息面板
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 用户名
        addInfoField(infoPanel, gbc, 0, "用户名:", currentUser.getUsername());
        
        // 真实姓名
        String realName = (currentUser.getRealName() != null) ? currentUser.getRealName() : "未设置";
        addInfoField(infoPanel, gbc, 1, "真实姓名:", realName);
        
        // 联系电话
        String phone = (currentUser.getPhone() != null) ? currentUser.getPhone() : "未设置";
        addInfoField(infoPanel, gbc, 2, "联系电话:", phone);
        
        // 所属楼栋
        String buildingName = "未知";
        if (currentUser.getBuildingID() != null) {
            try {
                BuildingDAO buildingDAO = new BuildingDAO();
                Building building = buildingDAO.findByID(currentUser.getBuildingID());
                if (building != null) {
                    buildingName = building.getBuildingName();
                }
                buildingDAO.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        addInfoField(infoPanel, gbc, 3, "所属楼栋:", buildingName);
        
        // 账号状态
        String status = (currentUser.getAccountStatus() == 1) ? "正常" : "禁用";
        addInfoField(infoPanel, gbc, 4, "账号状态:", status);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton editButton = new JButton("编辑资料");
        editButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        editButton.setBackground(PRIMARY_COLOR);
        editButton.setForeground(Color.WHITE);
        editButton.setOpaque(true);
        editButton.setBorderPainted(false);
        editButton.setFocusPainted(false);
        editButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 添加编辑按钮点击事件
        editButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                    "编辑资料功能尚未实现，请联系管家修改个人信息。", 
                    "提示", 
                    JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonPanel.add(editButton);
        
        // 添加组件到面板
        add(titlePanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 添加信息字段
     * @param panel 面板
     * @param gbc 网格约束
     * @param row 行号
     * @param labelText 标签文本
     * @param valueText 值文本
     */
    private void addInfoField(JPanel panel, GridBagConstraints gbc, int row, String labelText, String valueText) {
        // 标签
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        label.setForeground(TEXT_COLOR);
        panel.add(label, gbc);
        
        // 值
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel value = new JLabel(valueText);
        value.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        value.setForeground(TEXT_COLOR);
        panel.add(value, gbc);
    }
}