package com.github.liaoheng.common.plus.core;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.liaoheng.common.plus.R;
import com.github.liaoheng.common.plus.view.WebViewLayout;
import com.github.liaoheng.common.util.OperateCallback;
import com.github.liaoheng.common.util.UIUtils;

/**
 *  may use {@link R.layout#lcp_layout_web}
 * @author liaoheng
 * @version 2015-11-30 14:04
 */
public class WebHelper {
    private WebViewLayout mWebView;

    public static WebHelper with(@NonNull View view) {
        return new WebHelper(view);
    }

    private WebHelper(@NonNull View view) {
        mWebView = UIUtils.findViewById(view, R.id.lcp_web_view);
        if (mWebView == null) {
            throw new IllegalArgumentException("WebView is null");
        }
    }

    public void loadUrl(String url) {
        getWebView().loadUrl(url);
    }

    public void refresh() {
        loadUrl(getWebView().getWebView().getUrl());
    }

    public WebViewLayout getWebView() {
        return mWebView;
    }

    public void onPause() {
        getWebView().pause();
    }

    public void onResume() {
        getWebView().resume();
    }

    public void onDestroy() {
        getWebView().destroy();
    }

    public void onBackPressed(Activity activity) {
        if (getWebView().backPressed()) {
            activity.finish();
        }
    }

    public void onBackPressed(OperateCallback<Void> callback) {
        if (getWebView().backPressed()) {
            callback.onSuccess(null);
        }
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        getWebView().onActivityResult(requestCode, resultCode, intent);
    }
}