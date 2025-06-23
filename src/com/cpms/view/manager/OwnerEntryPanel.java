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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 业主录入面板
 * 用于管家添加新业主
 */
public class OwnerEntryPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private OwnerController ownerController;
    private BuildingController buildingController;
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField realNameField;
    private JTextField phoneField;
    private JTextField roomNumberField;
    private JComboBox<String> buildingComboBox;
    private JButton submitButton;
    private JButton clearButton;
    private JButton generatePasswordButton;
    
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
     * 构造方法，初始化业主录入面板
     * @param user 当前用户
     */
    public OwnerEntryPanel(User user) {
        this.currentUser = user;
        
        // 初始化控制器
        ownerController = new OwnerController();
        buildingController = new BuildingController();
        
        // 初始化数据
        loadBuildingData();
        
        // 设置布局
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 创建界面组件
        initComponents();
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
        buildingIDs[0] = 0; // 第一个选项为"请选择楼栋"
        for (int i = 0; i < buildingList.size(); i++) {
            buildingIDs[i + 1] = buildingList.get(i).getBuildingID();
        }
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建表单面板
        JPanel formPanel = createFormPanel();
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加到主面板
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建表单面板
     * @return 表单面板
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                "业主信息录入",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                        config.getIntProperty("ui.font.size.large", 16)),
                PRIMARY_COLOR));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 用户名
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        usernameField = new JTextField(20);
        usernameField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 密码
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 生成密码按钮
        generatePasswordButton = new JButton("生成密码");
        generatePasswordButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        generatePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRandomPassword();
            }
        });
        
        // 真实姓名
        JLabel realNameLabel = new JLabel("真实姓名:");
        realNameLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        realNameField = new JTextField(20);
        realNameField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 联系电话
        JLabel phoneLabel = new JLabel("联系电话:");
        phoneLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        phoneField = new JTextField(20);
        phoneField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 房间号
        JLabel roomNumberLabel = new JLabel("房间号:");
        roomNumberLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        roomNumberField = new JTextField(20);
        roomNumberField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 所属楼栋
        JLabel buildingLabel = new JLabel("所属楼栋:");
        buildingLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        buildingComboBox = new JComboBox<>();
        buildingComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 添加楼栋选项
        buildingComboBox.addItem("请选择楼栋");
        for (int i = 0; i < buildingList.size(); i++) {
            Building building = buildingList.get(i);
            buildingComboBox.addItem(building.getBuildingName());
        }
        
        // 如果管家只负责一个楼栋，则默认选中该楼栋
        Integer managedBuildingID = currentUser.getManagedBuildingID();
        if (managedBuildingID != null && managedBuildingID > 0 && buildingList.size() == 1) {
            buildingComboBox.setSelectedIndex(1);
        }
        
        // 添加组件到面板
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(passwordField, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(generatePasswordButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(realNameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(realNameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(roomNumberLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(roomNumberField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(buildingLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(buildingComboBox, gbc);
        
        return panel;
    }
    
    /**
     * 创建按钮面板
     * @return 按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // 提交按钮
        submitButton = createStyledButton("提交", SUCCESS_COLOR);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitOwner();
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
        
        panel.add(submitButton);
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
     * 生成随机密码
     */
    private void generateRandomPassword() {
        String randomPassword = PasswordUtil.generateRandomPassword(8);
        passwordField.setText(randomPassword);
    }
    
    /**
     * 提交业主信息
     */
    private void submitOwner() {
        // 获取表单数据
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String realName = realNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String roomNumber = roomNumberField.getText().trim();
        int buildingIndex = buildingComboBox.getSelectedIndex();
        
        // 验证表单数据
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入密码！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (realName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入真实姓名！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (buildingIndex <= 0) {
            JOptionPane.showMessageDialog(this, "请选择所属楼栋！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 获取楼栋ID
        int buildingID = buildingIDs[buildingIndex];
        
        // 提交业主信息（包含房间号）
        boolean success = ownerController.addOwner(username, password, realName, phone, buildingID, roomNumber);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "业主添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "业主添加失败！可能用户名已存在。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 清空表单
     */
    private void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        realNameField.setText("");
        phoneField.setText("");
        roomNumberField.setText("");
        buildingComboBox.setSelectedIndex(0);
    }
}