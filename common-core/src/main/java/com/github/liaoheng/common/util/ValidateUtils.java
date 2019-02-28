package com.github.liaoheng.common.util;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.webkit.URLUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 验证工具
 *
 * @author liaoheng
 */
public final class ValidateUtils {

    /**
     * 是否为网络地址
     */
    public static boolean isWebUrl(Uri url) {
        return isWebUrl(url.toString());
    }

    /**
     * 是否为网络地址
     */
    public static boolean isWebUrl(String uri) {
        if (!isUrl(uri)) {
            return false;
        }
        return Patterns.WEB_URL.matcher(uri).matches();
    }

    public static boolean isUrl(String url) {
        return URLUtil.isValidUrl(url);
    }

    /**
     * 数据为空
     */
    public static String isEmpty(String key, String hint) throws SystemException {
        isEmpty(key, new SystemDataException(hint));
        return key;
    }

    /**
     * 数据为空
     */
    public static String isEmpty(String key, SystemException hint) throws SystemException {
        if (TextUtils.isEmpty(key)) {
            throw hint;
        }
        return key;
    }

    /**
     * 判断邮箱是否合法
     *
     * @see <a href='http://blog.csdn.net/gao_chun/article/details/39580363'>csdn</a>
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        return p.matcher(email).matches();
    }

    public static void isEmail(String email, String hint) throws SystemException {
        if (!isEmail(email)) {
            throw new SystemDataException(hint);
        }
    }

    public static boolean isNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        Pattern pattern = Pattern.compile("([0-9]+[.][0-9]+)||([0-9]*)");
        return pattern.matcher(number).matches();
    }

    public static void isNumber(String number, String hint) throws SystemException {
        if (!isNumber(number)) {
            throw new SystemException(hint);
        }
    }

    public static Integer setInteger(String s, String hint) throws SystemException {
        isNumber(s, hint);
        return Integer.parseInt(s);
    }

    public static Double setDouble(String s, String hint) throws SystemException {
        isNumber(s, hint);
        return Double.parseDouble(s);
    }

    public static boolean greaterLength(String s, int length) {
        return s.length() > length;
    }

    public static boolean lessLength(String s, int length) {
        return s.length() < length;
    }

    public static void greaterLength(String s, int length, String hint) throws SystemException {
        if (greaterLength(s, length)) {
            throw new SystemDataException(hint);
        }
    }

    public static void lessLength(String s, int length, String hint) throws SystemException {
        if (lessLength(s, length)) {
            throw new SystemDataException(hint);
        }
    }

    /**
     * 对像为空
     */
    public static <T> T isNull(T key, String hint) throws SystemException {
        return isNull(key, new SystemDataException(hint));
    }

    /**
     * 对像为空
     */
    public static <T> T isNull(T key, SystemException hint) throws SystemException {
        if (isNull(key)) {
            throw hint;
        }
        return key;
    }

    public static boolean isNull(Object key) {
        return null == key;
    }

    /**
     * Object...为空
     */
    public static boolean isNullObjectList(Object... key) {
        if (isNull(key)) {
            return true;
        }
        return key.length == 0;
    }

    /**
     * 列表数据为空
     */
    public static Boolean isItemEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * 列表数据为空
     */
    public static void isItemEmpty(List<?> list, String hint) throws SystemException {
        if (isItemEmpty(list)) {
            throw new SystemDataException(hint);
        }
    }
}
