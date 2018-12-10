package com.github.liaoheng.common.ui.core;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.util.UIUtils;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;

/**
 * may use {@link R.layout#lcu_view_progress}
 *
 * @author liaoheng
 * @version 2015年9月8日
 */
public class ProgressHelper {

    private ProgressBar mProgressBar;
    private View mBgView;

    public static ProgressHelper with(@NonNull View view) {
        View bgView = UIUtils.findViewById(view, R.id.lcu_progress_layout);
        return new ProgressHelper(bgView,
                (ProgressBar) UIUtils.findViewById(view, R.id.lcu_progress));
    }

    public static ProgressHelper with(@NonNull Activity activity) {
        View bgView = UIUtils.findViewById(activity, R.id.lcu_progress_layout);
        return new ProgressHelper(bgView,
                (ProgressBar) UIUtils.findViewById(activity, R.id.lcu_progress));
    }

    public ProgressHelper(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    private ProgressHelper(View bgView, ProgressBar progressBar) {
        mBgView = bgView;
        mProgressBar = progressBar;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public void show() {
        if (mBgView != null) {
            UIUtils.viewVisible(mBgView);
        }
        if (mProgressBar instanceof ContentLoadingProgressBar) {
            ((ContentLoadingProgressBar) mProgressBar).show();
        } else {
            UIUtils.viewVisible(mProgressBar);
        }
    }

    public void hide() {
        if (mProgressBar instanceof ContentLoadingProgressBar) {
            ((ContentLoadingProgressBar) mProgressBar).hide();
        } else {
            UIUtils.viewGone(mProgressBar);
        }
        if (mBgView != null) {
            UIUtils.viewGone(mBgView);
        }
    }
}
