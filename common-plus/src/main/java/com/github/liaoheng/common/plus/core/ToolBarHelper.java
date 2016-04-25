package com.github.liaoheng.common.plus.core;

import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.liaoheng.common.plus.R;
import com.github.liaoheng.common.plus.adapter.SpinnerAdapter;
import com.github.liaoheng.common.plus.model.SpinnerItem;
import com.github.liaoheng.common.plus.view.ToggleImageButton;
import com.github.liaoheng.common.util.Callback;
import com.github.liaoheng.common.util.UIUtils;

/**
 * may use   {@link R.layout#lcp_view_toolbar_dark} || {@link R.layout#lcp_view_toolbar_light}
 * @author liaoheng
 * @version 2015年9月22日
 */
public class ToolBarHelper {
    private Toolbar  toolbar;
    private TextView toolbarTitle;
    private Spinner  toolbarSpinner;
    private View     toolbarToggle;
    private View     toolbarRight;
    private View     toolbarLeft;

    public static ToolBarHelper with(@NonNull AppCompatActivity activity) {
        Toolbar toolbar = UIUtils.findViewById(activity, R.id.lcp_toolbar);
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
        return new ToolBarHelper((Toolbar) UIUtils.findViewById(view, R.id.lcp_toolbar));
    }

    public ToolBarHelper(Toolbar toolbar) {
        if (toolbar == null) {
            throw new IllegalArgumentException("Toolbar is null");
        }
        this.toolbar = toolbar;
        toolbarTitle = (TextView) toolbar.findViewById(R.id.lcp_toolbar_title);
        toolbarSpinner = (Spinner) toolbar.findViewById(R.id.lcp_toolbar_spinner);
        toolbarToggle = toolbar.findViewById(R.id.lcp_toolbar_toggle);
        toolbarRight = toolbar.findViewById(R.id.lcp_toolbar_right);
        toolbarLeft = toolbar.findViewById(R.id.lcp_toolbar_left);
    }

    public void setToggle(final Callback<ToggleImageButton> callback) {
        toolbarToggle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (callback != null) {
                    ToggleImageButton buttonView = ((ToggleImageButton) v);
                    callback.onSuccess(buttonView);
                }
            }
        });
    }

    public SpinnerAdapter setSpinnerAdapter(Context context, List<SpinnerItem> list) {
        SpinnerAdapter adapter = new SpinnerAdapter(context, list);
        toolbarSpinner.setAdapter(adapter);
        return adapter;
    }

    public void toggleVisibilityToolbarSpinner() {
        UIUtils.toggleVisibility(toolbarSpinner);
    }

    public void toggleVisibilityToolbarTitle() {
        UIUtils.toggleVisibility(toolbarTitle);
    }

    public void toggleVisibilityToolbarLike() {
        UIUtils.toggleVisibility(toolbarToggle);
    }

    public void toggleVisibilityToolbarRight() {
        UIUtils.toggleVisibility(toolbarRight);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public TextView getToolbarTitle() {
        return toolbarTitle;
    }

    public Spinner getToolbarSpinner() {
        return toolbarSpinner;
    }

    /**
     * def {@link ToggleImageButton}
     * @return
     */
    public View getToolbarToggle() {
        return toolbarToggle;
    }

    /**
     * def {@link ImageButton}
     * @return
     */
    public View getToolbarRight() {
        return toolbarRight;
    }

    /**
     * def {@link ImageButton}
     * @return
     */
    public View getToolbarLeft() {
        return toolbarLeft;
    }

}
