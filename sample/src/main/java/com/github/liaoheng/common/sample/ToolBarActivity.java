package com.github.liaoheng.common.sample;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.liaoheng.common.ui.base.CUBaseActivity;

import androidx.annotation.Nullable;

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

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        //    Window window = getWindow();
        //    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //    window.setStatusBarColor(Color.TRANSPARENT);
        //
        //    int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        //    systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        //    window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        //}

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
