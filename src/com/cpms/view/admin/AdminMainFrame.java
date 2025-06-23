package com.cpms.view.admin;

import com.cpms.model.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 管理员主界面
 * 管理员角色的主界面框架
 */
public class AdminMainFrame extends JFrame {
    // 用户信息
    private User currentUser;
    
    // 界面组件
    private JPanel contentPanel;
    private JMenuBar menuBar;
    
    /**
     * 构造方法，初始化管理员主界面
     * @param user 当前登录用户
     */
    public AdminMainFrame(User user) {
        this.currentUser = user;
        
        // 设置窗口属性
        setTitle("小区物业管理系统 - 管理员");
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
        
        // 角色管理菜单
        JMenuItem roleMenuItem = new JMenuItem("角色管理");
        roleMenuItem.addActionListener(e -> showRoleManagement());
        
        // 角色权限分配菜单
        JMenuItem rolePermissionMenuItem = new JMenuItem("角色权限分配");
        rolePermissionMenuItem.addActionListener(e -> showRolePermissionAssignment());
        

        
        // 用户管理菜单
        JMenuItem userMenuItem = new JMenuItem("用户管理");
        userMenuItem.addActionListener(e -> showUserManagement());
        
        // 楼栋信息菜单
        JMenuItem buildingMenuItem = new JMenuItem("楼栋信息");
        buildingMenuItem.addActionListener(e -> showBuildingManagement());
        

        
        // 退出系统菜单
        JMenu exitMenu = new JMenu("退出系统");
        JMenuItem exitMenuItem = new JMenuItem("退出系统");
        exitMenuItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                this,
                "确定要退出系统吗？",
                "退出确认",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        exitMenu.add(exitMenuItem);
        
        // 添加所有菜单到菜单栏
        menuBar.add(roleMenuItem);
        menuBar.add(rolePermissionMenuItem);
        menuBar.add(userMenuItem);
        menuBar.add(buildingMenuItem);
        menuBar.add(exitMenu);
        
        // 设置菜单栏
        setJMenuBar(menuBar);
    }
    
    /**
     * 显示角色管理界面
     */
    private void showRoleManagement() {
        // 清空内容面板
        contentPanel.removeAll();
        
        // 添加角色管理面板
        RoleManagementPanel rolePanel = new RoleManagementPanel();
        contentPanel.add(rolePanel, BorderLayout.CENTER);
        
        // 刷新界面
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示角色权限分配界面
     */
    private void showRolePermissionAssignment() {
        // 清空内容面板
        contentPanel.removeAll();
        
        // 添加角色权限分配面板
        RolePermissionPanel rolePermissionPanel = new RolePermissionPanel();
        contentPanel.add(rolePermissionPanel, BorderLayout.CENTER);
        
        // 刷新界面
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    

    
    /**
     * 显示用户管理界面
     */
    private void showUserManagement() {
        // 清空内容面板
        contentPanel.removeAll();
        
        // 添加用户管理面板
        UserManagementPanel userPanel = new UserManagementPanel();
        contentPanel.add(userPanel, BorderLayout.CENTER);
        
        // 刷新界面
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * 显示楼栋管理界面
     */
    private void showBuildingManagement() {
        // 清空内容面板
        contentPanel.removeAll();
        
        // 添加楼栋管理面板
        BuildingManagementPanel buildingPanel = new BuildingManagementPanel();
        contentPanel.add(buildingPanel, BorderLayout.CENTER);
        
        // 刷新界面
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    

    


}