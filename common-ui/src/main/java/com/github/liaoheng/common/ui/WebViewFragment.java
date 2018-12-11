package com.github.liaoheng.common.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.github.liaoheng.common.core.BackPressedListener;
import com.github.liaoheng.common.ui.base.CULazyFragment;
import com.github.liaoheng.common.ui.core.WebHelper;
import com.github.liaoheng.common.ui.core.WebViewMenuHelper;
import com.github.liaoheng.common.ui.widget.WebViewLayout;

/**
 * Simple WebView Fragment
 *
 * @author liaoheng
 * @version 2016-03-30 11:26
 */
public class WebViewFragment extends CULazyFragment implements BackPressedListener {

    public static Fragment newInstance(String url) {
        return newInstance(url, false, false);
    }

    public static Fragment newInstance(String url, boolean htmlTitle, boolean enableMenu) {
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putBoolean("htmlTitle", htmlTitle);
        args.putBoolean("enableMenu", enableMenu);
        return setBundle(new WebViewFragment(), args);
    }

    private WebHelper mWebHelper;

    class WebChromeClient extends WebViewLayout.CPWebChromeClient {

        public WebChromeClient(Object context) {
            super(context);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            getActivity().setTitle(title);
        }
    }

    private WebViewMenuHelper mMenuHelper;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        setContentView(R.layout.lcu_layout_web);

        mWebHelper = WebHelper.with(getContentView());
        mMenuHelper = WebViewMenuHelper.with();

        String url = getArguments().getString("url");
        boolean htmlTitle = getArguments().getBoolean("htmlTitle");
        if (htmlTitle) {
            getActivity().setTitle("");
            mWebHelper.getWebView().setWebChromeClient(new WebChromeClient(this));
        } else {
            mWebHelper.getWebView().getWebChromeClient().setContext(this);
        }
        boolean enableMenu = getArguments().getBoolean("enableMenu");
        setHasOptionsMenu(enableMenu);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mMenuHelper.onWebOptionsItemSelected(getContext(), item,
                mWebHelper.getWebView().getWebView().getUrl()) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenuHelper.onWebCreateOptionsMenu(inflater, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WebViewActivity) {
            ((WebViewActivity) context).setBackPressedListener(this);
        }
    }

    @Override
    public void onDetach() {
        if (getContext() instanceof WebViewActivity) {
            ((WebViewActivity) getContext()).setBackPressedListener(null);
        }
        super.onDetach();
    }

    @Override
    public boolean backPressed(Object o) {
        return mWebHelper.getWebView().backPressed();
    }
}
