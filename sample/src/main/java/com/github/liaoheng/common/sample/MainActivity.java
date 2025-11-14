package com.github.liaoheng.common.sample;

import android.os.Bundle;
import android.widget.EditText;

import com.github.liaoheng.common.sample.databinding.ActivityMainBinding;
import com.github.liaoheng.ui.base.CURxBaseActivity;
import com.github.liaoheng.ui.core.CUInputDialogClickListener;
import com.github.liaoheng.ui.widget.CUInputDialog;
import com.github.liaoheng.util.UIUtils;

public class MainActivity extends CURxBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        UIUtils.setStatusBarColor(this, viewBinding.getRoot(), R.color.colorPrimaryDark);
        setContentView(viewBinding.getRoot());
        viewBinding.openSingleInputDialog.setOnClickListener(v -> openSingleInputDialog());
        viewBinding.openMultiInputDialog.setOnClickListener(v -> openMultiInputDialog());
        viewBinding.openToolbar.setOnClickListener(v -> opeToolBar());
        viewBinding.loadImage.setOnClickListener(v -> loadImage());
        viewBinding.openWebView.setOnClickListener(v -> openWebView());
    }

    void openSingleInputDialog() {
        CUInputDialog.single(getActivity(), R.style.AppTheme_Dialog)
                .setMessage("Single Input")
                .setClickListener(new CUInputDialogClickListener.EmptyCUInputDialogClickListener() {
                    @Override
                    public void onYes(CUInputDialog dialog, EditText editText, String text) {
                        UIUtils.showToast(getApplicationContext(), text);
                    }
                })
                .show();
    }

    void openMultiInputDialog() {
        CUInputDialog.multi(getActivity(), R.style.AppTheme_Dialog)
                .setMessage("Multi Input")
                .setClickListener(new CUInputDialogClickListener.EmptyCUInputDialogClickListener() {
                    @Override
                    public void onYes(CUInputDialog dialog, EditText editText, String text) {
                        UIUtils.showToast(getApplicationContext(), text);
                    }
                })
                .show();
    }

    void opeToolBar() {
        UIUtils.startActivity(this, ToolBarActivity.class);
    }

    void loadImage() {
        UIUtils.startActivity(this, GankListActivity.class);
    }

    void openWebView() {
        WebViewActivity.start(this, "https://www.github.com", true);
    }

}
