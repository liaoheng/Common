package com.github.liaoheng.common.plus.core;

import android.widget.EditText;
import com.github.liaoheng.common.plus.view.CPInputDialog;
import com.github.liaoheng.common.util.UIUtils;

/**
 * 文字输入对话框点击回调
 * @author liaoheng
 * @version 2016-08-31 10:59
 */
public interface CPInputDialogClickListener {
    void onYes(CPInputDialog dialog, EditText editText, String text);

    void onCancel(CPInputDialog dialog, String text);

    void onFinish(CPInputDialog dialog);


    class EmptyCPInputDialogClickListener implements CPInputDialogClickListener {

        @Override public void onYes(CPInputDialog dialog, EditText editText, String text) {

        }

        @Override public void onCancel(CPInputDialog dialog, String text) {
            UIUtils.dismissDialog(dialog);
        }

        @Override public void onFinish(CPInputDialog dialog) {

        }
    }

}
