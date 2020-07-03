package com.github.liaoheng.common.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符工具
 *
 * @author liaoheng
 * @author <a href="http://jodd.org" target="_blank">jodd</a>
 * @version $Id: StringUtils.java, v 0.1 2014年5月14日 上午9:31:58 Administrator Exp $
 */
public class StringUtils {

    /**
     * 过滤EMOJI表情
     */
    public static InputFilter[] emojiFilter() {
        InputFilter[] inputFilters = new InputFilter[] {
                new InputFilter() {
                    Pattern emoji = Pattern.compile(
                            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                            int dend) {
                        Matcher emojiMatcher = emoji.matcher(source);
                        if (emojiMatcher.find()) {
                            return "";
                        }
                        return source;
                    }
                }
        };
        return inputFilters;
    }

    /**
     * 判断字符是否为ASCII格式
     */
    public static boolean isAllASCII(String input) {
        boolean isASCII = true;
        for (int i = 0; i < input.length(); i++) {
            int c = input.charAt(i);
            if (c > 0x7F) {
                isASCII = false;
                break;
            }
        }
        return isASCII;
    }

    /**
     * 判断字符是否为中文
     */
    // GENERAL_PUNCTUATION 判断中文的“号
    // CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
    // HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否中文
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在原字符串相应位置中插入新的字符串
     *
     * @param source         需要插入的新字符串
     * @param oldString      原來的字符串
     * @param insertionStart 插入開始的下標
     * @param insertionEnd   插入結束的下標
     */
    public static String replace(String source, String oldString, int insertionStart, int insertionEnd) {
        if (TextUtils.isEmpty(source)) {
            return oldString;
        }
        int oldLength = oldString.length();  //新字符串長度
        int insertionLength = insertionEnd - insertionStart; //老字符串替換的長度
        return oldString.substring(0, insertionStart) +
                source +
                oldString.substring(insertionStart + insertionLength, oldLength);
    }

    /**
     * Returns a new string that is a substring of this string. The substring
     * begins at the specified <code>fromIndex</code> and extends to the character
     * at index <code>toIndex - 1</code>. However, index values can be negative,
     * and then the real index will be calculated from the strings end. This
     * allows to specify, e.g. <code>substring(1,-1)</code> to cut one character
     * from both ends of the string. If <code>fromIndex</code> is negative
     * and <code>toIndex</code> is 0, it will return last characters of the string.
     * Also, this method will never throw an exception if index is out of range.
     */
    public static String substring(String string, int fromIndex, int toIndex) {
        int len = string.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;

            if (toIndex == 0) {
                toIndex = len;
            }
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
        }

        // safe net

        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > len) {
            toIndex = len;
        }
        if (fromIndex >= toIndex) {
            return null;
        }

        return string.substring(fromIndex, toIndex);
    }

    /**
     * Splits a string in several parts (tokens) that are separated by delimiter.
     * Delimiter is <b>always</b> surrounded by two strings! If there is no
     * content between two delimiters, null string will be returned for that
     * token. Therefore, the length of the returned array will always be:
     * #delimiters + 1.
     * <p>
     * Method is much, much faster then regexp <code>String.split()</code>,
     * and a bit faster then <code>StringTokenizer</code>.
     *
     * @param src       string to split
     * @param delimiter split delimiter
     * @return array of split strings
     */
    public static String[] split(String src, String delimiter) {
        int maxparts = (src.length() / delimiter.length()) + 2;        // one more for the last
        int[] positions = new int[maxparts];
        int dellen = delimiter.length();

        int i, j = 0;
        int count = 0;
        positions[0] = -dellen;
        while ((i = src.indexOf(delimiter, j)) != -1) {
            count++;
            positions[count] = i;
            j = i + dellen;
        }
        count++;
        positions[count] = src.length();

        String[] result = new String[count];

        for (i = 0; i < count; i++) {
            result[i] = src.substring(positions[i] + dellen, positions[i + 1]);
        }
        return result;
    }

}
