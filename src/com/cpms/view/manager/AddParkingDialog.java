package com.cpms.view.manager;

import com.cpms.model.dao.BuildingDAO;
import com.cpms.model.dao.ParkingSpotDAO;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.ParkingSpot;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * 新增车位对话框
 * 用于录入新的车位信息
 */
public class AddParkingDialog extends JDialog {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 界面组件
    private JComboBox<Building> buildingComboBox;
    private JTextField spotNumberField;
    private JTextField locationField;
    private JComboBox<String> statusComboBox;
    private JButton confirmButton;
    private JButton cancelButton;
    
    // 数据
    private List<Building> buildingList;
    private boolean confirmed = false;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    
    /**
     * 构造方法
     * @param parent 父窗口
     */
    public AddParkingDialog(Window parent) {
        super(parent, "新增车位", ModalityType.APPLICATION_MODAL);
        
        // 初始化界面
        initComponents();
        
        // 加载数据
        loadBuildingData();
        
        // 设置对话框属性
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(true);
        setMinimumSize(new Dimension(400, 350));
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
        JLabel titleLabel = new JLabel("新增车位信息");
        titleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        mainPanel.add(titleLabel, gbc);
        
        // 重置网格设置
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // 楼栋选择
        JLabel buildingLabel = new JLabel("所属楼栋:");
        buildingLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(buildingLabel, gbc);
        
        buildingComboBox = new JComboBox<>();
        buildingComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        buildingComboBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buildingComboBox, gbc);
        
        // 车位编号
        JLabel spotNumberLabel = new JLabel("车位编号:");
        spotNumberLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(spotNumberLabel, gbc);
        
        spotNumberField = new JTextField();
        spotNumberField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        spotNumberField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spotNumberField, gbc);
        
        // 位置描述
        JLabel locationLabel = new JLabel("位置描述:");
        locationLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(locationLabel, gbc);
        
        locationField = new JTextField();
        locationField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        locationField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(locationField, gbc);
        
        // 使用状态
        JLabel statusLabel = new JLabel("使用状态:");
        statusLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(statusLabel, gbc);
        
        statusComboBox = new JComboBox<>(new String[]{"空闲", "已占用"});
        statusComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        statusComboBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 4;
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
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        // 确认按钮
        confirmButton = new JButton("确认新增");
        confirmButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        confirmButton.setBackground(PRIMARY_COLOR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setPreferredSize(new Dimension(100, 35));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmAdd();
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
        panel.add(Box.createHorizontalStrut(20));
        panel.add(cancelButton);
        
        return panel;
    }
    
    /**
     * 加载楼栋数据
     */
    private void loadBuildingData() {
        try {
            BuildingDAO buildingDAO = new BuildingDAO();
            buildingList = buildingDAO.findAll();
            
            buildingComboBox.removeAllItems();
            
            if (buildingList != null) {
                for (Building building : buildingList) {
                    buildingComboBox.addItem(building);
                }
            }
            
            buildingDAO.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载楼栋数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 确认新增车位
     */
    private void confirmAdd() {
        // 验证输入
        if (!validateInput()) {
            return;
        }
        
        try {
            // 获取输入数据
            Building selectedBuilding = (Building) buildingComboBox.getSelectedItem();
            String spotNumber = spotNumberField.getText().trim();
            String location = locationField.getText().trim();
            int status = statusComboBox.getSelectedIndex(); // 0-空闲, 1-已占用
            
            // 创建车位对象
            ParkingSpot parkingSpot = new ParkingSpot();
            parkingSpot.setBuildingID(selectedBuilding.getBuildingID());
            parkingSpot.setSpotNumber(spotNumber);
            parkingSpot.setLocation(location);
            parkingSpot.setUsageStatus(status);
            
            // 保存到数据库
            ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
            boolean success = parkingSpotDAO.addParkingSpot(parkingSpot);
            
            if (success) {
                confirmed = true;
                JOptionPane.showMessageDialog(this, "车位新增成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "车位新增失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
            
            parkingSpotDAO.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "新增车位时发生错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 验证输入数据
     */
    private boolean validateInput() {
        // 检查楼栋选择
        if (buildingComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "请选择所属楼栋", "输入错误", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // 检查车位编号
        String spotNumber = spotNumberField.getText().trim();
        if (spotNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入车位编号", "输入错误", JOptionPane.WARNING_MESSAGE);
            spotNumberField.requestFocus();
            return false;
        }
        
        // 检查位置描述
        String location = locationField.getText().trim();
        if (location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入位置描述", "输入错误", JOptionPane.WARNING_MESSAGE);
            locationField.requestFocus();
            return false;
        }
        
        // 检查车位编号是否已存在
        try {
            Building selectedBuilding = (Building) buildingComboBox.getSelectedItem();
            ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
            
            List<ParkingSpot> existingSpots = parkingSpotDAO.findByBuildingID(selectedBuilding.getBuildingID());
            if (existingSpots != null) {
                for (ParkingSpot spot : existingSpots) {
                    if (spotNumber.equals(spot.getSpotNumber())) {
                        JOptionPane.showMessageDialog(this, "该楼栋下已存在相同编号的车位", "输入错误", JOptionPane.WARNING_MESSAGE);
                        spotNumberField.requestFocus();
                        parkingSpotDAO.close();
                        return false;
                    }
                }
            }
            
            parkingSpotDAO.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "验证车位编号时发生错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * 获取确认状态
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}