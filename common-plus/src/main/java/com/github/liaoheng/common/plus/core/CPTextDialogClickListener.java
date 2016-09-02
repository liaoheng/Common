package com.github.liaoheng.common.plus.core;

import android.widget.EditText;
import com.github.liaoheng.common.plus.view.CPTextDialog;

/**
 * 文字输入对话框点击回调
 * @author liaoheng
 * @version 2016-08-31 10:59
 */
public interface CPTextDialogClickListener {
    void onYes(CPTextDialog dialog, EditText editText, String text);

    void onCancel(CPTextDialog dialog, String text);

    void onFinish(CPTextDialog dialog);


    class EmptyCPTextDialogClickListener implements CPTextDialogClickListener {

        @Override public void onYes(CPTextDialog dialog, EditText editText, String text) {

        }

        @Override public void onCancel(CPTextDialog dialog, String text) {

        }

        @Override public void onFinish(CPTextDialog dialog) {

        }
    }

}
