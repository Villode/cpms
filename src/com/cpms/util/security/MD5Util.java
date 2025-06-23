package com.cpms.util.security;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 */
public class MD5Util {
    
    /**
     * MD5加密
     * @param str 需要加密的字符串
     * @param charset 字符编码
     * @return 加密后的字符串
     */
    public static String MD5Encode(String str, String charset) {
        String resultString = null;
        try {
            resultString = str;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charset == null || "".equals(charset)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charset)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
    
    /**
     * 将字节数组转换为十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }
    
    /**
     * 将字节转换为十六进制字符串
     * @param b 字节
     * @return 十六进制字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    
    /**
     * 十六进制字符
     */
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
} 