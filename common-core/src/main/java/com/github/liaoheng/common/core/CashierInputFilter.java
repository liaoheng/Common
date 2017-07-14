package com.github.liaoheng.common.core;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import com.github.liaoheng.common.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参考http://blog.csdn.net/shineflowers/article/details/50846268
 *
 * @author liaoheng
 * @version 2017-07-03 16:02
 *          过滤用户输入只能为金额格式
 */
public class CashierInputFilter implements InputFilter {
    private Pattern mPattern = Pattern.compile("([0-9]|\\.)*");

    //输入的最大金额
    private int MAX_VALUE = 999;
    //小数点后的位数
    private int POINTER_LENGTH = 2;

    private final String POINTER = ".";

    public CashierInputFilter() {
    }

    /**
     * @param max 输入的最大金额
     * @param pointerLength 小数点后的位数
     */
    public CashierInputFilter(int max, int pointerLength) {
        MAX_VALUE = max;
        POINTER_LENGTH = pointerLength;
    }

    /**
     * @param source 新输入的字符串
     * @param start 新输入的字符串起始下标
     * @param end 新输入的字符串终点下标
     * @param dest 输入之前文本框内容
     * @param dstart 新输入的在旧字符串中的起始下标
     * @param dend 新输入的在旧字符串中的终点下标
     * @return 输入内容
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceText = source.toString();
        String destText = dest.toString();

        //验证删除等按键
        if (TextUtils.isEmpty(sourceText)) {
            return "";
        }
        Matcher matcher = mPattern.matcher(source);

        //只能输入数据与小数点
        if (!matcher.matches() && !POINTER.equals(source)) {
            return "";
        }

        //输入数据与原始数据合成之后的数据
        String synthesis = StringUtils.replace(sourceText, destText, dstart, dend);
        //空不处理
        if (TextUtils.isEmpty(synthesis)) {
            return null;
        }

        //只输入.时默认添0
        if (POINTER.equals(synthesis)) {
            return "0.";
        }

        //有小数位时
        if (synthesis.contains(POINTER)) {
            if (POINTER.equals(sourceText)) {  //只允许输入一个小数点
                if (destText.contains(POINTER)) {
                    return "";
                }
            }

            //验证小数点精度，保证小数点后只能输入两位
            int index = synthesis.indexOf(POINTER);
            int length = synthesis.length() - index;

            if (length > POINTER_LENGTH + 1) {
                return dest.subSequence(dstart, dend);
            }
        }

        //验证输入金额的大小
        double synthesisN = Double.parseDouble(synthesis);
        if (synthesisN > MAX_VALUE) {
            return dest.subSequence(dstart, dend);
        } else if (synthesisN == 0 && synthesis.length() > 1 && !POINTER.equals(sourceText)) {//只允许输入一个0
            return "";
        }

        return null;
    }
}
