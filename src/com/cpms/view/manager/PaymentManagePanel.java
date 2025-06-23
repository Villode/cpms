package com.cpms.view.manager;

import com.cpms.controller.steward.PaymentManageController;
import com.cpms.model.entity.PaymentRecord;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 物业管家物业费管理面板
 * 用于管理物业费账单的生成、查询和管理
 */
public class PaymentManagePanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private PaymentManageController paymentController;
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilterComboBox;
    private JComboBox<String> typeFilterComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JButton refreshButton;
    private JButton createBillButton;
    private JButton batchCreateButton;
    private JButton confirmPaymentButton;
    private JButton deleteButton;
    private JLabel totalCountLabel;
    private JLabel unpaidCountLabel;
    private JLabel paidCountLabel;
    private JLabel totalAmountLabel;
    
    // 缴费记录列表
    private List<PaymentRecord> paymentList;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    private final Color WARNING_COLOR = Color.decode(config.getProperty("ui.color.warning", "#FFC107"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    
    /**
     * 构造方法
     * @param user 当前用户
     */
    public PaymentManagePanel(User user) {
        this.currentUser = user;
        this.paymentController = new PaymentManageController();
        
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
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建标题面板
        JPanel titlePanel = createTitlePanel();
        
        // 创建工具栏面板
        JPanel toolbarPanel = createToolbarPanel();
        
        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        
        // 创建统计面板
        JPanel statsPanel = createStatsPanel();
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 组装界面
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(toolbarPanel, BorderLayout.CENTER);
        topPanel.add(statsPanel, BorderLayout.SOUTH);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建标题面板
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("物业费管理");
        titleLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                Integer.parseInt(config.getProperty("ui.font.size.title", "24"))));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        panel.add(titleLabel);
        return panel;
    }
    
    /**
     * 创建工具栏面板
     */
    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // 状态筛选
        panel.add(new JLabel("缴费状态:"));
        statusFilterComboBox = new JComboBox<>(new String[]{"全部", "未缴费", "已缴费"});
        statusFilterComboBox.addActionListener(e -> filterData());
        panel.add(statusFilterComboBox);
        
        panel.add(Box.createHorizontalStrut(20));
        
        // 类型筛选
        panel.add(new JLabel("缴费类型:"));
        typeFilterComboBox = new JComboBox<>(new String[]{"全部", "物业费", "水费", "电费", "其他"});
        typeFilterComboBox.addActionListener(e -> filterData());
        panel.add(typeFilterComboBox);
        
        panel.add(Box.createHorizontalStrut(20));
        
        // 搜索框
        panel.add(new JLabel("搜索:"));
        searchField = new JTextField(15);
        searchField.addActionListener(e -> searchData());
        panel.add(searchField);
        
        // 搜索按钮
        searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> searchData());
        panel.add(searchButton);
        
        panel.add(Box.createHorizontalStrut(20));
        
        // 刷新按钮
        refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadData());
        panel.add(refreshButton);
        
        return panel;
    }
    
    /**
     * 创建表格面板
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // 创建表格模型
        String[] columnNames = {"缴费ID", "业主姓名", "楼栋", "房间号", "缴费类型", "金额(元)", 
                               "缴费状态", "截止日期", "缴费时间", "缴费方式", "交易号", "备注"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // 创建表格
        paymentTable = new JTable(tableModel);
        paymentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paymentTable.setRowHeight(30);
        paymentTable.getTableHeader().setReorderingAllowed(false);
        
        // 设置列宽
        paymentTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // 缴费ID
        paymentTable.getColumnModel().getColumn(1).setPreferredWidth(100); // 业主姓名
        paymentTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // 楼栋
        paymentTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // 房间号
        paymentTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // 缴费类型
        paymentTable.getColumnModel().getColumn(5).setPreferredWidth(100); // 金额
        paymentTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // 缴费状态
        paymentTable.getColumnModel().getColumn(7).setPreferredWidth(120); // 截止日期
        paymentTable.getColumnModel().getColumn(8).setPreferredWidth(120); // 缴费时间
        paymentTable.getColumnModel().getColumn(9).setPreferredWidth(80);  // 缴费方式
        paymentTable.getColumnModel().getColumn(10).setPreferredWidth(120); // 交易号
        paymentTable.getColumnModel().getColumn(11).setPreferredWidth(150); // 备注
        
        // 设置状态列的渲染器
        paymentTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                         boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if ("已缴费".equals(value)) {
                        c.setForeground(SUCCESS_COLOR);
                    } else if ("未缴费".equals(value)) {
                        c.setForeground(ERROR_COLOR);
                    } else {
                        c.setForeground(TEXT_COLOR);
                    }
                }
                return c;
            }
        });
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("缴费记录列表"));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * 创建统计面板
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        totalCountLabel = new JLabel("总记录数: 0");
        unpaidCountLabel = new JLabel("未缴费: 0");
        paidCountLabel = new JLabel("已缴费: 0");
        totalAmountLabel = new JLabel("总金额: ¥0.00");
        
        unpaidCountLabel.setForeground(ERROR_COLOR);
        paidCountLabel.setForeground(SUCCESS_COLOR);
        totalAmountLabel.setForeground(PRIMARY_COLOR);
        
        panel.add(totalCountLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(unpaidCountLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(paidCountLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(totalAmountLabel);
        
        return panel;
    }
    
    /**
     * 创建按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // 创建单个账单按钮
        createBillButton = new JButton("创建账单");
        createBillButton.setBackground(PRIMARY_COLOR);
        createBillButton.setForeground(Color.WHITE);
        createBillButton.addActionListener(e -> showCreateBillDialog());
        
        // 批量创建账单按钮
        batchCreateButton = new JButton("批量创建");
        batchCreateButton.setBackground(PRIMARY_COLOR);
        batchCreateButton.setForeground(Color.WHITE);
        batchCreateButton.addActionListener(e -> showBatchCreateDialog());
        
        // 确认缴费按钮
        confirmPaymentButton = new JButton("确认缴费");
        confirmPaymentButton.setBackground(SUCCESS_COLOR);
        confirmPaymentButton.setForeground(Color.WHITE);
        confirmPaymentButton.addActionListener(e -> confirmPayment());
        
        // 删除按钮
        deleteButton = new JButton("删除记录");
        deleteButton.setBackground(ERROR_COLOR);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deletePaymentRecord());
        
        panel.add(createBillButton);
        panel.add(batchCreateButton);
        panel.add(confirmPaymentButton);
        panel.add(deleteButton);
        
        return panel;
    }
    
    /**
     * 加载数据
     */
    private void loadData() {
        try {
            // 根据当前用户的管理楼栋获取缴费记录
            if (currentUser.getManagedBuildingID() != null) {
                paymentList = paymentController.getPaymentRecordsByBuilding(currentUser.getManagedBuildingID());
            } else {
                paymentList = paymentController.getAllPaymentRecords();
            }
            
            updateTable();
            updateStats();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 更新表格数据
     */
    private void updateTable() {
        // 清空表格
        tableModel.setRowCount(0);
        
        if (paymentList == null) {
            return;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        // 添加数据到表格
        for (PaymentRecord payment : paymentList) {
            Object[] row = {
                payment.getPaymentID(),
                payment.getUserName() != null ? payment.getUserName() : "未知",
                payment.getBuildingName() != null ? payment.getBuildingName() : "未知",
                payment.getRoomNumber(),
                paymentController.getPaymentTypeText(payment.getPaymentType()),
                String.format("%.2f", payment.getAmount()),
                paymentController.getPaymentStatusText(payment.getPaymentStatus()),
                payment.getDueDate() != null ? dateFormat.format(payment.getDueDate()) : "",
                payment.getPaymentTime() != null ? dateFormat.format(payment.getPaymentTime()) : "",
                payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "",
                payment.getTransactionNo() != null ? payment.getTransactionNo() : "",
                payment.getRemark() != null ? payment.getRemark() : ""
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * 更新统计信息
     */
    private void updateStats() {
        if (paymentList == null) {
            totalCountLabel.setText("总记录数: 0");
            unpaidCountLabel.setText("未缴费: 0");
            paidCountLabel.setText("已缴费: 0");
            totalAmountLabel.setText("总金额: ¥0.00");
            return;
        }
        
        int totalCount = paymentList.size();
        int unpaidCount = 0;
        int paidCount = 0;
        double totalAmount = 0.0;
        
        for (PaymentRecord payment : paymentList) {
            if (payment.getPaymentStatus() == 0) {
                unpaidCount++;
            } else {
                paidCount++;
            }
            totalAmount += payment.getAmount();
        }
        
        totalCountLabel.setText("总记录数: " + totalCount);
        unpaidCountLabel.setText("未缴费: " + unpaidCount);
        paidCountLabel.setText("已缴费: " + paidCount);
        totalAmountLabel.setText("总金额: ¥" + String.format("%.2f", totalAmount));
    }
    
    /**
     * 筛选数据
     */
    private void filterData() {
        // TODO: 实现数据筛选功能
        loadData();
    }
    
    /**
     * 搜索数据
     */
    private void searchData() {
        // TODO: 实现数据搜索功能
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
        } else {
            // 实现搜索逻辑
            loadData();
        }
    }
    
    /**
     * 显示创建账单对话框
     */
    private void showCreateBillDialog() {
        CreateBillDialog dialog = new CreateBillDialog(SwingUtilities.getWindowAncestor(this), currentUser);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            loadData();
        }
    }
    
    /**
     * 显示批量创建对话框
     */
    private void showBatchCreateDialog() {
        BatchCreateBillDialog dialog = new BatchCreateBillDialog(SwingUtilities.getWindowAncestor(this), currentUser);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            loadData();
        }
    }
    
    /**
     * 确认缴费
     */
    private void confirmPayment() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一条缴费记录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        PaymentRecord payment = paymentList.get(selectedRow);
        if (payment.getPaymentStatus() == 1) {
            JOptionPane.showMessageDialog(this, "该记录已经缴费", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        ConfirmPaymentDialog dialog = new ConfirmPaymentDialog(SwingUtilities.getWindowAncestor(this), payment);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            loadData();
        }
    }
    
    /**
     * 删除缴费记录
     */
    private void deletePaymentRecord() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一条缴费记录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        PaymentRecord payment = paymentList.get(selectedRow);
        
        int result = JOptionPane.showConfirmDialog(this, 
                "确定要删除这条缴费记录吗？\n缴费ID: " + payment.getPaymentID(), 
                "确认删除", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            boolean success = paymentController.deletePaymentRecord(payment.getPaymentID());
            if (success) {
                JOptionPane.showMessageDialog(this, "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // 内部对话框类
    
    /**
     * 创建账单对话框
     */
    private class CreateBillDialog extends JDialog {
        private boolean confirmed = false;
        private JTextField userIDField;
        private JTextField roomNumberField;
        private JComboBox<String> typeComboBox;
        private JTextField amountField;
        private JTextField dueDateField;
        private JTextArea remarkArea;
        
        public CreateBillDialog(Window parent, User user) {
            super(parent, "创建账单", ModalityType.APPLICATION_MODAL);
            initDialog();
        }
        
        private void initDialog() {
            setSize(400, 500);
            setLocationRelativeTo(getParent());
            setLayout(new BorderLayout());
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // 用户ID
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("用户ID:"), gbc);
            gbc.gridx = 1;
            userIDField = new JTextField(15);
            formPanel.add(userIDField, gbc);
            
            // 房间号
            gbc.gridx = 0; gbc.gridy = 1;
            formPanel.add(new JLabel("房间号:"), gbc);
            gbc.gridx = 1;
            roomNumberField = new JTextField(15);
            formPanel.add(roomNumberField, gbc);
            
            // 缴费类型
            gbc.gridx = 0; gbc.gridy = 2;
            formPanel.add(new JLabel("缴费类型:"), gbc);
            gbc.gridx = 1;
            typeComboBox = new JComboBox<>(new String[]{"物业费", "水费", "电费", "其他"});
            formPanel.add(typeComboBox, gbc);
            
            // 金额
            gbc.gridx = 0; gbc.gridy = 3;
            formPanel.add(new JLabel("金额:"), gbc);
            gbc.gridx = 1;
            amountField = new JTextField(15);
            formPanel.add(amountField, gbc);
            
            // 截止日期
            gbc.gridx = 0; gbc.gridy = 4;
            formPanel.add(new JLabel("截止日期:"), gbc);
            gbc.gridx = 1;
            dueDateField = new JTextField(15);
            dueDateField.setToolTipText("格式: yyyy-MM-dd");
            formPanel.add(dueDateField, gbc);
            
            // 备注
            gbc.gridx = 0; gbc.gridy = 5;
            formPanel.add(new JLabel("备注:"), gbc);
            gbc.gridx = 1;
            remarkArea = new JTextArea(3, 15);
            formPanel.add(new JScrollPane(remarkArea), gbc);
            
            // 按钮面板
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton confirmButton = new JButton("确认");
            JButton cancelButton = new JButton("取消");
            
            confirmButton.addActionListener(e -> {
                if (validateAndSave()) {
                    confirmed = true;
                    dispose();
                }
            });
            
            cancelButton.addActionListener(e -> dispose());
            
            buttonPanel.add(confirmButton);
            buttonPanel.add(cancelButton);
            
            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        private boolean validateAndSave() {
            try {
                int userID = Integer.parseInt(userIDField.getText().trim());
                String roomNumber = roomNumberField.getText().trim();
                int paymentType = typeComboBox.getSelectedIndex() + 1;
                double amount = Double.parseDouble(amountField.getText().trim());
                String dueDateStr = dueDateField.getText().trim();
                String remark = remarkArea.getText().trim();
                
                if (roomNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "请输入房间号", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                Timestamp dueDate = null;
                if (!dueDateStr.isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(dueDateStr);
                        dueDate = new Timestamp(date.getTime());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "日期格式错误，请使用 yyyy-MM-dd 格式", "错误", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                
                boolean success = paymentController.createPaymentBill(userID, 
                        currentUser.getManagedBuildingID() != null ? currentUser.getManagedBuildingID() : 1, 
                        roomNumber, paymentType, amount, dueDate, remark);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "账单创建成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "账单创建失败", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入正确的数字格式", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        public boolean isConfirmed() {
            return confirmed;
        }
    }
    
    /**
     * 批量创建账单对话框
     */
    private class BatchCreateBillDialog extends JDialog {
        private boolean confirmed = false;
        private JComboBox<String> typeComboBox;
        private JTextField amountField;
        private JTextField dueDateField;
        private JTextArea remarkArea;
        
        public BatchCreateBillDialog(Window parent, User user) {
            super(parent, "批量创建账单", ModalityType.APPLICATION_MODAL);
            initDialog();
        }
        
        private void initDialog() {
            setSize(400, 400);
            setLocationRelativeTo(getParent());
            setLayout(new BorderLayout());
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // 说明
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            JLabel infoLabel = new JLabel("<html>将为当前管理楼栋的所有业主创建账单</html>");
            infoLabel.setForeground(PRIMARY_COLOR);
            formPanel.add(infoLabel, gbc);
            
            gbc.gridwidth = 1;
            
            // 缴费类型
            gbc.gridx = 0; gbc.gridy = 1;
            formPanel.add(new JLabel("缴费类型:"), gbc);
            gbc.gridx = 1;
            typeComboBox = new JComboBox<>(new String[]{"物业费", "水费", "电费", "其他"});
            formPanel.add(typeComboBox, gbc);
            
            // 金额
            gbc.gridx = 0; gbc.gridy = 2;
            formPanel.add(new JLabel("金额:"), gbc);
            gbc.gridx = 1;
            amountField = new JTextField(15);
            formPanel.add(amountField, gbc);
            
            // 截止日期
            gbc.gridx = 0; gbc.gridy = 3;
            formPanel.add(new JLabel("截止日期:"), gbc);
            gbc.gridx = 1;
            dueDateField = new JTextField(15);
            dueDateField.setToolTipText("格式: yyyy-MM-dd");
            formPanel.add(dueDateField, gbc);
            
            // 备注
            gbc.gridx = 0; gbc.gridy = 4;
            formPanel.add(new JLabel("备注:"), gbc);
            gbc.gridx = 1;
            remarkArea = new JTextArea(3, 15);
            formPanel.add(new JScrollPane(remarkArea), gbc);
            
            // 按钮面板
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton confirmButton = new JButton("确认");
            JButton cancelButton = new JButton("取消");
            
            confirmButton.addActionListener(e -> {
                if (validateAndSave()) {
                    confirmed = true;
                    dispose();
                }
            });
            
            cancelButton.addActionListener(e -> dispose());
            
            buttonPanel.add(confirmButton);
            buttonPanel.add(cancelButton);
            
            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        private boolean validateAndSave() {
            try {
                int paymentType = typeComboBox.getSelectedIndex() + 1;
                double amount = Double.parseDouble(amountField.getText().trim());
                String dueDateStr = dueDateField.getText().trim();
                String remark = remarkArea.getText().trim();
                
                Timestamp dueDate = null;
                if (!dueDateStr.isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(dueDateStr);
                        dueDate = new Timestamp(date.getTime());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "日期格式错误，请使用 yyyy-MM-dd 格式", "错误", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                
                if (currentUser.getManagedBuildingID() == null) {
                    JOptionPane.showMessageDialog(this, "当前用户未分配管理楼栋", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                int successCount = paymentController.createBatchPaymentBills(
                        currentUser.getManagedBuildingID(), paymentType, amount, dueDate, remark);
                
                if (successCount > 0) {
                    JOptionPane.showMessageDialog(this, "成功创建 " + successCount + " 条账单", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "账单创建失败，可能该楼栋没有业主", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入正确的金额格式", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        public boolean isConfirmed() {
            return confirmed;
        }
    }
    
    /**
     * 确认缴费对话框
     */
    private class ConfirmPaymentDialog extends JDialog {
        private boolean confirmed = false;
        private PaymentRecord payment;
        private JComboBox<String> methodComboBox;
        private JTextField transactionField;
        
        public ConfirmPaymentDialog(Window parent, PaymentRecord payment) {
            super(parent, "确认缴费", ModalityType.APPLICATION_MODAL);
            this.payment = payment;
            initDialog();
        }
        
        private void initDialog() {
            setSize(400, 300);
            setLocationRelativeTo(getParent());
            setLayout(new BorderLayout());
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // 缴费信息
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            JLabel infoLabel = new JLabel("<html>缴费ID: " + payment.getPaymentID() + "<br>" +
                    "金额: ¥" + String.format("%.2f", payment.getAmount()) + "</html>");
            formPanel.add(infoLabel, gbc);
            
            gbc.gridwidth = 1;
            
            // 缴费方式
            gbc.gridx = 0; gbc.gridy = 1;
            formPanel.add(new JLabel("缴费方式:"), gbc);
            gbc.gridx = 1;
            methodComboBox = new JComboBox<>(new String[]{"支付宝", "微信", "银行卡", "现金", "其他"});
            formPanel.add(methodComboBox, gbc);
            
            // 交易号
            gbc.gridx = 0; gbc.gridy = 2;
            formPanel.add(new JLabel("交易号:"), gbc);
            gbc.gridx = 1;
            transactionField = new JTextField(15);
            formPanel.add(transactionField, gbc);
            
            // 按钮面板
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton confirmButton = new JButton("确认");
            JButton cancelButton = new JButton("取消");
            
            confirmButton.addActionListener(e -> {
                if (validateAndSave()) {
                    confirmed = true;
                    dispose();
                }
            });
            
            cancelButton.addActionListener(e -> dispose());
            
            buttonPanel.add(confirmButton);
            buttonPanel.add(cancelButton);
            
            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        private boolean validateAndSave() {
            String paymentMethod = (String) methodComboBox.getSelectedItem();
            String transactionNo = transactionField.getText().trim();
            
            boolean success = paymentController.confirmPayment(payment.getPaymentID(), paymentMethod, transactionNo);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "缴费确认成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "缴费确认失败", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        public boolean isConfirmed() {
            return confirmed;
        }
    }
}