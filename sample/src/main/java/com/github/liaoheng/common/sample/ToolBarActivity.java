package com.github.liaoheng.common.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;

import com.github.liaoheng.common.ui.base.CUBaseActivity;

/**
 * @author liaoheng
 * @version 2017-01-22 11:45
 */
public class ToolBarActivity extends CUBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        //initToolBar();
        initToolBarCustom();

        getToolBarUtils().toggleVisibilityToolbarTitle();
        getToolBarUtils().toggleVisibilityToolbarRight();
        getToolBarUtils().toggleVisibilityToolbarLeft();

        getToolBarUtils().getToolbarLeftDef().setImageResource(R.drawable.lcu_ic_close_white_24dp);
        getToolBarUtils().getToolbarRightDef().setImageResource(R.drawable.lcu_ic_drawer_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
