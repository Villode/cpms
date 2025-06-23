package com.cpms.view.owner;

import com.cpms.controller.owner.PaymentController;
import com.cpms.model.entity.PaymentRecord;
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
 * 业主已缴费记录查询面板
 */
public class PaymentHistoryPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton detailButton;
    
    // 缴费记录列表
    private List<PaymentRecord> paymentList;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    
    /**
     * 构造方法
     * @param user 当前用户
     */
    public PaymentHistoryPanel(User user) {
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
        
        JLabel titleLabel = new JLabel("缴费记录");
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
        String[] columnNames = {"缴费ID", "缴费类型", "金额", "缴费时间", "支付方式", "房间号"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        // 创建表格
        historyTable = new JTable(tableModel);
        historyTable.setRowHeight(30);
        historyTable.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        historyTable.getTableHeader().setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        historyTable.getTableHeader().setReorderingAllowed(false);
        historyTable.getTableHeader().setResizingAllowed(true);
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 设置表格列宽
        TableColumnModel columnModel = historyTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // 缴费ID
        columnModel.getColumn(1).setPreferredWidth(80);  // 缴费类型
        columnModel.getColumn(2).setPreferredWidth(80);  // 金额
        columnModel.getColumn(3).setPreferredWidth(150); // 缴费时间
        columnModel.getColumn(4).setPreferredWidth(80);  // 支付方式
        columnModel.getColumn(5).setPreferredWidth(80);  // 房间号
        
        // 设置表格单元格渲染器
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(historyTable);
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
                showPaymentDetail();
            }
        });
        
        buttonPanel.add(detailButton);
        
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
            
            // 创建缴费控制器
            PaymentController paymentController = new PaymentController();
            
            // 获取当前用户的已缴费记录
            paymentList = paymentController.getPaidRecords(currentUser.getUserID());
            
            // 日期格式化
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            // 添加数据到表格
            for (PaymentRecord payment : paymentList) {
                Object[] rowData = new Object[6];
                rowData[0] = payment.getPaymentID();
                rowData[1] = payment.getPaymentTypeText();
                rowData[2] = String.format("%.2f", payment.getAmount());
                rowData[3] = payment.getPaymentTime() != null ? dateFormat.format(payment.getPaymentTime()) : "无";
                rowData[4] = payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "未知";
                rowData[5] = payment.getRoomNumber();
                
                tableModel.addRow(rowData);
            }
            
            // 如果没有数据，显示提示
            if (paymentList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "暂无缴费记录", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载缴费记录失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 显示缴费详情
     */
    private void showPaymentDetail() {
        int selectedRow = historyTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一条记录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 获取选中的缴费记录
        PaymentRecord payment = paymentList.get(selectedRow);
        
        // 创建详情对话框
        JDialog detailDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "缴费详情", true);
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
        
        // 缴费ID
        addDetailField(detailPanel, gbc, 0, "缴费ID:", String.valueOf(payment.getPaymentID()));
        
        // 缴费类型
        addDetailField(detailPanel, gbc, 1, "缴费类型:", payment.getPaymentTypeText());
        
        // 金额
        addDetailField(detailPanel, gbc, 2, "金额:", String.format("%.2f 元", payment.getAmount()));
        
        // 楼栋
        String buildingName = payment.getBuildingName() != null ? payment.getBuildingName() : "未知";
        addDetailField(detailPanel, gbc, 3, "楼栋:", buildingName);
        
        // 房间号
        addDetailField(detailPanel, gbc, 4, "房间号:", payment.getRoomNumber());
        
        // 缴费时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String paymentTime = payment.getPaymentTime() != null ? dateFormat.format(payment.getPaymentTime()) : "无";
        addDetailField(detailPanel, gbc, 5, "缴费时间:", paymentTime);
        
        // 支付方式
        String paymentMethod = payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "未知";
        addDetailField(detailPanel, gbc, 6, "支付方式:", paymentMethod);
        
        // 交易号
        String transactionNo = payment.getTransactionNo() != null ? payment.getTransactionNo() : "无";
        addDetailField(detailPanel, gbc, 7, "交易号:", transactionNo);
        
        // 备注
        String remark = payment.getRemark() != null ? payment.getRemark() : "无";
        addDetailField(detailPanel, gbc, 8, "备注:", remark);
        
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
        
        JButton printButton = new JButton("打印凭证");
        printButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        printButton.setBackground(SUCCESS_COLOR);
        printButton.setForeground(Color.WHITE);
        printButton.setOpaque(true);
        printButton.setBorderPainted(false);
        printButton.setFocusPainted(false);
        printButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(detailDialog, "打印功能暂未实现", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        buttonPanel.add(printButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
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
}