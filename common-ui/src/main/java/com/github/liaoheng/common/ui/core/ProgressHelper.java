package com.github.liaoheng.common.ui.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.ProgressBar;
import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.util.UIUtils;

/**
 * may use {@link R.layout#lcu_view_progress}
 * @author liaoheng
 * @version 2015年9月8日
 */
public class ProgressHelper {

    private ContentLoadingProgressBar mProgressBar;
    private View                      mBgView;

    public static ProgressHelper with(@NonNull View view) {
        View bgView = UIUtils.findViewById(view, R.id.lcu_progress_layout);
        return new ProgressHelper(bgView,
                (ContentLoadingProgressBar) UIUtils.findViewById(view, R.id.lcu_progress));
    }

    public static ProgressHelper with(@NonNull Activity activity) {
        View bgView = UIUtils.findViewById(activity, R.id.lcu_progress_layout);
        return new ProgressHelper(bgView,
                (ContentLoadingProgressBar) UIUtils.findViewById(activity, R.id.lcu_progress));
    }

    public ProgressHelper(ContentLoadingProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    private ProgressHelper(View bgView, ContentLoadingProgressBar progressBar) {
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
        mProgressBar.show();
    }

    public void hide() {
        mProgressBar.hide();
        if (mBgView != null) {
            UIUtils.viewGone(mBgView);
        }
    }
}
