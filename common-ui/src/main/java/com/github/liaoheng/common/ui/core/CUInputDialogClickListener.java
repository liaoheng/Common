package com.github.liaoheng.common.ui.core;

import android.widget.EditText;
import com.github.liaoheng.common.ui.view.CUInputDialog;
import com.github.liaoheng.common.util.UIUtils;

/**
 * 文字输入对话框点击回调
 * @author liaoheng
 * @version 2016-08-31 10:59
 */
public interface CUInputDialogClickListener {
    void onYes(CUInputDialog dialog, EditText editText, String text);

    void onCancel(CUInputDialog dialog, String text);

    void onFinish(CUInputDialog dialog);


    class EmptyCUInputDialogClickListener implements CUInputDialogClickListener {

        @Override public void onYes(CUInputDialog dialog, EditText editText, String text) {

        }

        @Override public void onCancel(CUInputDialog dialog, String text) {
            UIUtils.dismissDialog(dialog);
        }

        @Override public void onFinish(CUInputDialog dialog) {

        }
    }

}
