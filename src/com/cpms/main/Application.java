package com.cpms.main;

import com.cpms.controller.common.LoginController;
import com.cpms.view.common.LoginFrame;
import com.cpms.util.config.ConfigManager;

import javax.swing.*;
import java.awt.*;

/**
 * 应用程序入口类
 * 系统启动类 - 统一的系统入口点
 */
public class Application {
    
    /**
     * 主方法，程序入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 设置界面外观
        setLookAndFeel();
        
        // 加载配置
        ConfigManager configManager = ConfigManager.getInstance();
        
        // 设置全局字体
        setGlobalFont(configManager.getProperty("ui.font.name", "微软雅黑"));
        
        // 初始化系统数据
        initSystemData();
        
        // 显示登录界面
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame();
            }
        });
    }
    
    /**
     * 设置界面外观
     */
    private static void setLookAndFeel() {
        try {
            // 使用系统外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 初始化系统数据
     */
    private static void initSystemData() {
        try {
            System.out.println("开始初始化系统数据...");
            
            // 创建登录控制器
            LoginController loginController = new LoginController();
            
            // 初始化管理员账号
            boolean result = loginController.initAdminAccount();
            if (result) {
                System.out.println("系统初始化成功 - 默认管理员账号: admin/admin123");
            } else {
                System.err.println("系统初始化失败 - 无法创建管理员账号");
            }
            
            System.out.println("系统初始化完成");
        } catch (Exception e) {
            System.err.println("系统初始化异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 设置全局字体
     * @param fontName 字体名称
     */
    private static void setGlobalFont(String fontName) {
        Font font = new Font(fontName, Font.PLAIN, 12);
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("CheckBox.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("MenuBar.font", font);
        UIManager.put("ToolTip.font", font);
        UIManager.put("List.font", font);
        UIManager.put("OptionPane.font", font);
        UIManager.put("Panel.font", font);
        UIManager.put("ScrollPane.font", font);
        UIManager.put("Viewport.font", font);
    }
}