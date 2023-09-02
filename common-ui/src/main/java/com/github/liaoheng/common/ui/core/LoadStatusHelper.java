package com.github.liaoheng.common.ui.core;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.github.liaoheng.common.ui.R;

import java.util.function.Consumer;

/**
 * 加载/错误布局帮助类,默认资源文件：{@link R.layout#lcu_view_load_status}
 *
 * @author liaoheng
 * @date 2021-09-13 13:40
 */
public class LoadStatusHelper {
    private LoadStatusHelper(View view) {
        initLoadView(view);

        mErrorLayoutStub = view.findViewById(R.id.lcu_view_load_error);
        if (mErrorLayoutStub == null) {
            initErrorView(view);
        }

        mEmptyLayoutStub = view.findViewById(R.id.lcu_view_load_empty);
        if (mEmptyLayoutStub == null) {
            initEmptyView(view);
        }
    }

    private LoadStatusHelper(View loadingLayout, View loadingView, TextView loadingMsgView,
            View errorLayout, TextView errorMsgView, Button errorRetryView, View emptyLayout,
            TextView emptyMsgView) {
        mLoadingLayout = loadingLayout;
        mLoadingView = loadingView;
        mLoadingMsgView = loadingMsgView;
        mErrorLayout = errorLayout;
        mErrorMsgView = errorMsgView;
        mErrorRetryView = errorRetryView;
        mEmptyLayout = emptyLayout;
        mEmptyMsgView = emptyMsgView;
    }

    public void initLoadView(View view) {
        mLoadingLayout = view.findViewById(R.id.lcu_load_status_loading_layout);
        mLoadingView = view.findViewById(R.id.lcu_load_status_loading);
        mLoadingMsgView = view.findViewById(R.id.lcu_load_status_loading_msg);
    }

    public void initErrorView(View view) {
        mErrorLayout = view.findViewById(R.id.lcu_load_status_error_layout);
        mErrorMsgView = view.findViewById(R.id.lcu_load_status_error_msg);
        mErrorRetryView = view.findViewById(R.id.lcu_load_status_error_retry);
    }

    public void initEmptyView(View view) {
        mEmptyLayout = view.findViewById(R.id.lcu_load_status_empty_layout);
        mEmptyMsgView = view.findViewById(R.id.lcu_load_status_empty_msg);
    }

    public static LoadStatusHelper with(View view) {
        return new LoadStatusHelper(view);
    }

    public static LoadStatusHelper withLoadView(View loadingLayout, View loadingView) {
        return new LoadStatusHelper(loadingLayout, loadingView, null, null, null, null, null, null);
    }

    public static LoadStatusHelper withLoadErrorView(View loadingLayout, View loadingView, View errorLayout) {
        return new LoadStatusHelper(loadingLayout, loadingView, null, errorLayout, null, null, null,
                null);
    }

    private View mLoadingLayout;
    private View mLoadingView;
    private TextView mLoadingMsgView;
    private ViewStub mErrorLayoutStub;
    private View mErrorLayout;
    private TextView mErrorMsgView;
    private Button mErrorRetryView;
    private ViewStub mEmptyLayoutStub;
    private View mEmptyLayout;
    private TextView mEmptyMsgView;

    /**
     * 加载中/停止加载
     *
     * @param isLoading 状态
     */
    public void isLoading(boolean isLoading) {
        isLoading("", isLoading);
    }

    /**
     * 加载中/停止加载
     *
     * @param msg       提示文字
     * @param isLoading 状态
     */
    public void isLoading(String msg, boolean isLoading) {
        int v = isLoading ? View.VISIBLE : View.GONE;
        if (mLoadingLayout != null) {
            mLoadingLayout.setVisibility(v);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(v);
        }
        if (mLoadingMsgView != null) {
            if (!TextUtils.isEmpty(msg)) {
                mLoadingMsgView.setText(msg);
                mLoadingMsgView.setVisibility(View.VISIBLE);
            }
        }
        if (mErrorLayout != null && isLoading) {
            mErrorLayout.setVisibility(View.GONE);
        }
        if (mEmptyLayout != null && isLoading) {
            mEmptyLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 错误
     */
    public void error() {
        error("", "", null);
    }

    /**
     * 错误
     *
     * @param msg 错误提示文字
     */
    public void error(String msg) {
        error(msg, "", null);
    }

    /**
     * 错误
     *
     * @param retryCallback 重试回掉
     */
    public void error(Consumer<View> retryCallback) {
        error("", "", retryCallback);
    }

    /**
     * 错误
     *
     * @param msg           错误提示文字
     * @param retryMsg      重试提示文字
     * @param retryCallback 重试回掉
     */
    public void error(String msg, String retryMsg, Consumer<View> retryCallback) {
        hide();
        if (mErrorLayoutStub != null && mErrorMsgView == null) {
            initErrorView(mErrorLayoutStub.inflate());
        }
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(View.VISIBLE);
        }
        if (mErrorMsgView != null) {
            if (!TextUtils.isEmpty(msg)) {
                mErrorMsgView.setText(msg);
            }
        }
        if (mErrorRetryView != null) {
            if (retryCallback == null) {
                mErrorRetryView.setVisibility(View.GONE);
                return;
            } else {
                mErrorRetryView.setOnClickListener(retryCallback::accept);
            }
            mErrorRetryView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(retryMsg)) {
                mErrorRetryView.setText(retryMsg);
            }
        }
    }

    /**
     * 隐藏
     */
    public void hide() {
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(View.GONE);
        }
        if (mLoadingLayout != null) {
            mLoadingLayout.setVisibility(View.GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mEmptyLayout != null) {
            mEmptyLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 提示
     *
     * @param msg 提示文字
     */
    public void hint(String msg) {
        error(msg, null, null);
    }

    /**
     * 空
     *
     * @param msg 提示文字
     */
    public void empty(String msg) {
        hide();
        if (mEmptyLayoutStub != null && mEmptyMsgView == null) {
            initEmptyView(mEmptyLayoutStub.inflate());
        }
        if (mEmptyLayout != null) {
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
        if (mEmptyMsgView != null) {
            if (!TextUtils.isEmpty(msg)) {
                mEmptyMsgView.setText(msg);
            }
        }
    }

    /**
     * 空
     */
    public void empty() {
        empty("");
    }

}
