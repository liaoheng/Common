package com.github.liaoheng.common.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.webkit.WebView;
import com.github.liaoheng.common.core.BackPressedListener;
import com.github.liaoheng.common.ui.base.CPLazyFragment;
import com.github.liaoheng.common.ui.core.WebHelper;
import com.github.liaoheng.common.ui.view.WebViewLayout;

/**
 * @author liaoheng
 * @version 2016-03-30 11:26
 */
public class WebViewFragment extends CPLazyFragment implements BackPressedListener {

    public static Fragment newInstance(String url) {
        return newInstance(url, false);
    }

    public static Fragment newInstance(String url, boolean htmlTitle) {
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putBoolean("htmlTitle", htmlTitle);
        return newInstance(new WebViewFragment(), args);
    }

    private WebHelper mWebHelper;

    class WebChromeClient extends WebViewLayout.CPWebChromeClient {

        public WebChromeClient(Object context) {
            super(context);
        }

        @Override public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            getActivity().setTitle(title);
        }
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.lcu_layout_web);

        mWebHelper = WebHelper.with(getContentView());

        String url = getArguments().getString("url");
        boolean htmlTitle = getArguments().getBoolean("htmlTitle");
        if (htmlTitle) {
            getActivity().setTitle("");
            mWebHelper.getWebView().setWebChromeClient(new WebChromeClient(this));
        } else {
            mWebHelper.getWebView().getWebChromeClient().setContext(this);
        }

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

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WebViewActivity) {
            ((WebViewActivity) context).setBackPressedListener(this);
        }
    }

    @Override public void onDetach() {
        if (getContext() instanceof WebViewActivity) {
            ((WebViewActivity) getContext()).setBackPressedListener(null);
        }
        super.onDetach();
    }

    @Override public boolean backPressed(Object o) {
        return mWebHelper.getWebView().backPressed();
    }
}
