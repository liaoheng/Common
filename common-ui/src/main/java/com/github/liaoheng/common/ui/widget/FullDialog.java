package com.github.liaoheng.common.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * @author liaoheng
 * @date 2021-10-12 17:01
 */
public class FullDialog extends Dialog {
    private final Context mContext;

    public FullDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    public void show() {
        //https://stackoverflow.com/questions/22794049/how-do-i-maintain-the-immersive-mode-in-dialogs
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        getWindow().getDecorView()
                .setSystemUiVisibility(((Activity) mContext).getWindow().getDecorView().getSystemUiVisibility());

        super.show();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }
}
