package com.github.liaoheng.common.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;

/**
 * @author liaoheng
 * @version 2016-01-31 17:08
 */
public class LogSnack implements L.ILogSnack {
    @Override
    public void showLog(View view, String hint) {
        UIUtils.showSnack(view, hint);
    }

    @Override
    public void e(String TAG, @NonNull View view, String userHint, String sysHint,
                  @NonNull Throwable e) {
        L.e(TAG, e, sysHint);
        if (TextUtils.isEmpty(userHint)) {
            userHint = e.getMessage();
        }
        showLog(view, userHint);
    }

    @Override
    public void e(String TAG, @NonNull View view, String userHint, @NonNull Throwable e) {
        e(TAG, view, userHint, null, e);
    }

    @Override
    public void e(String TAG, @NonNull View view, @NonNull Throwable e) {
        e(TAG, view, null, e);
    }

    @Override
    public void e(String TAG, @NonNull View view, String userHint) {
        L.e(TAG, userHint);
        showLog(view, userHint);
    }

    @Override
    public void e(String TAG, @NonNull View view, @StringRes int userHint) {
        e(TAG, view, view.getContext().getString(userHint));
    }

    @Override
    public void e(String TAG, @NonNull Activity activity, String userHint, String sysHint,
                  @NonNull Throwable e) {
        e(TAG, activity.getWindow().getDecorView(), userHint, sysHint, e);
    }

    @Override
    public void e(String TAG, @NonNull Activity activity, String userHint, @NonNull Throwable e) {
        e(TAG, activity, userHint, null, e);
    }

    @Override
    public void e(String TAG, @NonNull Activity activity, @NonNull Throwable e) {
        e(TAG, activity, null, e);
    }

    @Override
    public void e(String TAG, @NonNull Activity activity, String userHint) {
        e(TAG, activity.getWindow().getDecorView(), userHint);
    }

    @Override
    public void e(String TAG, @NonNull Activity activity, @StringRes int userHint) {
        e(TAG, activity, activity.getString(userHint));
    }
}
