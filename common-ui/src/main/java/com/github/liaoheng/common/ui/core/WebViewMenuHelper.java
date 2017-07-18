package com.github.liaoheng.common.ui.core;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.util.UIUtils;

/**
 * @author liaoheng
 * @version 2017-07-18 16:15
 */
public class WebViewMenuHelper {
    private WebViewMenuHelper() {
    }

    public static WebViewMenuHelper with() {
        return new WebViewMenuHelper();
    }

    public void onWebCreateOptionsMenu(MenuInflater inflater, Menu menu) {
        inflater.inflate(R.menu.lcu_menu_web, menu);
    }

    public boolean onWebOptionsItemSelected(Context context, MenuItem item, String url) {
        int itemId_ = item.getItemId();
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (itemId_ == R.id.lcu_menu_web_share) {
            Uri uri = Uri.parse(url);
            Intent share = new Intent(Intent.ACTION_SEND, uri);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.putExtra(Intent.EXTRA_TEXT, uri.toString());
            context.startActivity(Intent.createChooser(share, context.getString(R.string.lcu_share)));
            return true;
        } else if (itemId_ == R.id.lcu_menu_web_open_with_browser) {
            Uri uri = Uri.parse(url);
            Intent browser = new Intent(Intent.ACTION_VIEW, uri);
            browser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browser);
            return true;
        } else if (itemId_ == R.id.lcu_menu_web_copy_url) {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("url", url);
            clipboard.setPrimaryClip(clip);
            UIUtils.showToast(context.getApplicationContext(), context.getString(R.string.lcu_url_copied));
            return true;
        }
        return false;
    }
}
