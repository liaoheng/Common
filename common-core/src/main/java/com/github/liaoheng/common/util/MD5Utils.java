package com.github.liaoheng.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5
 */
public class MD5Utils {
    private static final int STREAM_BUFFER_LENGTH = 1024;

    /**
     * 得到32位的16进制字符串
     * @param byteArray
     * @return
     */
    public static String byteToHex32(byte[] byteArray) {
        return HexUtils.with().encodeHexString(byteArray);
    }

    /**
     * 得到16位的16进制字符串
     * @param byteArray
     * @return
     */
    public static String byteToHex16(byte[] byteArray) {
        return byteToHex32(byteArray).substring(8, 24);
    }

    /**
     * 得到MD5加密16进制的字符串
     * @param txt
     * @return 32位16进制字符串
     */
    public static String md5Hex(String txt) {
        return byteToHex32(md5(txt));
    }

    public static MessageDigest getDigest(final String algorithm) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm);
    }

    public static byte[] md5(String txt) {
        return md5(txt.getBytes());
    }

    public static byte[] md5(byte[] bytes) {
        try {
            MessageDigest digest = getDigest("MD5");
            digest.update(bytes);
            return digest.digest();
        } catch (NoSuchAlgorithmException ignored) {
        }
        return null;
    }

    public static byte[] md5(InputStream is) throws NoSuchAlgorithmException, IOException {
        return updateDigest(getDigest("MD5"), is).digest();
    }

    public static MessageDigest updateDigest(final MessageDigest digest, final InputStream data)
            throws IOException {
        final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

        while (read > -1) {
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }

        return digest;
    }
}
