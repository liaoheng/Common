package com.github.liaoheng.common.plus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.github.liaoheng.common.plus.R;
import com.github.liaoheng.common.plus.core.BackPressedListener;

/**
 * @author liaoheng
 * @version 2016-03-30 11:26
 */
public class WebViewActivity extends CPBaseActivity {

    public static void start(Context context, String url) {
        start(context, url, false);
    }

    public static void start(Context context, String url, boolean htmlTitle) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("htmlTitle", htmlTitle);
        context.startActivity(intent);
    }

    private BackPressedListener mBackPressedListener;

    public void setBackPressedListener(BackPressedListener backPressedListener) {
        this.mBackPressedListener = backPressedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lcp_layout_fragment);
        String url = getIntent().getStringExtra("url");
        boolean htmlTitle = getIntent().getBooleanExtra("htmlTitle", false);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.lcp_fragment_view, WebViewFragment.newInstance(url, htmlTitle))
                .commit();
    }

    @Override public void onBackPressed() {
        if (mBackPressedListener != null && !mBackPressedListener.backPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
