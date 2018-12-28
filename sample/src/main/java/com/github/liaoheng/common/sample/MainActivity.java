package com.github.liaoheng.common.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
        ButterKnife.bind(this);
    }

    @OnClick(R.id.open_single_input_dialog)
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

    @OnClick(R.id.open_multi_input_dialog)
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

    @OnClick(R.id.open_bottom_sheet_dialog)
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
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.dialog_close)
        void close() {
            cuBottomSheetDialog.dismiss();
        }
    }

    @OnClick(R.id.open_toolbar)
    void opeToolBar() {
        UIUtils.startActivity(this, ToolBarActivity.class);
    }

    @OnClick(R.id.load_image)
    void loadImage() {
        UIUtils.startActivity(this, GankListActivity.class);
    }

    @OnClick(R.id.open_web_view)
    void openWebView() {
        WebViewActivity.start(this, "https://www.github.com", true);
    }

}
