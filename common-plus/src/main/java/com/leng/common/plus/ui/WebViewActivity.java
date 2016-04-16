package com.leng.common.plus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.leng.common.plus.R;

/**
 * @author liaoheng
 * @version 2016-03-30 11:26
 */
public class WebViewActivity extends CPBaseActivity {

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lcp_layout_fragment);
        String url = getIntent().getStringExtra("url");

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.lcp_fragment_view, WebViewFragment.newInstance(url)).commit();
    }
}
