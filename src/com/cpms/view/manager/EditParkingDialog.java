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
 * 编辑车位对话框
 * 用于修改车位信息
 */
public class EditParkingDialog extends JDialog {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 界面组件
    private JComboBox<Building> buildingComboBox;
    private JTextField spotNumberField;
    private JTextField locationField;
    private JTextField licensePlateField;
    private JComboBox<String> statusComboBox;
    private JButton confirmButton;
    private JButton cancelButton;
    
    // 数据
    private ParkingSpot parkingSpot;
    private List<Building> buildingList;
    private boolean confirmed = false;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    
    /**
     * 构造方法
     * @param parent 父窗口
     * @param parkingSpot 要编辑的车位
     */
    public EditParkingDialog(Window parent, ParkingSpot parkingSpot) {
        super(parent, "编辑车位", ModalityType.APPLICATION_MODAL);
        this.parkingSpot = parkingSpot;
        
        // 初始化界面
        initComponents();
        
        // 加载数据
        loadBuildingData();
        
        // 填充现有数据
        fillData();
        
        // 设置对话框属性
        setSize(400, 400);
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
        JLabel titleLabel = new JLabel("编辑车位信息");
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
        
        // 车位ID（只读）
        JLabel idLabel = new JLabel("车位ID:");
        idLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(idLabel, gbc);
        
        JLabel idValueLabel = new JLabel(String.valueOf(parkingSpot.getParkingID()));
        idValueLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        idValueLabel.setForeground(Color.GRAY);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(idValueLabel, gbc);
        
        // 楼栋选择
        JLabel buildingLabel = new JLabel("所属楼栋:");
        buildingLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(buildingLabel, gbc);
        
        buildingComboBox = new JComboBox<>();
        buildingComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        buildingComboBox.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buildingComboBox, gbc);
        
        // 车位编号
        JLabel spotNumberLabel = new JLabel("车位编号:");
        spotNumberLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(spotNumberLabel, gbc);
        
        spotNumberField = new JTextField();
        spotNumberField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        spotNumberField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spotNumberField, gbc);
        
        // 位置描述
        JLabel locationLabel = new JLabel("位置描述:");
        locationLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(locationLabel, gbc);
        
        locationField = new JTextField();
        locationField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        locationField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(locationField, gbc);
        
        // 车牌号
        JLabel licensePlateLabel = new JLabel("车牌号:");
        licensePlateLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(licensePlateLabel, gbc);
        
        licensePlateField = new JTextField();
        licensePlateField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        licensePlateField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(licensePlateField, gbc);
        
        // 使用状态
        JLabel statusLabel = new JLabel("使用状态:");
        statusLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(statusLabel, gbc);
        
        statusComboBox = new JComboBox<>(new String[]{"空闲", "已占用"});
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
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        // 确认按钮
        confirmButton = new JButton("确认修改");
        confirmButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 14));
        confirmButton.setBackground(PRIMARY_COLOR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setPreferredSize(new Dimension(100, 35));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmEdit();
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
     * 填充现有数据
     */
    private void fillData() {
        // 设置楼栋
        if (buildingList != null) {
            for (int i = 0; i < buildingList.size(); i++) {
                if (buildingList.get(i).getBuildingID() == parkingSpot.getBuildingID()) {
                    buildingComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        // 设置车位编号
        spotNumberField.setText(parkingSpot.getSpotNumber());
        
        // 设置位置描述
        locationField.setText(parkingSpot.getLocation());
        
        // 设置车牌号
        if (parkingSpot.getLicensePlate() != null) {
            licensePlateField.setText(parkingSpot.getLicensePlate());
        }
        
        // 设置使用状态
        statusComboBox.setSelectedIndex(parkingSpot.getUsageStatus());
    }
    
    /**
     * 确认编辑车位
     */
    private void confirmEdit() {
        // 验证输入
        if (!validateInput()) {
            return;
        }
        
        try {
            // 获取输入数据
            Building selectedBuilding = (Building) buildingComboBox.getSelectedItem();
            String spotNumber = spotNumberField.getText().trim();
            String location = locationField.getText().trim();
            String licensePlate = licensePlateField.getText().trim();
            int status = statusComboBox.getSelectedIndex(); // 0-空闲, 1-已占用
            
            // 更新车位对象
            parkingSpot.setBuildingID(selectedBuilding.getBuildingID());
            parkingSpot.setSpotNumber(spotNumber);
            parkingSpot.setLocation(location);
            parkingSpot.setLicensePlate(licensePlate.isEmpty() ? null : licensePlate);
            parkingSpot.setUsageStatus(status);
            
            // 保存到数据库
            ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
            boolean success = parkingSpotDAO.updateParkingSpot(parkingSpot);
            
            if (success) {
                confirmed = true;
                JOptionPane.showMessageDialog(this, "车位信息修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "车位信息修改失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
            
            parkingSpotDAO.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "修改车位信息时发生错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
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
        
        // 检查车位编号是否已存在（排除当前车位）
        try {
            Building selectedBuilding = (Building) buildingComboBox.getSelectedItem();
            ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
            
            List<ParkingSpot> existingSpots = parkingSpotDAO.findByBuildingID(selectedBuilding.getBuildingID());
            if (existingSpots != null) {
                for (ParkingSpot spot : existingSpots) {
                    if (spotNumber.equals(spot.getSpotNumber()) && spot.getParkingID() != parkingSpot.getParkingID()) {
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
        
        // 验证车牌号格式（如果不为空）
        String licensePlate = licensePlateField.getText().trim();
        if (!licensePlate.isEmpty() && !isValidLicensePlate(licensePlate)) {
            JOptionPane.showMessageDialog(this, "车牌号格式不正确", "输入错误", JOptionPane.WARNING_MESSAGE);
            licensePlateField.requestFocus();
            return false;
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
}