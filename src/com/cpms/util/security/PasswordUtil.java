package com.cpms.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 密码工具类
 * 提供密码加密和随机密码生成功能
 */
public class PasswordUtil {
    
    /**
     * 使用MD5加密密码
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        if (password == null || password.isEmpty()) {
            System.out.println("加密失败：密码为空");
            return null;
        }
        
        try {
            // 创建MD5消息摘要对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // 计算摘要
            byte[] digest = md.digest(password.getBytes());
            
            // 将字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            
            String result = sb.toString();
            System.out.println("密码 [" + password + "] 加密结果: " + result);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 验证密码是否匹配
     * @param inputPassword 输入的密码
     * @param storedPassword 存储的加密密码
     * @return 是否匹配
     */
    public static boolean verifyPassword(String inputPassword, String storedPassword) {
        if (inputPassword == null || storedPassword == null) {
            System.out.println("验证失败：输入密码或存储密码为空");
            return false;
        }
        
        // 方法1：直接比较（明文密码）
        if (inputPassword.equals(storedPassword)) {
            System.out.println("明文密码匹配成功");
            return true;
        }
        
        // 方法2：MD5加密比较
        String encryptedInput = encryptPassword(inputPassword);
        boolean result = encryptedInput != null && encryptedInput.equals(storedPassword);
        System.out.println("MD5密码验证结果: " + result);
        
        return result;
    }
    
    /**
     * 生成指定长度的随机密码
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        if (length <= 0) {
            return "";
        }
        
        // 定义密码字符集
        String upperChars = "ABCDEFGHJKLMNPQRSTUVWXYZ"; // 排除容易混淆的字符I和O
        String lowerChars = "abcdefghijkmnpqrstuvwxyz"; // 排除容易混淆的字符l和o
        String numbers = "23456789"; // 排除容易混淆的字符0和1
        String specialChars = "!@#$%^&*"; // 特殊字符
        
        String allChars = upperChars + lowerChars + numbers + specialChars;
        
        // 创建随机数生成器
        Random random = new Random();
        
        // 生成随机密码
        StringBuilder password = new StringBuilder();
        
        // 确保密码包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符
        password.append(upperChars.charAt(random.nextInt(upperChars.length())));
        password.append(lowerChars.charAt(random.nextInt(lowerChars.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        
        // 生成剩余的随机字符
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // 打乱密码字符顺序
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int j = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
} 