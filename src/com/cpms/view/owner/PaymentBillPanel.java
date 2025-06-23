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
 * 业主账单查询面板
 */
public class PaymentBillPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTable billTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton payButton;
    private JButton detailButton;
    
    // 账单列表
    private List<PaymentRecord> billList;
    
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
    public PaymentBillPanel(User user) {
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
        
        JLabel titleLabel = new JLabel("未缴费账单");
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
        String[] columnNames = {"账单ID", "账单类型", "金额", "截止日期", "房间号", "备注"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        // 创建表格
        billTable = new JTable(tableModel);
        billTable.setRowHeight(30);
        billTable.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        billTable.getTableHeader().setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        billTable.getTableHeader().setReorderingAllowed(false);
        billTable.getTableHeader().setResizingAllowed(true);
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 设置表格列宽
        TableColumnModel columnModel = billTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // 账单ID
        columnModel.getColumn(1).setPreferredWidth(80);  // 账单类型
        columnModel.getColumn(2).setPreferredWidth(80);  // 金额
        columnModel.getColumn(3).setPreferredWidth(150); // 截止日期
        columnModel.getColumn(4).setPreferredWidth(80);  // 房间号
        columnModel.getColumn(5).setPreferredWidth(200); // 备注
        
        // 设置表格单元格渲染器
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(billTable);
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
                showBillDetail();
            }
        });
        
        payButton = new JButton("缴费");
        payButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        payButton.setBackground(SUCCESS_COLOR);
        payButton.setForeground(Color.WHITE);
        payButton.setOpaque(true);
        payButton.setBorderPainted(false);
        payButton.setFocusPainted(false);
        payButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        payButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payBill();
            }
        });
        
        buttonPanel.add(detailButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(payButton);
        
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
            
            // 获取当前用户的未缴费账单
            billList = paymentController.getUnpaidBills(currentUser.getUserID());
            
            // 日期格式化
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            // 添加数据到表格
            for (PaymentRecord bill : billList) {
                Object[] rowData = new Object[6];
                rowData[0] = bill.getPaymentID();
                rowData[1] = bill.getPaymentTypeText();
                rowData[2] = String.format("%.2f", bill.getAmount());
                rowData[3] = bill.getDueDate() != null ? dateFormat.format(bill.getDueDate()) : "无";
                rowData[4] = bill.getRoomNumber();
                rowData[5] = bill.getRemark() != null ? bill.getRemark() : "";
                
                tableModel.addRow(rowData);
            }
            
            // 如果没有数据，显示提示
            if (billList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "您当前没有未缴费账单", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载账单失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 显示账单详情
     */
    private void showBillDetail() {
        int selectedRow = billTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一条账单", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 获取选中的账单
        PaymentRecord bill = billList.get(selectedRow);
        
        // 创建详情对话框
        JDialog detailDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "账单详情", true);
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
        
        // 账单ID
        addDetailField(detailPanel, gbc, 0, "账单ID:", String.valueOf(bill.getPaymentID()));
        
        // 账单类型
        addDetailField(detailPanel, gbc, 1, "账单类型:", bill.getPaymentTypeText());
        
        // 金额
        addDetailField(detailPanel, gbc, 2, "金额:", String.format("%.2f 元", bill.getAmount()));
        
        // 截止日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dueDate = bill.getDueDate() != null ? dateFormat.format(bill.getDueDate()) : "无";
        addDetailField(detailPanel, gbc, 3, "截止日期:", dueDate);
        
        // 楼栋
        String buildingName = bill.getBuildingName() != null ? bill.getBuildingName() : "未知";
        addDetailField(detailPanel, gbc, 4, "楼栋:", buildingName);
        
        // 房间号
        addDetailField(detailPanel, gbc, 5, "房间号:", bill.getRoomNumber());
        
        // 状态
        addDetailField(detailPanel, gbc, 6, "状态:", bill.getPaymentStatusText());
        
        // 备注
        String remark = bill.getRemark() != null ? bill.getRemark() : "无";
        addDetailField(detailPanel, gbc, 7, "备注:", remark);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton payNowButton = new JButton("立即缴费");
        payNowButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        payNowButton.setBackground(SUCCESS_COLOR);
        payNowButton.setForeground(Color.WHITE);
        payNowButton.setOpaque(true);
        payNowButton.setBorderPainted(false);
        payNowButton.setFocusPainted(false);
        payNowButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        payNowButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        payNowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailDialog.dispose();
                payBill();
            }
        });
        
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
        
        buttonPanel.add(payNowButton);
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
    
    /**
     * 缴费
     */
    private void payBill() {
        int selectedRow = billTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一条账单", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 获取选中的账单
        PaymentRecord bill = billList.get(selectedRow);
        
        // 创建支付对话框
        JDialog payDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "缴费", true);
        payDialog.setSize(400, 300);
        payDialog.setLocationRelativeTo(this);
        payDialog.setLayout(new BorderLayout());
        
        // 创建支付面板
        JPanel payPanel = new JPanel(new GridBagLayout());
        payPanel.setBackground(Color.WHITE);
        payPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 账单信息
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("账单信息: " + bill.getPaymentTypeText() + " - " + String.format("%.2f 元", bill.getAmount()));
        infoLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        infoLabel.setForeground(TEXT_COLOR);
        payPanel.add(infoLabel, gbc);
        
        // 支付方式
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel methodLabel = new JLabel("支付方式:");
        methodLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        methodLabel.setForeground(TEXT_COLOR);
        payPanel.add(methodLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JComboBox<String> methodComboBox = new JComboBox<>(new String[]{"支付宝", "微信", "银行卡"});
        methodComboBox.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        payPanel.add(methodComboBox, gbc);
        
        // 交易号
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel transactionLabel = new JLabel("交易号:");
        transactionLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.normal", 14)));
        transactionLabel.setForeground(TEXT_COLOR);
        payPanel.add(transactionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField transactionField = new JTextField();
        transactionField.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        payPanel.add(transactionField, gbc);
        
        // 提示信息
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel tipLabel = new JLabel("* 请输入支付平台生成的交易号");
        tipLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.ITALIC, 
                config.getIntProperty("ui.font.size.small", 12)));
        tipLabel.setForeground(TEXT_COLOR);
        payPanel.add(tipLabel, gbc);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton cancelButton = new JButton("取消");
        cancelButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payDialog.dispose();
            }
        });
        
        JButton confirmButton = new JButton("确认支付");
        confirmButton.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        confirmButton.setBackground(SUCCESS_COLOR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setOpaque(true);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取支付方式和交易号
                String paymentMethod = (String) methodComboBox.getSelectedItem();
                String transactionNo = transactionField.getText().trim();
                
                // 验证输入
                if (transactionNo.isEmpty()) {
                    JOptionPane.showMessageDialog(payDialog, "请输入交易号", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 处理支付
                try {
                    // 创建缴费控制器
                    PaymentController paymentController = new PaymentController();
                    
                    // 支付账单
                    boolean result = paymentController.payBill(bill.getPaymentID(), paymentMethod, transactionNo);
                    
                    if (result) {
                        JOptionPane.showMessageDialog(payDialog, "支付成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        payDialog.dispose();
                        loadData(); // 重新加载数据
                    } else {
                        JOptionPane.showMessageDialog(payDialog, "支付失败，请稍后重试", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(payDialog, "支付失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(confirmButton);
        
        // 添加组件到对话框
        payDialog.add(payPanel, BorderLayout.CENTER);
        payDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // 显示对话框
        payDialog.setVisible(true);
    }
}