package com.cpms.test;

import com.cpms.model.entity.User;
import com.cpms.view.admin.AdminMainFrame;
import com.cpms.view.manager.ManagerMainFrame;
import com.cpms.view.owner.OwnerMainFrame;
import com.cpms.view.common.LoginFrame;
import com.cpms.view.admin.AdminPermissionManagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 简单测试启动器
 * 使用固定的用户信息直接启动三个不同角色的窗口，避免数据库查询
 */
public class SimpleTestLauncher extends JFrame {
    
    private JButton loginButton;
    private JButton adminButton;
    private JButton managerButton;
    private JButton ownerButton;
    private JButton allButton;
    private JButton permissionInitButton;
    
    /**
     * 构造方法
     */
    public SimpleTestLauncher() {
        // 设置窗口标题
        setTitle("CPMS 简单测试启动器");
        
        // 设置窗口大小和位置
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 初始化组件
        initComponents();
        
        // 显示窗口
        setVisible(true);
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        // 创建面板
        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建标签
        JLabel titleLabel = new JLabel("请选择要启动的窗口", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        
        // 创建登录按钮
        loginButton = new JButton("启动登录界面");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        loginButton.setBackground(new Color(30, 136, 229));
        loginButton.setForeground(Color.BLACK);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchLoginWindow();
            }
        });
        
        // 创建按钮
        adminButton = new JButton("启动管理员窗口");
        adminButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        adminButton.setForeground(Color.BLACK);
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchAdminWindow();
            }
        });
        
        managerButton = new JButton("启动物业管家窗口");
        managerButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        managerButton.setForeground(Color.BLACK);
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchManagerWindow();
            }
        });
        
        ownerButton = new JButton("启动业主窗口");
        ownerButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        ownerButton.setForeground(Color.BLACK);
        ownerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchOwnerWindow();
            }
        });
        
        allButton = new JButton("启动所有窗口");
        allButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        allButton.setForeground(Color.BLACK);
        allButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchAllWindows();
            }
        });
        
        // 创建权限初始化按钮
        permissionInitButton = new JButton("初始化权限数据");
        permissionInitButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        permissionInitButton.setForeground(Color.BLACK);
        permissionInitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initPermissionData();
            }
        });
        
        // 添加组件到面板
        panel.add(titleLabel);
        panel.add(loginButton);
        panel.add(adminButton);
        panel.add(managerButton);
        panel.add(ownerButton);
        panel.add(permissionInitButton);
        panel.add(allButton);
        
        // 设置内容面板
        setContentPane(panel);
    }
    
    /**
     * 启动管理员窗口
     */
    private void launchAdminWindow() {
        try {
            // 创建管理员用户
            User adminUser = createAdminUser();
            
            // 启动管理员窗口
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new AdminMainFrame(adminUser);
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "启动管理员窗口失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * 启动物业管家窗口
     */
    private void launchManagerWindow() {
        try {
            // 创建物业管家用户
            User managerUser = createManagerUser();
            
            // 启动物业管家窗口
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ManagerMainFrame(managerUser);
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "启动物业管家窗口失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * 启动业主窗口
     */
    private void launchOwnerWindow() {
        try {
            // 创建业主用户
            User ownerUser = createOwnerUser();
            
            // 启动业主窗口
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new OwnerMainFrame(ownerUser);
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "启动业主窗口失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * 启动登录窗口
     */
    private void launchLoginWindow() {
        try {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "启动登录窗口失败: " + e.getMessage(),
                "错误",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * 初始化权限数据
     */
    private void initPermissionData() {
        try {
            // 创建权限初始化界面
            JFrame permissionFrame = new JFrame("权限数据初始化");
            permissionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            permissionFrame.setSize(800, 600);
            permissionFrame.setLocationRelativeTo(this);
            
            // 添加权限管理面板
            AdminPermissionManagePanel permissionPanel = new AdminPermissionManagePanel();
            permissionFrame.add(permissionPanel);
            
            permissionFrame.setVisible(true);
            
            // 显示提示信息
            JOptionPane.showMessageDialog(
                this,
                "权限初始化界面已打开\n" +
                "请在新窗口中进行权限数据初始化操作",
                "权限初始化",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "打开权限初始化界面失败: " + e.getMessage(),
                "错误",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * 启动所有窗口
     */
    private void launchAllWindows() {
        launchAdminWindow();
        launchManagerWindow();
        launchOwnerWindow();
        launchLoginWindow();
        initPermissionData();
    }
    
    /**
     * 创建管理员用户
     * @return 管理员用户对象
     */
    private User createAdminUser() {
        User adminUser = new User();
        adminUser.setUserID(1);
        adminUser.setUsername("admin");
        adminUser.setPassword("e10adc3949ba59abbe56e057f20f883e"); // 123456的MD5
        adminUser.setRealName("系统管理员");
        adminUser.setPhone("13800138000");
        adminUser.setRoleID(1); // 1-管理员
        adminUser.setAccountStatus(1); // 1-正常
        return adminUser;
    }
    
    /**
     * 创建物业管家用户
     * @return 物业管家用户对象
     */
    private User createManagerUser() {
        User managerUser = new User();
        managerUser.setUserID(2);
        managerUser.setUsername("manager");
        managerUser.setPassword("e10adc3949ba59abbe56e057f20f883e"); // 123456的MD5
        managerUser.setRealName("物业管家");
        managerUser.setPhone("13900139000");
        managerUser.setRoleID(2); // 2-物业管家
        managerUser.setManagedBuildingID(1); // 管理的楼栋ID
        managerUser.setAccountStatus(1); // 1-正常
        return managerUser;
    }
    
    /**
     * 创建业主用户
     * @return 业主用户对象
     */
    private User createOwnerUser() {
        User ownerUser = new User();
        ownerUser.setUserID(3);
        ownerUser.setUsername("owner");
        ownerUser.setPassword("e10adc3949ba59abbe56e057f20f883e"); // 123456的MD5
        ownerUser.setRealName("测试业主");
        ownerUser.setPhone("13700137000");
        ownerUser.setRoleID(3); // 3-业主
        ownerUser.setBuildingID(1); // 所属楼栋ID
        ownerUser.setAccountStatus(1); // 1-正常
        return ownerUser;
    }
    
    /**
     * 主方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 设置界面风格为系统风格
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 启动测试启动器
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleTestLauncher();
            }
        });
    }
}