package com.github.liaoheng.common.util;

import android.text.TextUtils;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 验证工具
 *
 * @author liaoheng
 * @author https://github.com/artjimlop/chernobyl
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

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalArgumentException if {@code expression} is true
     */
    public static void checkArgument(boolean expression) {
        if (expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code expression} is true
     */
    public static void checkArgument(boolean expression, Object errorMessage) {
        if (expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param errorMessageTemplate a template for the exception message should the check fail. The
     *     message is formed by replacing each {@code %s} placeholder in the template with an
     *     argument. These are matched by position - the first {@code %s} gets {@code
     *     errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in
     *     square braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs the arguments to be substituted into the message template. Arguments
     *     are converted to Object.
     * @throws IllegalArgumentException if {@code expression} is true
     */
    public static void checkArgument(boolean expression, String errorMessageTemplate,
                                     Object... errorMessageArgs) {
        if (expression) {
            throw new IllegalArgumentException(StringUtils.format(errorMessageTemplate, errorMessageArgs));
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalStateException if {@code expression} is true
     */
    public static void checkState(boolean expression) {
        if (expression) {
            throw new IllegalStateException();
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(boolean expression, Object errorMessage) {
        if (expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param errorMessageTemplate a template for the exception message should the check fail. The
     *     message is formed by replacing each {@code %s} placeholder in the template with an
     *     argument. These are matched by position - the first {@code %s} gets {@code
     *     errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in
     *     square braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs the arguments to be substituted into the message template. Arguments
     *     are converted to strings using {@link String#valueOf(Object)}.
     * @throws IllegalStateException if {@code expression} is true
     * @throws NullPointerException if the check fails and either {@code errorMessageTemplate} or
     *     {@code errorMessageArgs} is null (don't let this happen)
     */
    public static void checkState(boolean expression, String errorMessageTemplate,
                                  Object... errorMessageArgs) {
        if (expression) {
            throw new IllegalStateException(StringUtils.format(errorMessageTemplate, errorMessageArgs));
        }
    }
}
