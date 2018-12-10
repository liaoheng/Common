package com.github.liaoheng.common.ui.core;

import android.util.AndroidRuntimeException;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.util.UIUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * may use   {@link R.layout#lcu_view_toolbar_dark} || {@link R.layout#lcu_view_toolbar_light}
 * @author liaoheng
 * @version 2015年9月22日
 */
public class ToolBarHelper {
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private View     mToolbarRight;
    private View     mToolbarLeft;

    public static ToolBarHelper custom(@NonNull AppCompatActivity activity) {
        Toolbar toolbar = UIUtils.findViewById(activity, R.id.lcu_toolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null) {
            throw new AndroidRuntimeException("SupportActionBar is null");
        }
        actionBar.setDisplayShowTitleEnabled(false);
        return new ToolBarHelper(toolbar);
    }

    public static ToolBarHelper with(@NonNull AppCompatActivity activity) {
        Toolbar toolbar = UIUtils.findViewById(activity, R.id.lcu_toolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null) {
            throw new AndroidRuntimeException("SupportActionBar is null");
        }
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        return new ToolBarHelper(toolbar);
    }

    public static ToolBarHelper with(@NonNull View view) {
        return new ToolBarHelper((Toolbar) UIUtils.findViewById(view, R.id.lcu_toolbar));
    }

    public ToolBarHelper(Toolbar toolbar) {
        if (toolbar == null) {
            throw new IllegalArgumentException("Toolbar is null");
        }
        mToolbar = toolbar;
        mToolbarTitle = (TextView) toolbar.findViewById(R.id.lcu_toolbar_title);
        mToolbarRight = toolbar.findViewById(R.id.lcu_toolbar_right);
        mToolbarLeft = toolbar.findViewById(R.id.lcu_toolbar_left);
    }

    public void toggleVisibilityToolbarTitle() {
        UIUtils.toggleVisibility(mToolbarTitle);
    }

    public void toggleVisibilityToolbarRight() {
        UIUtils.toggleVisibility(mToolbarRight);
    }

    public void toggleVisibilityToolbarLeft() {
        UIUtils.toggleVisibility(mToolbarLeft);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    /**
     * def {@link ImageButton}
     */
    public View getToolbarRight() {
        return mToolbarRight;
    }

    public ImageButton getToolbarRightDef() {
        return (ImageButton) mToolbarRight;
    }

    /**
     * def {@link ImageButton}
     */
    public View getToolbarLeft() {
        return mToolbarLeft;
    }

    public ImageButton getToolbarLeftDef() {
        return (ImageButton) mToolbarLeft;
    }
}
