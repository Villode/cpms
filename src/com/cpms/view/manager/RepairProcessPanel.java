package com.cpms.view.manager;

import com.cpms.controller.admin.BuildingController;
import com.cpms.controller.common.RepairController;
import com.cpms.model.dao.UserDAO;
import com.cpms.model.entity.Building;
import com.cpms.model.entity.Repair;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 报修处理面板
 * 物业管家处理报修的界面
 */
public class RepairProcessPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private RepairController repairController;
    private BuildingController buildingController;
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTable repairTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilterComboBox;
    private JButton refreshButton;
    private JButton handleButton;
    private JTextArea handleOpinionArea;
    private JComboBox<String> statusComboBox;
    
    // 数据
    private List<Repair> repairList;
    private int selectedRepairID = -1;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法，初始化报修处理面板
     * @param user 当前用户
     */
    public RepairProcessPanel(User user) {
        this.currentUser = user;
        
        // 初始化控制器
        repairController = new RepairController();
        buildingController = new BuildingController();
        
        // 设置布局
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 创建界面组件
        initComponents();
        
        // 加载数据
        loadRepairs();
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建顶部过滤面板
        JPanel filterPanel = createFilterPanel();
        
        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        
        // 创建处理面板
        JPanel handlePanel = createHandlePanel();
        
        // 添加到主面板
        add(filterPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(handlePanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建过滤面板
     * @return 过滤面板
     */
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // 状态过滤
        JLabel statusLabel = new JLabel("报修状态:");
        statusLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        statusFilterComboBox = new JComboBox<>(new String[]{"全部", "待处理", "处理中", "已完成", "已取消"});
        statusFilterComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        statusFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRepairs();
            }
        });
        
        // 刷新按钮
        refreshButton = new JButton("刷新");
        refreshButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRepairs();
            }
        });
        
        panel.add(statusLabel);
        panel.add(statusFilterComboBox);
        panel.add(refreshButton);
        
        return panel;
    }
    
    /**
     * 创建表格面板
     * @return 表格面板
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                "报修列表",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                        config.getIntProperty("ui.font.size.large", 16)),
                PRIMARY_COLOR));
        
        // 创建表格模型
        String[] columnNames = {"ID", "楼栋", "房间号", "报修类型", "报修描述", "状态", "报修人", "报修时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };
        
        // 创建表格
        repairTable = new JTable(tableModel);
        repairTable.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        repairTable.setRowHeight(30);
        repairTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        repairTable.getTableHeader().setReorderingAllowed(false);
        repairTable.getTableHeader().setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 设置列宽
        repairTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        repairTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        repairTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        repairTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        repairTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        repairTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        repairTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        repairTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        
        // 居中显示
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < repairTable.getColumnCount(); i++) {
            repairTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // 添加表格选择事件
        repairTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = repairTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedRepairID = (int) tableModel.getValueAt(selectedRow, 0);
                    updateHandlePanel();
                }
            }
        });
        
        // 添加到滚动面板
        JScrollPane scrollPane = new JScrollPane(repairTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建处理面板
     * @return 处理面板
     */
    private JPanel createHandlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                "报修处理",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                        config.getIntProperty("ui.font.size.large", 16)),
                PRIMARY_COLOR));
        
        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 处理意见
        JLabel opinionLabel = new JLabel("处理意见:");
        opinionLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        handleOpinionArea = new JTextArea(3, 20);
        handleOpinionArea.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        handleOpinionArea.setLineWrap(true);
        handleOpinionArea.setWrapStyleWord(true);
        JScrollPane opinionScrollPane = new JScrollPane(handleOpinionArea);
        
        // 状态选择
        JLabel statusLabel = new JLabel("处理状态:");
        statusLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        statusComboBox = new JComboBox<>(new String[]{"待处理", "处理中", "已完成", "已取消"});
        statusComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        
        // 添加组件到表单面板
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(statusLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(statusComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(opinionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(opinionScrollPane, gbc);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        handleButton = createStyledButton("处理报修", SUCCESS_COLOR);
        handleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRepair();
            }
        });
        
        buttonPanel.add(handleButton);
        
        // 添加到处理面板
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
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
     * 加载报修数据
     */
    private void loadRepairs() {
        // 清空表格
        tableModel.setRowCount(0);
        
        try {
            // 根据过滤条件获取报修列表
            int statusFilter = statusFilterComboBox.getSelectedIndex() - 1;
            if (statusFilter == -1) {
                // 获取管家负责的楼栋ID
                Integer managedBuildingID = currentUser.getManagedBuildingID();
                if (managedBuildingID != null && managedBuildingID > 0) {
                    repairList = repairController.getRepairsByBuildingID(managedBuildingID);
                } else {
                    // 如果没有指定管理楼栋，获取所有报修
                    repairList = repairController.getAllRepairs();
                }
            } else {
                repairList = repairController.getRepairsByStatus(statusFilter);
                
                // 过滤出当前管家负责的楼栋的报修
                Integer managedBuildingID = currentUser.getManagedBuildingID();
                if (managedBuildingID != null && managedBuildingID > 0) {
                    repairList.removeIf(repair -> repair.getBuildingID() != managedBuildingID);
                }
            }
            
            // 填充表格数据
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (Repair repair : repairList) {
                // 获取楼栋名称
                String buildingName = getBuildingName(repair.getBuildingID());
                
                // 获取报修类型名称
                String repairTypeName = getRepairTypeName(repair.getRepairType());
                
                // 获取状态名称
                String statusName = getStatusName(repair.getRepairStatus());
                
                // 获取报修人姓名
                String userName = getUserName(repair.getUserID());
                
                // 添加到表格
                tableModel.addRow(new Object[]{
                    repair.getRepairID(),
                    buildingName,
                    repair.getRoomNumber(),
                    repairTypeName,
                    repair.getRepairDesc(),
                    statusName,
                    userName,
                    dateFormat.format(repair.getCreateTime())
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载报修数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 更新处理面板
     */
    private void updateHandlePanel() {
        if (selectedRepairID <= 0) {
            return;
        }
        
        try {
            // 查找选中的报修
            Repair selectedRepair = null;
            for (Repair repair : repairList) {
                if (repair.getRepairID() == selectedRepairID) {
                    selectedRepair = repair;
                    break;
                }
            }
            
            if (selectedRepair != null) {
                // 设置状态
                statusComboBox.setSelectedIndex(selectedRepair.getRepairStatus());
                
                // 设置处理意见
                handleOpinionArea.setText(selectedRepair.getHandleOpinion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 处理报修
     */
    private void handleRepair() {
        if (selectedRepairID <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择要处理的报修！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 获取处理意见和状态
        String handleOpinion = handleOpinionArea.getText().trim();
        int status = statusComboBox.getSelectedIndex();
        
        if (handleOpinion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入处理意见！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 处理报修
        boolean success = repairController.handleRepair(selectedRepairID, currentUser.getUserID(), status, handleOpinion);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "报修处理成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadRepairs(); // 重新加载数据
        } else {
            JOptionPane.showMessageDialog(this, "报修处理失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 获取楼栋名称
     * @param buildingID 楼栋ID
     * @return 楼栋名称
     */
    private String getBuildingName(int buildingID) {
        try {
            Building building = buildingController.getBuildingByID(buildingID);
            return building != null ? building.getBuildingName() : "未知楼栋";
        } catch (Exception e) {
            return "未知楼栋";
        }
    }
    
    /**
     * 获取报修类型名称
     * @param repairType 报修类型
     * @return 报修类型名称
     */
    private String getRepairTypeName(int repairType) {
        switch (repairType) {
            case 1:
                return "水电维修";
            case 2:
                return "家具维修";
            case 3:
                return "门窗维修";
            case 4:
                return "其他";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 获取状态名称
     * @param status 状态
     * @return 状态名称
     */
    private String getStatusName(int status) {
        switch (status) {
            case 0:
                return "待处理";
            case 1:
                return "处理中";
            case 2:
                return "已完成";
            case 3:
                return "已取消";
            default:
                return "未知状态";
        }
    }
    
    /**
     * 获取用户姓名
     * @param userID 用户ID
     * @return 用户姓名
     */
    private String getUserName(int userID) {
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.findByID(userID);
            userDAO.close();
            return user != null ? user.getRealName() : "未知用户";
        } catch (SQLException e) {
            return "未知用户";
        }
    }
} 