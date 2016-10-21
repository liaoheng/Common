package com.github.liaoheng.common.ui.base;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.github.liaoheng.common.ui.core.ToolBarHelper;
import com.github.liaoheng.common.util.UIUtils;

/**
 * 基础通用Activity
 * @author liaoheng
 */
public class CUBaseActivity extends AppCompatActivity {

    protected final String TAG = getActivity().getClass().getSimpleName();
    private ToolBarHelper toolBarUtils;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            back();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ToolBarHelper getToolBarUtils() {
        return toolBarUtils;
    }

    public Toolbar getToolbar() {
        return toolBarUtils.getToolbar();
    }

    public void setMTitle(CharSequence title) {
        if (toolBarUtils.getToolbarTitle() != null && title != null) {
            toolBarUtils.getToolbarTitle().setText(title);
        }
    }

    public CharSequence getMTitle() {
        if (toolBarUtils.getToolbarTitle() == null) {
            return "";
        }
        return toolBarUtils.getToolbarTitle().getText().toString();
    }

    protected void back() {
        getActivity().finish();
    }

    protected void initToolBar() {
        toolBarUtils = initToolBar((AppCompatActivity) getActivity());
    }

    protected ToolBarHelper initToolBar(AppCompatActivity activity) {
        if (activity == null) {
            return null;
        }
        return ToolBarHelper.with(activity);
    }

    protected void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public View inflate(@LayoutRes int resource) {
        return UIUtils.inflate(getActivity(), resource);
    }

    public View inflate(@LayoutRes int resource, ViewGroup root) {
        return UIUtils.inflate(getActivity(), resource, root);
    }

    public View inflate(@LayoutRes int resource, ViewGroup root, boolean attachToRoot) {
        return UIUtils.inflate(getActivity(), resource, root, attachToRoot);
    }

    public Activity getActivity() {
        return this;
    }

    public ActionBar getBaseActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            throw new AndroidRuntimeException("getSupportActionBar is null");
        }
        return actionBar;
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (toolBarUtils == null) {
            return;
        }
        if (!TextUtils.isEmpty(getMTitle()) || TextUtils.isEmpty(title)) {
            return;
        }
        if (UIUtils.isVisible(toolBarUtils.getToolbarTitle())) {
            setMTitle(title.toString());
        }
    }
}
