package com.github.liaoheng.common.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.liaoheng.common.sample.databinding.ActivityWebBinding;
import com.github.liaoheng.ui.WebViewFragment;
import com.github.liaoheng.ui.base.CUBaseActivity;
import com.github.liaoheng.util.UIUtils;

/**
 * @author liaoheng
 * @date 2025-11-12 11:12
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWebBinding viewBinding = ActivityWebBinding.inflate(getLayoutInflater());
        UIUtils.setStatusBarColor(this, viewBinding.getRoot(), R.color.colorPrimaryDark);
        setContentView(viewBinding.getRoot());
        initActionBar();
        getBaseActionBar().setHomeAsUpIndicator(com.github.liaoheng.ui.R.drawable.lcu_ic_close_white_24dp);

        String url = getIntent().getStringExtra("url");
        boolean htmlTitle = getIntent().getBooleanExtra("htmlTitle", false);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.web_layout, WebViewFragment.newInstance(url, htmlTitle, true))
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
}
