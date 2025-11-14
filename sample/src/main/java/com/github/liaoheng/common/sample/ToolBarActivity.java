package com.github.liaoheng.common.sample;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.Nullable;

import com.github.liaoheng.common.sample.databinding.ActivityToolbarBinding;
import com.github.liaoheng.ui.base.CUBaseActivity;
import com.github.liaoheng.util.UIUtils;

/**
 * @author liaoheng
 * @version 2017-01-22 11:45
 */
public class ToolBarActivity extends CUBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityToolbarBinding viewBinding = ActivityToolbarBinding.inflate(getLayoutInflater());
        UIUtils.setStatusBarColor(this, viewBinding.getRoot(), R.color.colorPrimaryDark);
        setContentView(viewBinding.getRoot());
        //initToolBar();
        initToolBarCustom();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getToolBarUtils().toggleVisibilityToolbarTitle();
        getToolBarUtils().toggleVisibilityToolbarRight();
        getToolBarUtils().toggleVisibilityToolbarLeft();

        getToolBarUtils().getToolbarLeft().setOnClickListener(v -> finish());

        getToolBarUtils().getToolbarLeftDef()
                .setImageResource(com.github.liaoheng.ui.R.drawable.lcu_ic_close_white_24dp);
        getToolBarUtils().getToolbarRightDef().setImageResource(com.github.liaoheng.ui.R.drawable.lcu_ic_drawer_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
