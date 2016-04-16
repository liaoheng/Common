package com.leng.common.plus.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import com.leng.common.plus.R;
import com.leng.common.util.UIUtils;

/**
 * may use {@link R.layout#lcp_view_progress}
 * @author liaoheng
 * @version 2015年9月8日
 */
public class ProgressHelper {

    private ProgressBar mProgressBar;

    public static ProgressHelper with(@NonNull View view) {
        return new ProgressHelper((ProgressBar) UIUtils.findViewById(view, R.id.lcp_progress));
    }

    public static ProgressHelper with(@NonNull Activity activity) {
        return new ProgressHelper((ProgressBar) UIUtils.findViewById(activity, R.id.lcp_progress));
    }

    public ProgressHelper(ProgressBar progressBar) {
        if (progressBar == null) {
            throw new IllegalArgumentException("ProgressBar is null");
        }
        mProgressBar = progressBar;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public void visible() {
        UIUtils.viewVisible(mProgressBar);
    }

    public void inVisible() {
        UIUtils.viewInVisible(mProgressBar);
    }

    public void gone() {
        UIUtils.viewGone(mProgressBar);
    }
}
