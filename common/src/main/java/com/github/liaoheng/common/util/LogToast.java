package com.github.liaoheng.common.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;

/**
 * @author liaoheng
 * @version 2016-01-31 16:34
 */
public class LogToast implements L.ILogToast {

    @Override
    public void showLog(Context context,String hint){
        UIUtils.showToast(context, hint);
    }

    @Override
    public void e(String TAG, @NonNull Context context, String userHint, String sysHint,
                  @NonNull Throwable e) {
            L.e(TAG, e, sysHint);
        if (TextUtils.isEmpty(userHint)) {
            userHint =e.getMessage();
        }
        showLog(context, userHint);
    }

    @Override
    public void e(String TAG, @NonNull Context context, String userHint, @NonNull Throwable e) {
        e(TAG, context, userHint, null, e);
    }

    @Override
    public void e(String TAG, @NonNull Context context, @NonNull Throwable e) {
        e(TAG, context, null, e);
    }

    @Override
    public void e(String TAG, @NonNull Context context, String userHint) {
        L.e(TAG, userHint);
        showLog(context, userHint);
    }

    @Override
    public void e(String TAG, @NonNull Context context, @StringRes int userHint) {
        e(TAG, context, context.getResources().getString(userHint));
    }


    @Override
    public void eu(String TAG, @NonNull Context context, @NonNull Throwable e) {
        L.e(TAG, e);
        UIUtils.showToastUI(context, e.getMessage());
    }

    @Override
    public void eu(String TAG, @NonNull Context context, String userHint, @NonNull Throwable e) {
        L.e(TAG, e);
        UIUtils.showToastUI(context, userHint);
    }
}
