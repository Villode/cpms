package com.cpms.util.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 简单的日志工具类
 * 提供统一的日志记录功能
 */
public class Logger {
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 日志级别
    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }
    
    private static Level currentLevel = Level.INFO;
    
    /**
     * 设置日志级别
     * @param level 日志级别
     */
    public static void setLevel(Level level) {
        currentLevel = level;
    }
    
    /**
     * 记录DEBUG级别日志
     * @param message 日志消息
     */
    public static void debug(String message) {
        log(Level.DEBUG, message, null);
    }
    
    /**
     * 记录INFO级别日志
     * @param message 日志消息
     */
    public static void info(String message) {
        log(Level.INFO, message, null);
    }
    
    /**
     * 记录WARN级别日志
     * @param message 日志消息
     */
    public static void warn(String message) {
        log(Level.WARN, message, null);
    }
    
    /**
     * 记录WARN级别日志（带异常）
     * @param message 日志消息
     * @param throwable 异常对象
     */
    public static void warn(String message, Throwable throwable) {
        log(Level.WARN, message, throwable);
    }
    
    /**
     * 记录ERROR级别日志
     * @param message 日志消息
     */
    public static void error(String message) {
        log(Level.ERROR, message, null);
    }
    
    /**
     * 记录ERROR级别日志（带异常）
     * @param message 日志消息
     * @param throwable 异常对象
     */
    public static void error(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }
    
    /**
     * 统一的日志记录方法
     * @param level 日志级别
     * @param message 日志消息
     * @param throwable 异常对象（可为null）
     */
    private static void log(Level level, String message, Throwable throwable) {
        if (level.ordinal() < currentLevel.ordinal()) {
            return;
        }
        
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level.name(), message);
        
        if (level == Level.ERROR || level == Level.WARN) {
            System.err.println(logMessage);
        } else {
            System.out.println(logMessage);
        }
        
        if (throwable != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            
            if (level == Level.ERROR || level == Level.WARN) {
                System.err.println(sw.toString());
            } else {
                System.out.println(sw.toString());
            }
        }
    }
    
    /**
     * 获取调用者信息
     * @return 调用者类名和方法名
     */
    private static String getCallerInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 3) {
            StackTraceElement caller = stackTrace[3];
            return caller.getClassName() + "." + caller.getMethodName();
        }
        return "Unknown";
    }
}