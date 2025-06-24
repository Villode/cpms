package com.cpms.view.manager;

import com.cpms.controller.manager.OwnerController;
import com.cpms.model.dao.ParkingSpotDAO;
import com.cpms.model.entity.ParkingSpot;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * 分配车位对话框
 * 用于将车位分配给业主
 */
public class AssignParkingDialog extends JDialog {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 界面组件
    private JComboBox<User> ownerComboBox;
    private JTextField licensePlateField;
    private JButton confirmButton;
    private JButton cancelButton;
    private JButton clearButton;
    
    // 数据
    private ParkingSpot parkingSpot;
    private List<User> ownerList;
    private boolean confirmed = false;
    private User currentUser; // 当前登录用户
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color WARNING_COLOR = Color.decode(config.getProperty("ui.color.warning", "#FF8F00"));
    
    /**
     * 构造方法
     * @param parent 父窗口
     * @param parkingSpot 要分配的车位
     * @param currentUser 当前登录用户
     */
    public AssignParkingDialog(Window parent, ParkingSpot parkingSpot, User currentUser) {
        super(parent, "分配车位", ModalityType.APPLICATION_MODAL);
        this.parkingSpot = parkingSpot;
        this.currentUser = currentUser;
        
        // 初始化界面
        initComponents();
        
        // 加载数据
        loadOwnerData();
        
        // 填充现有数据
        fillData();
        
        // 设置对话框属性
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
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
        JLabel titleLabel = new JLabel("分配车位给业主");
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
        
        // 车位信息面板
        JPanel infoPanel = createInfoPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(infoPanel, gbc);
        
        // 重置网格设置
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        
        // 业主选择
        JLabel ownerLabel = new JLabel("选择业主:");
        ownerLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(ownerLabel, gbc);
        
        ownerComboBox = new JComboBox<>();
        ownerComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        ownerComboBox.setPreferredSize(new Dimension(300, 30));
        ownerComboBox.setRenderer(new OwnerComboBoxRenderer());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(ownerComboBox, gbc);
        
        // 车牌号
        JLabel licensePlateLabel = new JLabel("车牌号:");
        licensePlateLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(licensePlateLabel, gbc);
        
        licensePlateField = new JTextField(30);
        licensePlateField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        licensePlateField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(licensePlateField, gbc);
        
        // 提示信息
        JLabel tipLabel = new JLabel("<html><font color='gray'>提示：车牌号可选填，格式如：京A12345</font></html>");
        tipLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(tipLabel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建车位信息面板
     */
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel infoLabel = new JLabel(String.format(
                "<html><b>车位信息</b><br>" +
                "车位编号：%s<br>" +
                "位置：%s<br>" +
                "当前状态：%s</html>",
                parkingSpot.getSpotNumber(),
                parkingSpot.getLocation(),
                parkingSpot.getUsageStatus() == 1 ? "已占用" : "空闲"
        ));
        infoLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        
        panel.add(infoLabel, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * 创建按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        // 确认分配按钮
        confirmButton = new JButton("确认分配");
        confirmButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        confirmButton.setBackground(PRIMARY_COLOR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setPreferredSize(new Dimension(100, 35));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmAssign();
            }
        });
        
        // 清除分配按钮
        clearButton = new JButton("清除分配");
        clearButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        clearButton.setBackground(WARNING_COLOR);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        clearButton.setPreferredSize(new Dimension(100, 35));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAssignment();
            }
        });
        
        // 取消按钮
        cancelButton = new JButton("取消");
        cancelButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        panel.add(confirmButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(clearButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(cancelButton);
        
        return panel;
    }
    
    /**
     * 加载业主数据
     */
    private void loadOwnerData() {
        OwnerController ownerController = new OwnerController();
        
        // 根据用户角色加载不同的业主列表
        if (currentUser != null && currentUser.getRoleID() == 2) { // 管家角色
            // 只加载管家负责楼栋的业主
            ownerList = ownerController.getOwnersByManagerID(currentUser.getUserID());
        } else {
            // 其他角色加载所有业主
            ownerList = ownerController.getAllOwners();
        }
        
        ownerComboBox.removeAllItems();
        ownerComboBox.addItem(null); // 添加空选项
        
        if (ownerList != null) {
            for (User owner : ownerList) {
                ownerComboBox.addItem(owner);
            }
        }
    }
    
    /**
     * 填充现有数据
     */
    private void fillData() {
        // 如果车位已分配，选中对应业主
        if (parkingSpot.getOwnerUserID() != null && ownerList != null) {
            for (User owner : ownerList) {
                if (owner.getUserID() == parkingSpot.getOwnerUserID()) {
                    ownerComboBox.setSelectedItem(owner);
                    break;
                }
            }
        }
        
        // 设置车牌号
        if (parkingSpot.getLicensePlate() != null) {
            licensePlateField.setText(parkingSpot.getLicensePlate());
        }
        
        // 根据当前状态设置按钮可用性
        updateButtonStates();
    }
    
    /**
     * 更新按钮状态
     */
    private void updateButtonStates() {
        boolean hasOwner = parkingSpot.getOwnerUserID() != null;
        clearButton.setEnabled(hasOwner);
    }
    
    /**
     * 确认分配车位
     */
    private void confirmAssign() {
        // 验证输入
        if (!validateInput()) {
            return;
        }
        
        try {
            User selectedOwner = (User) ownerComboBox.getSelectedItem();
            String licensePlate = licensePlateField.getText().trim();
            
            // 更新车位信息
            if (selectedOwner != null) {
                parkingSpot.setOwnerUserID(selectedOwner.getUserID());
                parkingSpot.setUsageStatus(1); // 设为已占用
            } else {
                parkingSpot.setOwnerUserID(null);
                parkingSpot.setUsageStatus(0); // 设为空闲
            }
            
            parkingSpot.setLicensePlate(licensePlate.isEmpty() ? null : licensePlate);
            
            // 保存到数据库
            ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
            boolean success = parkingSpotDAO.updateParkingSpot(parkingSpot);
            
            if (success) {
                confirmed = true;
                String message = selectedOwner != null ? 
                        "车位分配成功！" : "车位已清除分配！";
                JOptionPane.showMessageDialog(this, message, "成功", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "车位分配失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
            
            parkingSpotDAO.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "分配车位时发生错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 清除车位分配
     */
    private void clearAssignment() {
        int result = JOptionPane.showConfirmDialog(this, 
                "确定要清除该车位的分配吗？", 
                "确认清除", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            ownerComboBox.setSelectedIndex(0); // 选择空选项
            licensePlateField.setText("");
            confirmAssign(); // 执行分配操作（实际是清除）
        }
    }
    
    /**
     * 验证输入数据
     */
    private boolean validateInput() {
        // 验证车牌号格式（如果不为空）
        String licensePlate = licensePlateField.getText().trim();
        if (!licensePlate.isEmpty() && !isValidLicensePlate(licensePlate)) {
            JOptionPane.showMessageDialog(this, "车牌号格式不正确，请输入正确的车牌号", "输入错误", JOptionPane.WARNING_MESSAGE);
            licensePlateField.requestFocus();
            return false;
        }
        
        // 如果选择了业主但没有输入车牌号，给出提示
        User selectedOwner = (User) ownerComboBox.getSelectedItem();
        if (selectedOwner != null && licensePlate.isEmpty()) {
            int result = JOptionPane.showConfirmDialog(this, 
                    "您选择了业主但未输入车牌号，确定要继续吗？", 
                    "确认", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 验证车牌号格式
     */
    private boolean isValidLicensePlate(String licensePlate) {
        // 简单的车牌号验证：中文字符+字母+数字，长度7-8位
        String pattern = "^[\u4e00-\u9fa5][A-Z][A-Z0-9]{5,6}$";
        return licensePlate.matches(pattern);
    }
    
    /**
     * 获取确认状态
     */
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * 业主下拉框渲染器
     */
    private class OwnerComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value == null) {
                setText("-- 请选择业主 --");
                setForeground(Color.GRAY);
            } else if (value instanceof User) {
                User owner = (User) value;
                String displayText = owner.getRealName() != null ? 
                        owner.getRealName() + " (" + owner.getUsername() + ")" : 
                        owner.getUsername();
                setText(displayText);
            }
            
            return this;
        }
    }
}