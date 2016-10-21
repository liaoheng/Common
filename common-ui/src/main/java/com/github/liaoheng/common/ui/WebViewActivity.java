package com.github.liaoheng.common.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.github.liaoheng.common.core.BackPressedListener;
import com.github.liaoheng.common.ui.base.CUBaseActivity;

/**
 * @author liaoheng
 * @version 2016-03-30 11:26
 */
public class WebViewActivity extends CUBaseActivity {

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
        setContentView(R.layout.lcu_layout_web_activity);
        String url = getIntent().getStringExtra("url");
        boolean htmlTitle = getIntent().getBooleanExtra("htmlTitle", false);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.lcu_layout_web_activity_layout, WebViewFragment.newInstance(url, htmlTitle))
                .commit();
    }

    @Override public void onBackPressed() {
        if (mBackPressedListener != null && !mBackPressedListener.backPressed(null)) {
            return;
        }
        super.onBackPressed();
    }
}
