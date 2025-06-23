package com.cpms.view.owner;

import com.cpms.controller.owner.RepairController;
import com.cpms.model.entity.Repair;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Date;

/**
 * 报修提交面板
 */
public class RepairSubmitPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTextArea descriptionArea;
    private JTextField imagePathField;
    private JButton browseButton;
    private JButton submitButton;
    private JButton resetButton;
    
    // 选择的图片路径
    private String selectedImagePath;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法
     * @param user 当前用户
     */
    public RepairSubmitPanel(User user) {
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
        
        JLabel titleLabel = new JLabel("提交报修");
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
        
        // 故障描述
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel descriptionLabel = new JLabel("故障描述:");
        descriptionLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        descriptionLabel.setForeground(TEXT_COLOR);
        formPanel.add(descriptionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);
        
        // 图片上传
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel imageLabel = new JLabel("上传图片:");
        imageLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        imageLabel.setForeground(TEXT_COLOR);
        formPanel.add(imageLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        
        imagePathField = new JTextField();
        imagePathField.setEditable(false);
        imagePathField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        browseButton = new JButton("浏览...");
        browseButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        browseButton.setBackground(SECONDARY_COLOR);
        browseButton.setForeground(TEXT_COLOR);
        browseButton.setFocusPainted(false);
        browseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        browseButton.addActionListener(e -> browseImage());
        
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(browseButton, BorderLayout.EAST);
        
        formPanel.add(imagePanel, gbc);
        
        // 提示信息
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel tipLabel = new JLabel("* 请详细描述故障情况，以便管家及时处理");
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
        resetButton.addActionListener(e -> resetForm());
        
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
        submitButton.addActionListener(e -> submitRepair());
        
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(submitButton);
        
        // 添加组件到面板
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 浏览图片
     */
    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择图片");
        fileChooser.setFileFilter(new FileNameExtensionFilter("图片文件", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            imagePathField.setText(selectedFile.getName());
        }
    }
    
    /**
     * 重置表单
     */
    private void resetForm() {
        descriptionArea.setText("");
        imagePathField.setText("");
        selectedImagePath = null;
    }
    
    /**
     * 提交报修
     */
    private void submitRepair() {
        // 获取故障描述
        String description = descriptionArea.getText().trim();
        
        // 验证输入
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写故障描述", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // 创建报修对象
            Repair repair = new Repair();
            repair.setSubmitUserID(currentUser.getUserID());
            repair.setFaultDesc(description);
            // 图片路径暂时不保存到数据库
            // repair.setImagePath(selectedImagePath);
            
            // 设置必要的字段
            repair.setBuildingID(currentUser.getBuildingID() != null ? currentUser.getBuildingID() : 0);
            repair.setRoomNumber("未知"); // 默认值，实际应用中应从表单获取
            repair.setRepairType(4); // 默认为其他类型
            repair.setStatus(0); // 待处理状态
            
            // 创建报修控制器
            RepairController repairController = new RepairController();
            
            // 提交报修
            boolean result = repairController.submitRepair(repair);
            
            if (result) {
                JOptionPane.showMessageDialog(this, "报修提交成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "报修提交失败，请稍后重试", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "发生错误：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}