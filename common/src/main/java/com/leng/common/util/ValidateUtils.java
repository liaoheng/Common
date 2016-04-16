package com.leng.common.util;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 验证工具
 *
 * @author liaoheng
 */
public class ValidateUtils {
    /**
     * 数据为空
     *
     * @param key
     * @param hint
     * @throws SystemException
     */
    public static String isEmpty(String key, String hint) throws SystemException {
        isEmpty(key, new SystemException(hint));
        return key;
    }

    /**
     * 数据为空
     *
     * @param key
     * @param hint
     * @throws SystemException
     */
    public static String isEmpty(String key, SystemException hint) throws SystemException {
        if (StringUtils.isEmpty(key)) {
            throw hint;
        }
        return key;
    }

    public static void isNumber(String s, String hint) throws SystemException {
        Pattern pattern = Pattern.compile("([0-9]+[.][0-9]+)||([0-9]*)");
        if (!pattern.matcher(s).matches()) {
            throw new SystemException(hint);
        }
    }

    public static Integer setInteger(String s, String hint) throws SystemException {
        isEmpty(s, hint);
        isNumber(s, hint);
        return Integer.parseInt(s);
    }

    public static Double setDouble(String s, String hint) throws SystemException {
        isEmpty(s, hint);
        isNumber(s, hint);
        return Double.parseDouble(s);
    }

    public static void judgeLength(String s, int length, String hint) throws SystemException {
        if (s.length() > length) {
            throw new SystemException(hint);
        }
    }

    /**
     * 对像为空
     *
     * @param <T>
     * @param key
     * @param hint
     * @throws SystemException
     */
    public static <T> T isNull(T key, String hint) throws SystemException {
        return isNull(key, new SystemException(hint));
    }

    /**
     * 对像为空
     *
     * @param <T>
     * @param key
     * @param hint
     * @throws SystemException
     */
    public static <T> T isNull(T key, SystemException hint) throws SystemException {
        if (null == key) {
            throw hint;
        }
        return key;
    }

    /**
     * 对像为空
     *
     * @param <T>
     * @param key
     */
    public static <T> boolean isNull(T key) {
        if (null == key) {
            return true;
        }
        return false;
    }

    /**
     * 列表数据为空
     *
     * @param list
     */
    public static Boolean isItemEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * 列表数据为空
     *
     * @param list
     * @throws SystemException
     */
    public static void isItemEmpty(List<?> list, String hint) throws SystemException {
        if (isItemEmpty(list)) {
            throw new SystemException(hint);
        }
    }
}
