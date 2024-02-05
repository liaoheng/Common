package com.github.liaoheng.common.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.github.liaoheng.common.adapter.core.HandleView;
import com.github.liaoheng.common.ui.WebViewActivity;
import com.github.liaoheng.common.ui.base.CURxBaseActivity;
import com.github.liaoheng.common.ui.core.CUInputDialogClickListener;
import com.github.liaoheng.common.ui.widget.CUBottomSheetDialog;
import com.github.liaoheng.common.ui.widget.CUInputDialog;
import com.github.liaoheng.common.util.UIUtils;

public class MainActivity extends CURxBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.open_single_input_dialog).setOnClickListener(v -> openSingleInputDialog());
        findViewById(R.id.open_multi_input_dialog).setOnClickListener(v -> openMultiInputDialog());
        findViewById(R.id.open_bottom_sheet_dialog).setOnClickListener(v -> openBottomSheetDialog());
        findViewById(R.id.open_toolbar).setOnClickListener(v -> opeToolBar());
        findViewById(R.id.load_image).setOnClickListener(v -> loadImage());
        findViewById(R.id.open_web_view).setOnClickListener(v -> openWebView());
    }

    void openSingleInputDialog() {
        CUInputDialog.single(getActivity(), R.style.AppTheme_Dialog).setMessage("Single Input")
                .setClickListener(new CUInputDialogClickListener.EmptyCUInputDialogClickListener() {
                    @Override
                    public void onYes(CUInputDialog dialog, EditText editText,
                            String text) {
                        UIUtils.showToast(getApplicationContext(), text);
                    }
                }).show();
    }

    void openMultiInputDialog() {
        CUInputDialog.multi(getActivity(), R.style.AppTheme_Dialog).setMessage("Multi Input")
                .setClickListener(new CUInputDialogClickListener.EmptyCUInputDialogClickListener() {
                    @Override
                    public void onYes(CUInputDialog dialog, EditText editText,
                            String text) {
                        UIUtils.showToast(getApplicationContext(), text);
                    }
                }).show();
    }

    CUBottomSheetDialog cuBottomSheetDialog;

    void openBottomSheetDialog() {
        if (cuBottomSheetDialog == null) {
            cuBottomSheetDialog = new CUBottomSheetDialog(this);
            cuBottomSheetDialog.setContentView(R.layout.view_dialog, new BottomSheetDialogHandleView());
            cuBottomSheetDialog.setPeekHeight(400);
        }

        cuBottomSheetDialog.show();

    }

    class BottomSheetDialogHandleView extends HandleView.EmptyHandleView {
        @Override
        public void handle(View view) {
            view.findViewById(R.id.dialog_close).setOnClickListener(v -> close());
        }

        void close() {
            cuBottomSheetDialog.dismiss();
        }
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
