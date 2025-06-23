package com.cpms.view.owner;

import com.cpms.controller.common.RepairController;
import com.cpms.model.entity.Repair;
import com.cpms.model.entity.User;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 报修查询面板
 * 业主查询报修进度的界面
 */
public class RepairQueryPanel extends JPanel {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 控制器
    private RepairController repairController;
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JTable repairTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton cancelButton;
    private JButton detailButton;
    
    // 报修数据
    private List<Repair> repairList;
    
    // 当前选中的报修ID
    private int currentRepairID = -1;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color SUCCESS_COLOR = Color.decode(config.getProperty("ui.color.success", "#4CAF50"));
    private final Color ERROR_COLOR = Color.decode(config.getProperty("ui.color.error", "#F44336"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法，初始化报修查询面板
     * @param user 当前用户
     */
    public RepairQueryPanel(User user) {
        this.currentUser = user;
        
        // 初始化控制器
        repairController = new RepairController();
        
        // 设置布局
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 创建界面组件
        initComponents();
        
        // 加载报修数据
        loadRepairData();
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加到主面板
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建表格面板
     * @return 表格面板
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                "报修记录",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                        config.getIntProperty("ui.font.size.large", 16)),
                PRIMARY_COLOR));
        
        // 创建表格模型
        String[] columnNames = {"报修ID", "楼栋", "房间号", "报修类型", "报修描述", "报修状态", "处理人", "处理意见", "创建时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        // 创建表格
        repairTable = new JTable(tableModel);
        repairTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        repairTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        repairTable.getTableHeader().setReorderingAllowed(false);
        
        // 设置列宽
        repairTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        repairTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        repairTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        repairTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        repairTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        repairTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        repairTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        repairTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        repairTable.getColumnModel().getColumn(8).setPreferredWidth(120);
        
        // 添加表格选择监听器
        repairTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = repairTable.getSelectedRow();
                if (row >= 0) {
                    currentRepairID = (int) tableModel.getValueAt(row, 0);
                    
                    // 根据报修状态启用/禁用取消按钮
                    int status = getRepairStatusByID(currentRepairID);
                    cancelButton.setEnabled(status == 0 || status == 1); // 只有待处理或处理中的报修可以取消
                }
            }
        });
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(repairTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建按钮面板
     * @return 按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // 刷新按钮
        refreshButton = createStyledButton("刷新", PRIMARY_COLOR);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRepairData();
            }
        });
        
        // 取消按钮
        cancelButton = createStyledButton("取消报修", ERROR_COLOR);
        cancelButton.setEnabled(false); // 初始状态禁用
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelRepair();
            }
        });
        
        // 详情按钮
        detailButton = createStyledButton("查看详情", ACCENT_COLOR);
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRepairDetail();
            }
        });
        
        panel.add(refreshButton);
        panel.add(cancelButton);
        panel.add(detailButton);
        
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
        button.setOpaque(true);
        button.setBorderPainted(false);
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
    private void loadRepairData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 获取当前用户的报修列表
        repairList = repairController.getRepairsByUserID(currentUser.getUserID());
        
        // 日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        // 添加报修数据到表格
        for (Repair repair : repairList) {
            Object[] rowData = {
                repair.getRepairID(),
                repair.getBuildingName(),
                repair.getRoomNumber(),
                repair.getRepairTypeText(),
                repair.getRepairDesc(),
                repair.getRepairStatusText(),
                repair.getHandlerName() == null ? "" : repair.getHandlerName(),
                repair.getHandleOpinion() == null ? "" : repair.getHandleOpinion(),
                sdf.format(repair.getCreateTime())
            };
            tableModel.addRow(rowData);
        }
        
        // 重置选中状态
        currentRepairID = -1;
        cancelButton.setEnabled(false);
    }
    
    /**
     * 取消报修
     */
    private void cancelRepair() {
        if (currentRepairID <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择要取消的报修！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 确认取消
        int confirm = JOptionPane.showConfirmDialog(this, "确定要取消该报修吗？", "确认取消", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // 取消报修
        boolean success = repairController.cancelRepair(currentRepairID, currentUser.getUserID());
        
        if (success) {
            JOptionPane.showMessageDialog(this, "报修取消成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadRepairData(); // 刷新数据
        } else {
            JOptionPane.showMessageDialog(this, "报修取消失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 显示报修详情
     */
    private void showRepairDetail() {
        if (currentRepairID <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择要查看的报修！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 查找选中的报修
        Repair repair = null;
        for (Repair r : repairList) {
            if (r.getRepairID() == currentRepairID) {
                repair = r;
                break;
            }
        }
        
        if (repair == null) {
            JOptionPane.showMessageDialog(this, "未找到报修信息！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 构建详情信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder detailBuilder = new StringBuilder();
        detailBuilder.append("报修ID: ").append(repair.getRepairID()).append("\n");
        detailBuilder.append("楼栋: ").append(repair.getBuildingName()).append("\n");
        detailBuilder.append("房间号: ").append(repair.getRoomNumber()).append("\n");
        detailBuilder.append("报修类型: ").append(repair.getRepairTypeText()).append("\n");
        detailBuilder.append("报修描述: ").append(repair.getRepairDesc()).append("\n");
        detailBuilder.append("报修状态: ").append(repair.getRepairStatusText()).append("\n");
        detailBuilder.append("处理人: ").append(repair.getHandlerName() == null ? "未分配" : repair.getHandlerName()).append("\n");
        detailBuilder.append("处理意见: ").append(repair.getHandleOpinion() == null ? "暂无" : repair.getHandleOpinion()).append("\n");
        detailBuilder.append("创建时间: ").append(sdf.format(repair.getCreateTime())).append("\n");
        detailBuilder.append("更新时间: ").append(sdf.format(repair.getUpdateTime())).append("\n");
        
        // 显示详情对话框
        JTextArea detailArea = new JTextArea(detailBuilder.toString());
        detailArea.setEditable(false);
        detailArea.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(detailArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "报修详情", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 根据报修ID获取报修状态
     * @param repairID 报修ID
     * @return 报修状态
     */
    private int getRepairStatusByID(int repairID) {
        for (Repair repair : repairList) {
            if (repair.getRepairID() == repairID) {
                return repair.getRepairStatus();
            }
        }
        return -1;
    }
}