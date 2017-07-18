package com.github.liaoheng.common.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.liaoheng.common.core.BackPressedListener;
import com.github.liaoheng.common.ui.base.CUBaseActivity;

/**
 * Simple WebView Activity
 *
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
        initActionBar();
        getBaseActionBar().setHomeAsUpIndicator(R.drawable.lcu_ic_close_white_24dp);

        String url = getIntent().getStringExtra("url");
        boolean htmlTitle = getIntent().getBooleanExtra("htmlTitle", false);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.lcu_layout_web_activity_layout, WebViewFragment.newInstance(url, htmlTitle, true))
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressedListener != null && !mBackPressedListener.backPressed(null)) {
            return;
        }
        super.onBackPressed();
    }
}
