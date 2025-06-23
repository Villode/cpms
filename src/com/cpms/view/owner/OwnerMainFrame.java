package com.cpms.view.owner;

import com.cpms.model.entity.User;
import com.cpms.util.PermissionValidator;
import com.cpms.util.config.ConfigManager;
import com.cpms.view.owner.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 业主主界面
 * 包含业主的功能模块
 */
public class OwnerMainFrame extends JFrame {
    // 配置管理器
    private final ConfigManager config = ConfigManager.getInstance();
    
    // 当前用户
    private User currentUser;
    
    // 界面组件
    private JPanel contentPanel;
    private JMenuBar menuBar;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = Color.decode(config.getProperty("ui.color.primary", "#1E88E5"));
    private final Color SECONDARY_COLOR = Color.decode(config.getProperty("ui.color.secondary", "#F5F5F5"));
    private final Color ACCENT_COLOR = Color.decode(config.getProperty("ui.color.accent", "#FF8F00"));
    private final Color TEXT_COLOR = Color.decode(config.getProperty("ui.color.text", "#333333"));
    
    /**
     * 构造方法，初始化业主主界面
     * @param user 当前用户
     */
    public OwnerMainFrame(User user) {
        // 验证用户对象
        if (user == null) {
            JOptionPane.showMessageDialog(null, "用户信息无效，请重新登录！", "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return;
        }
        
        this.currentUser = user;
        
        // 打印调试信息
        System.out.println("业主主界面初始化 - 用户ID: " + user.getUserID() + 
                          ", 用户名: " + user.getUsername() + 
                          ", 真实姓名: " + user.getRealName() + 
                          ", 角色ID: " + user.getRoleID());
        
        // 设置窗口标题
        setTitle("小区物业管理系统 - 业主端");
        
        // 设置窗口大小和位置
        setSize(1024, 768);
        setLocationRelativeTo(null);
        
        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 初始化界面组件
        initComponents();
        
        // 创建菜单栏
        createMenuBar();
        
        // 显示欢迎界面
        showWelcome();
        
        // 显示窗口
        setVisible(true);
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建内容面板
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(SECONDARY_COLOR);
        
        // 设置内容面板为窗口的内容面板
        setContentPane(contentPanel);
    }
    
    /**
     * 创建菜单栏
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();
        
        // 提交报修 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "REPAIR_SUBMIT")) {
            JMenuItem repairSubmitMenuItem = new JMenuItem("提交报修");
            repairSubmitMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showRepairSubmit();
                }
            });
            menuBar.add(repairSubmitMenuItem);
        }
        
        // 报修记录 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "REPAIR_QUERY_OWN")) {
            JMenuItem repairHistoryMenuItem = new JMenuItem("报修记录");
            repairHistoryMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showRepairHistory();
                }
            });
            menuBar.add(repairHistoryMenuItem);
        }
        
        // 账单查询 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "PAYMENT_QUERY_OWN")) {
            JMenuItem paymentBillMenuItem = new JMenuItem("账单查询");
            paymentBillMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showPaymentBill();
                }
            });
            menuBar.add(paymentBillMenuItem);
        }
        
        // 缴费记录 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "PAYMENT_HISTORY_OWN")) {
            JMenuItem paymentHistoryMenuItem = new JMenuItem("缴费记录");
            paymentHistoryMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showPaymentHistory();
                }
            });
            menuBar.add(paymentHistoryMenuItem);
        }
        
        // 我的车位 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "PARKING_VIEW_OWN")) {
            JMenuItem myParkingMenuItem = new JMenuItem("我的车位");
            myParkingMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showMyParking();
                }
            });
            menuBar.add(myParkingMenuItem);
        }
        
        // 查看信息 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "PROFILE_VIEW")) {
            JMenuItem profileViewMenuItem = new JMenuItem("查看信息");
            profileViewMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showProfileView();
                }
            });
            menuBar.add(profileViewMenuItem);
        }
        
        // 修改密码 - 根据权限动态显示
        if (PermissionValidator.canAccessMenu(currentUser, "PASSWORD_CHANGE")) {
            JMenuItem changePasswordMenuItem = new JMenuItem("修改密码");
            changePasswordMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showChangePassword();
                }
            });
            menuBar.add(changePasswordMenuItem);
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
     * 显示欢迎界面
     */
    private void showWelcome() {
        // 创建欢迎面板
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(SECONDARY_COLOR);
        
        // 欢迎标签
        JLabel welcomeLabel = new JLabel("欢迎使用小区物业管理系统", JLabel.CENTER);
        welcomeLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.BOLD, 
                config.getIntProperty("ui.font.size.xlarge", 24)));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        
        // 用户信息标签
        String realName = (currentUser.getRealName() != null) ? currentUser.getRealName() : "未设置";
        String username = currentUser.getUsername();
        JLabel userLabel = new JLabel("当前用户: " + realName + " (" + username + ")", JLabel.CENTER);
        userLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.large", 16)));
        userLabel.setForeground(TEXT_COLOR);
        
        // 提示标签
        JLabel tipLabel = new JLabel("请从上方菜单选择功能", JLabel.CENTER);
        tipLabel.setFont(new Font(config.getProperty("ui.font.name", "微软雅黑"), Font.PLAIN, 
                config.getIntProperty("ui.font.size.normal", 14)));
        tipLabel.setForeground(TEXT_COLOR);
        
        // 添加组件到欢迎面板
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        centerPanel.setBackground(SECONDARY_COLOR);
        centerPanel.add(welcomeLabel);
        centerPanel.add(userLabel);
        centerPanel.add(tipLabel);
        
        welcomePanel.add(centerPanel, BorderLayout.CENTER);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示提交报修界面
     */
    private void showRepairSubmit() {
        // 创建报修提交面板
        RepairSubmitPanel repairSubmitPanel = new RepairSubmitPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(repairSubmitPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示报修记录界面
     */
    private void showRepairHistory() {
        // 创建报修记录面板
        RepairHistoryPanel repairHistoryPanel = new RepairHistoryPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(repairHistoryPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示账单查询界面
     */
    private void showPaymentBill() {
        // 创建账单查询面板
        PaymentBillPanel paymentBillPanel = new PaymentBillPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(paymentBillPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示缴费记录界面
     */
    private void showPaymentHistory() {
        // 创建缴费记录面板
        PaymentHistoryPanel paymentHistoryPanel = new PaymentHistoryPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(paymentHistoryPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示我的车位界面
     */
    private void showMyParking() {
        // 创建车位管理面板
        ParkingPanel parkingPanel = new ParkingPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(parkingPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示个人信息界面
     */
    private void showProfileView() {
        // 创建个人信息面板
        ProfileViewPanel profileViewPanel = new ProfileViewPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(profileViewPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示修改密码界面
     */
    private void showChangePassword() {
        // 创建修改密码面板
        ChangePasswordPanel changePasswordPanel = new ChangePasswordPanel(currentUser);
        
        // 更新内容面板
        contentPanel.removeAll();
        contentPanel.add(changePasswordPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}