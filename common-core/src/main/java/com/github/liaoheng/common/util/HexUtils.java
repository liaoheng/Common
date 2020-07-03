package com.github.liaoheng.common.util;

import android.text.TextUtils;

/**
 * @author liaoheng
 * @version 2020-07-03 15:25
 */
public class HexUtils {
    /**
     * 字节转十六进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex.toUpperCase();
    }

    /**
     * 字节转十六进制
     *
     * @param bytes 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte... bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder hex = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            hex.append(byteToHex(bytes[i]));
        }
        return hex.toString();
    }

    /**
     * 字节转十六进制
     *
     * @param bytes 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(String interval, byte... bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder hex = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            hex.append(byteToHex(bytes[i])).append(interval);
        }
        if (!TextUtils.isEmpty(interval)) {
            hex.deleteCharAt(hex.length() - 1);
        }
        return hex.toString();
    }

    public static int hexToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static String intToHex(int n, int strLength) {
        String hex = Integer.toHexString(n);
        if (hex.length() < strLength) {
            StringBuilder p = new StringBuilder();
            for (int i = 0; i < strLength - hex.length(); i++) {
                p.append("0");
            }
            p.append(hex);
            return p.toString();
        }
        return hex.toUpperCase();
    }

    public static byte hexToByte(String hex) {
        return (byte) hexToInt(hex);
    }

    public static byte hexToByte(String hex, int start) {
        String h = StringUtils.substring(hex, start * 2, start * 2 + 2);
        return hexToByte(h);
    }

    /**
     * @param length hex字符串转成byte的长度
     */
    public static byte[] hexToBytes(String hex, int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = hexToByte(hex, i);
        }
        return bytes;
    }

    public static int bytesToHexInt(byte... data) {
        return hexToInt(bytesToHex(data));
    }

    public static int minusSafeHex(int hex) {
        if (hex <= 0) {
            return 65535;
        } else if (hex >= 65535) {
            return 0;
        }
        return hex - 1;
    }

    public static byte getXor(byte[] data) {
        byte temp = data[0];
        for (int i = 1; i < data.length; i++) {
            temp ^= data[i];
        }
        return temp;
    }
}
