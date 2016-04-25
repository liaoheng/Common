package com.github.liaoheng.common.plus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.leng.common.plus.R;
import com.github.liaoheng.common.plus.core.WebHelper;

/**
 * @author liaoheng
 * @version 2016-03-30 11:26
 */
public class WebViewFragment extends CPLazyFragment {

    public static Fragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        return newInstance(new WebViewFragment(), args);
    }

    private WebHelper mWebHelper;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.lcp_layout_web);

        mWebHelper = WebHelper.with(getContentView());
        mWebHelper.getWebView().getWebChromeClient().setContext(this);

        String url = getArguments().getString("url");
        mWebHelper.loadUrl(url);
    }

    @Override
    protected void onResumeLazy() {
        mWebHelper.onResume();
        super.onResumeLazy();
    }

    @Override
    protected void onPauseLazy() {
        mWebHelper.onPause();
        super.onPauseLazy();
    }

    @Override
    protected void onDestroyViewLazy() {
        mWebHelper.onDestroy();
        super.onDestroyViewLazy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mWebHelper.onActivityResult(requestCode, resultCode, data);
    }
}
