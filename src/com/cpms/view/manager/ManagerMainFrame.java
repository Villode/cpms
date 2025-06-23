package com.cpms.view.manager;

import com.cpms.model.entity.User;
import com.cpms.util.PermissionValidator;
import com.cpms.view.manager.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 物业管家主界面
 * 物业管家角色的主界面框架
 */
public class ManagerMainFrame extends JFrame {
    // 用户信息
    private User currentUser;
    
    // 界面组件
    private JPanel contentPanel;
    private JMenuBar menuBar;
    
    /**
     * 构造方法，初始化物业管家主界面
     * @param user 当前登录用户
     */
    public ManagerMainFrame(User user) {
        this.currentUser = user;
        
        // 设置窗口属性
        setTitle("小区物业管理系统 - 物业管家");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建界面
        initComponents();
        
        // 显示界面
        setVisible(true);
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建菜单栏
        createMenuBar();
        
        // 创建内容面板
        contentPanel = new JPanel(new BorderLayout());
        
        // 创建欢迎面板
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("欢迎，" + currentUser.getRealName() + "！");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel tipLabel = new JLabel("请从左侧菜单选择功能");
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        tipLabel.setHorizontalAlignment(JLabel.CENTER);
        
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.add(tipLabel, BorderLayout.SOUTH);
        
        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        
        // 设置内容面板
        setContentPane(contentPanel);
    }
    
    /**
     * 创建菜单栏
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();
        
        // 业主录入 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "OWNER_ENTRY")) {
            JMenuItem ownerEntryMenuItem = new JMenuItem("业主录入");
            ownerEntryMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showOwnerEntry();
                }
            });
            menuBar.add(ownerEntryMenuItem);
        }
        
        // 业主查询 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "OWNER_QUERY")) {
            JMenuItem ownerQueryMenuItem = new JMenuItem("业主查询");
            ownerQueryMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showOwnerQuery();
                }
            });
            menuBar.add(ownerQueryMenuItem);
        }
        

        
        // 报修处理 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "REPAIR_PROCESS")) {
            JMenuItem repairProcessMenuItem = new JMenuItem("报修处理");
            repairProcessMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showRepairProcess();
                }
            });
            menuBar.add(repairProcessMenuItem);
        }
        
        // 报修查询 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "REPAIR_QUERY")) {
            JMenuItem repairQueryMenuItem = new JMenuItem("报修查询");
            repairQueryMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showRepairQuery();
                }
            });
            menuBar.add(repairQueryMenuItem);
        }
        
        // 缴费管理 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "PAYMENT_MANAGE")) {
            JMenuItem paymentManageMenuItem = new JMenuItem("缴费管理");
            paymentManageMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showPaymentManage();
                }
            });
            menuBar.add(paymentManageMenuItem);
        }
        

        

        
        // 车位管理 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "PARKING_MANAGE")) {
            JMenuItem parkingManageMenuItem = new JMenuItem("车位管理");
            parkingManageMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showParkingManage();
                }
            });
            menuBar.add(parkingManageMenuItem);
        }
        
        // 退出系统 - 始终显示
        JMenuItem exitMenuItem = new JMenuItem("退出系统");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuBar.add(exitMenuItem);
        
        // 设置菜单栏
        setJMenuBar(menuBar);
    }
    
    /**
     * 显示业主录入界面
     */
    private void showOwnerEntry() {
        // 创建业主录入面板
        OwnerEntryPanel ownerEntryPanel = new OwnerEntryPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(ownerEntryPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示业主查询界面
     */
    private void showOwnerQuery() {
        // 创建业主查询面板
        OwnerQueryPanel ownerQueryPanel = new OwnerQueryPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(ownerQueryPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    

    
    /**
     * 显示报修处理界面
     */
    private void showRepairProcess() {
        // 创建报修处理面板
        RepairProcessPanel repairProcessPanel = new RepairProcessPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(repairProcessPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示报修查询界面
     */
    private void showRepairQuery() {
        // 创建报修查询面板
        RepairQueryPanel repairQueryPanel = new RepairQueryPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(repairQueryPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示物业费管理界面
     */
    private void showPaymentManage() {
        // 创建物业费管理面板
        PaymentManagePanel paymentManagePanel = new PaymentManagePanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(paymentManagePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    

    
    /**
     * 显示车位管理界面
     */
    private void showParkingManage() {
        // 创建车位管理面板
        ParkingManagePanel parkingPanel = new ParkingManagePanel(currentUser);
        
        // 清空内容面板并添加新面板
        contentPanel.removeAll();
        contentPanel.add(parkingPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}