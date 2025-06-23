package com.cpms.view.owner;

import com.cpms.controller.owner.RepairController;
import com.cpms.model.entity.Repair;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 业主报修记录查询面板
 */
public class RepairHistoryPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTable repairTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton detailButton;
    private JButton cancelButton;
    
    // 报修记录列表
    private List<Repair> repairList;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    private final Color WARNING_COLOR = Color.decode(config.getProperty("ui.color.warning", "#FF9800"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    
    /**
     * 构造方法
     * @param user 当前用户
     */
    public RepairHistoryPanel(User user) {
        this.currentUser = user;
        
        // 设置面板属性
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // 初始化组件
        initComponents();
        
        // 加载数据
        loadData();
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        // 创建标题面板
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("我的报修记录");
        titleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.large", 18)));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        refreshButton = new JButton("刷新");
        refreshButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        refreshButton.setBackground(PRIMARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(refreshButton, BorderLayout.EAST);
        
        // 创建表格面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        
        // 创建表格模型
        String[] columnNames = {"报修ID", "报修类型", "故障描述", "提交时间", "状态", "处理人", "处理意见"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        // 创建表格
        repairTable = new JTable(tableModel);
        repairTable.setRowHeight(30);
        repairTable.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        repairTable.getTableHeader().setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        repairTable.getTableHeader().setReorderingAllowed(false);
        repairTable.getTableHeader().setResizingAllowed(true);
        repairTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 设置表格列宽
        TableColumnModel columnModel = repairTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // 报修ID
        columnModel.getColumn(1).setPreferredWidth(80);  // 报修类型
        columnModel.getColumn(2).setPreferredWidth(200); // 故障描述
        columnModel.getColumn(3).setPreferredWidth(150); // 提交时间
        columnModel.getColumn(4).setPreferredWidth(80);  // 状态
        columnModel.getColumn(5).setPreferredWidth(80);  // 处理人
        columnModel.getColumn(6).setPreferredWidth(200); // 处理意见
        
        // 设置表格单元格渲染器
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(repairTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        detailButton = new JButton("查看详情");
        detailButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        detailButton.setBackground(PRIMARY_COLOR);
        detailButton.setForeground(Color.WHITE);
        detailButton.setOpaque(true);
        detailButton.setBorderPainted(false);
        detailButton.setFocusPainted(false);
        detailButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        detailButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRepairDetail();
            }
        });
        
        cancelButton = new JButton("取消报修");
        cancelButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        cancelButton.setBackground(ERROR_COLOR);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelRepair();
            }
        });
        
        buttonPanel.add(detailButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        
        // 添加组件到面板
        add(titlePanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 加载数据
     */
    private void loadData() {
        try {
            // 清空表格数据
            tableModel.setRowCount(0);
            
            // 创建报修控制器
            RepairController repairController = new RepairController();
            
            // 获取当前用户的报修记录
            repairList = repairController.getRepairsByUserID(currentUser.getUserID());
            
            // 日期格式化
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            // 添加数据到表格
            for (Repair repair : repairList) {
                Object[] rowData = new Object[7];
                rowData[0] = repair.getRepairID();
                rowData[1] = getRepairTypeText(repair.getRepairType());
                rowData[2] = repair.getFaultDesc();
                rowData[3] = repair.getCreateTime() != null ? dateFormat.format(repair.getCreateTime()) : "";
                rowData[4] = getStatusText(repair.getStatus());
                rowData[5] = repair.getHandlerName() != null ? repair.getHandlerName() : "";
                rowData[6] = repair.getHandleOpinion() != null ? repair.getHandleOpinion() : "";
                
                tableModel.addRow(rowData);
            }
            
            // 如果没有数据，显示提示
            if (repairList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "您还没有提交过报修记录", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载报修记录失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 获取报修类型文本
     * @param repairType 报修类型
     * @return 报修类型文本
     */
    private String getRepairTypeText(int repairType) {
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
                return "未知";
        }
    }
    
    /**
     * 获取状态文本
     * @param status 状态
     * @return 状态文本
     */
    private String getStatusText(int status) {
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
                return "未知";
        }
    }
    
    /**
     * 显示报修详情
     */
    private void showRepairDetail() {
        int selectedRow = repairTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一条报修记录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 获取选中的报修记录
        Repair repair = repairList.get(selectedRow);
        
        // 创建详情对话框
        JDialog detailDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "报修详情", true);
        detailDialog.setSize(500, 400);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setLayout(new BorderLayout());
        
        // 创建详情面板
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 报修ID
        addDetailField(detailPanel, gbc, 0, "报修ID:", String.valueOf(repair.getRepairID()));
        
        // 报修类型
        addDetailField(detailPanel, gbc, 1, "报修类型:", getRepairTypeText(repair.getRepairType()));
        
        // 故障描述
        addDetailField(detailPanel, gbc, 2, "故障描述:", repair.getFaultDesc());
        
        // 提交时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String submitTime = repair.getCreateTime() != null ? dateFormat.format(repair.getCreateTime()) : "未知";
        addDetailField(detailPanel, gbc, 3, "提交时间:", submitTime);
        
        // 状态
        addDetailField(detailPanel, gbc, 4, "状态:", getStatusText(repair.getStatus()));
        
        // 处理人
        String handlerName = repair.getHandlerName() != null ? repair.getHandlerName() : "未分配";
        addDetailField(detailPanel, gbc, 5, "处理人:", handlerName);
        
        // 处理意见
        String handleOpinion = repair.getHandleOpinion() != null ? repair.getHandleOpinion() : "无";
        addDetailField(detailPanel, gbc, 6, "处理意见:", handleOpinion);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton closeButton = new JButton("关闭");
        closeButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        closeButton.setBackground(PRIMARY_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setOpaque(true);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailDialog.dispose();
            }
        });
        
        buttonPanel.add(closeButton);
        
        // 添加组件到对话框
        detailDialog.add(detailPanel, BorderLayout.CENTER);
        detailDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // 显示对话框
        detailDialog.setVisible(true);
    }
    
    /**
     * 添加详情字段
     * @param panel 面板
     * @param gbc 网格约束
     * @param row 行号
     * @param labelText 标签文本
     * @param valueText 值文本
     */
    private void addDetailField(JPanel panel, GridBagConstraints gbc, int row, String labelText, String valueText) {
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
    
    /**
     * 取消报修
     */
    private void cancelRepair() {
        int selectedRow = repairTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一条报修记录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 获取选中的报修记录
        Repair repair = repairList.get(selectedRow);
        
        // 检查状态是否为待处理
        if (repair.getStatus() != 0) {
            JOptionPane.showMessageDialog(this, "只有待处理的报修才能取消", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 确认取消
        int option = JOptionPane.showConfirmDialog(this, "确定要取消这条报修记录吗？", "确认", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                // 创建报修控制器
                RepairController repairController = new RepairController();
                
                // 取消报修
                boolean result = repairController.cancelRepair(repair.getRepairID(), currentUser.getUserID());
                
                if (result) {
                    JOptionPane.showMessageDialog(this, "报修已成功取消", "成功", JOptionPane.INFORMATION_MESSAGE);
                    loadData(); // 重新加载数据
                } else {
                    JOptionPane.showMessageDialog(this, "取消报修失败，请稍后重试", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "取消报修失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}