package com.cpms.util.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 配置管理器
 * 用于读取和管理系统配置
 */
public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;
    
    /**
     * 私有构造方法，加载配置文件
     */
    private ConfigManager() {
        properties = new Properties();
        loadConfig();
    }
    
    /**
     * 获取配置管理器实例（单例模式）
     * @return ConfigManager 配置管理器实例
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * 加载配置文件
     */
    private void loadConfig() {
        InputStream inputStream = null;
        try {
            // 首先尝试从资源目录加载
            inputStream = getClass().getClassLoader().getResourceAsStream("config/application.properties");
            
            // 如果资源目录中没有找到，则尝试从文件系统加载
            if (inputStream == null) {
                inputStream = new FileInputStream("resources/config/application.properties");
            }
            
            if (inputStream != null) {
                // 使用InputStreamReader并指定UTF-8编码，确保正确读取中文
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                properties.load(reader);
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("无法加载配置文件: " + e.getMessage());
            // 使用默认配置继续运行
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 获取属性值
     * @param key 属性键
     * @return String 属性值，如果不存在则返回null
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * 获取属性值，如果不存在则返回默认值
     * @param key 属性键
     * @param defaultValue 默认值
     * @return String 属性值，如果不存在则返回默认值
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * 获取整数属性值
     * @param key 属性键
     * @param defaultValue 默认值
     * @return int 属性值，如果不存在或无法解析则返回默认值
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * 获取布尔属性值
     * @param key 属性键
     * @param defaultValue 默认值
     * @return boolean 属性值，如果不存在则返回默认值
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        
        return Boolean.parseBoolean(value);
    }
} 