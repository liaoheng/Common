package com.github.liaoheng.common.ui.util;

import android.content.Context;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.github.liaoheng.common.util.Callback5;

/**
 * @author liaoheng
 * @version 2020-07-03 17:27
 */
public class UIUtils {

    /**
     * 是否提示框
     */
    public static AlertDialog createYNAlertDialog(Context context, String message,
            final Callback5 call) {
        return createAlertDialog(context, message, call);
    }

    /**
     * 是否提示框
     */
    public static AlertDialog showYNAlertDialog(Context context, String message,
            final Callback5 call) {
        AlertDialog dialog = createYNAlertDialog(context, message, call);
        dialog.show();
        return dialog;
    }

    /**
     * 信息提示框
     */
    public static AlertDialog createInfoAlertDialog(Context context, String message,
            final Callback5 call) {
        return createAlertDialog(context, message, context.getString(com.github.liaoheng.common.R.string.lcm_ok),
                null, new Callback5() {
                    @Override
                    public void onAllow() {
                        call.onAllow();
                    }

                    @Override
                    public void onDeny() {
                    }
                });
    }

    /**
     * 信息提示框
     */
    public static AlertDialog showInfoAlertDialog(Context context, String message,
            final Callback5 call) {
        AlertDialog alb = createInfoAlertDialog(context, message, call);
        alb.show();
        return alb;
    }

    /**
     * 对话框
     */
    public static AlertDialog createAlertDialog(Context context, String message,
            final Callback5 call) {
        return createAlertDialog(context, message, context.getString(com.github.liaoheng.common.R.string.lcm_ok),
                context.getString(com.github.liaoheng.common.R.string.lcm_no), call);
    }

    /**
     * 对话框
     */
    public static AlertDialog showAlertDialog(Context context, String message,
            final Callback5 call) {
        AlertDialog alb = createAlertDialog(context, message, call);
        alb.show();
        return alb;
    }

    /**
     * 提示框
     */
    public static AlertDialog createAlertDialog(Context context, String message,
            String positiveButtonText,
            String negativeButtonText,
            Callback5 call) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message);
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, (dialog, which) -> call.onAllow());
        }
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, (dialog, which) -> call.onDeny());
        }
        return builder.create();
    }

}
