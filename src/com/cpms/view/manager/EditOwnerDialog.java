package com.cpms.view.manager;

import com.cpms.controller.manager.OwnerController;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 修改业主信息对话框
 */
public class EditOwnerDialog extends JDialog {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private OwnerController ownerController;
    
    // 界面组件
    private JTextField realNameField;
    private JTextField phoneField;
    private JTextField roomNumberField;
    private JComboBox<String> buildingComboBox;
    private JComboBox<String> statusComboBox;
    
    // 数据
    private User owner;
    private List<Building> buildingList;
    private boolean success = false;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    
    /**
     * 构造方法
     * @param parent 父窗口
     * @param owner 要修改的业主信息
     * @param buildingList 楼栋列表
     */
    public EditOwnerDialog(Window parent, User owner, List<Building> buildingList) {
        super(parent, "修改业主信息", ModalityType.APPLICATION_MODAL);
        this.owner = owner;
        this.buildingList = buildingList;
        
        // 初始化控制器
        ownerController = new OwnerController();
        
        // 初始化界面
        initComponents();
        
        // 填充数据
        fillData();
        
        // 设置对话框属性
        setSize(550, 450);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(SECONDARY_COLOR);
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // 标题
        JLabel titleLabel = new JLabel("修改业主信息");
        titleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        
        // 重置网格设置
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // 用户名（只读）
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(usernameLabel, gbc);
        
        JLabel usernameValueLabel = new JLabel(owner.getUsername());
        usernameValueLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        usernameValueLabel.setForeground(Color.GRAY);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(usernameValueLabel, gbc);
        
        // 真实姓名
        JLabel realNameLabel = new JLabel("真实姓名:");
        realNameLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(realNameLabel, gbc);
        
        realNameField = new JTextField(30);
        realNameField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        realNameField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(realNameField, gbc);
        
        // 电话号码
        JLabel phoneLabel = new JLabel("电话号码:");
        phoneLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(phoneLabel, gbc);
        
        phoneField = new JTextField(30);
        phoneField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        phoneField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(phoneField, gbc);
        
        // 房间号
        JLabel roomNumberLabel = new JLabel("房间号:");
        roomNumberLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(roomNumberLabel, gbc);
        
        roomNumberField = new JTextField(30);
        roomNumberField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        roomNumberField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(roomNumberField, gbc);
        
        // 所属楼栋
        JLabel buildingLabel = new JLabel("所属楼栋:");
        buildingLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(buildingLabel, gbc);
        
        buildingComboBox = new JComboBox<>();
        buildingComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        buildingComboBox.setPreferredSize(new Dimension(300, 30));
        for (Building building : buildingList) {
            buildingComboBox.addItem(building.getBuildingName());
        }
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buildingComboBox, gbc);
        
        // 账号状态
        JLabel statusLabel = new JLabel("账号状态:");
        statusLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(statusLabel, gbc);
        
        statusComboBox = new JComboBox<>(new String[]{"启用", "禁用"});
        statusComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        statusComboBox.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(statusComboBox, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建按钮面板
     * @return 按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(SECONDARY_COLOR);
        
        // 确认按钮
        JButton confirmButton = createStyledButton("确认修改", SUCCESS_COLOR);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmEdit();
            }
        });
        
        // 取消按钮
        JButton cancelButton = createStyledButton("取消", ERROR_COLOR);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        panel.add(confirmButton);
        panel.add(cancelButton);
        
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
        button.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * 填充数据
     */
    private void fillData() {
        realNameField.setText(owner.getRealName());
        phoneField.setText(owner.getPhone());
        roomNumberField.setText(owner.getRoomNumber() != null ? owner.getRoomNumber() : "");
        
        // 设置楼栋选择
        if (owner.getBuildingID() != null) {
            for (int i = 0; i < buildingList.size(); i++) {
                if (buildingList.get(i).getBuildingID() == owner.getBuildingID()) {
                    buildingComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        // 设置账号状态
        statusComboBox.setSelectedIndex(owner.getAccountStatus() == 1 ? 0 : 1);
    }
    
    /**
     * 确认修改
     */
    private void confirmEdit() {
        // 验证输入
        if (!validateInput()) {
            return;
        }
        
        // 获取输入数据
        String realName = realNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String roomNumber = roomNumberField.getText().trim();
        int buildingID = buildingList.get(buildingComboBox.getSelectedIndex()).getBuildingID();
        int accountStatus = statusComboBox.getSelectedIndex() == 0 ? 1 : 0;
        
        // 更新业主信息
        boolean result = ownerController.updateOwner(owner.getUserID(), realName, phone, roomNumber, buildingID, accountStatus);
        
        if (result) {
            success = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "修改业主信息失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 验证输入
     * @return 是否通过验证
     */
    private boolean validateInput() {
        String realName = realNameField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (realName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入真实姓名！", "提示", JOptionPane.WARNING_MESSAGE);
            realNameField.requestFocus();
            return false;
        }
        
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入电话号码！", "提示", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return false;
        }
        
        // 验证电话号码格式
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "请输入正确的手机号码格式！", "提示", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return false;
        }
        
        if (buildingComboBox.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "请选择所属楼栋！", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * 获取操作是否成功
     * @return 是否成功
     */
    public boolean isSuccess() {
        return success;
    }
}