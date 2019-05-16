package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import com.github.liaoheng.common.adapter.core.HandleView;
import com.github.liaoheng.common.util.DisplayUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * 底部卡片对话框
 *
 * @author liaoheng
 * @version 2016-10-08 16:23
 */
public class CUBottomSheetDialog extends BottomSheetDialog {
    public CUBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public CUBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    protected CUBottomSheetDialog(@NonNull Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * dialog default  max height see : {@link BottomSheetBehavior#setPeekHeight(int)}
     *
     * @param peekHeight dip
     */
    public void setPeekHeight(int peekHeight) {
        float v = DisplayUtils.dp2px(getContext(), peekHeight);
        getBehavior().setPeekHeight((int) v);
    }

    public void setContentView(@LayoutRes int layoutResId, HandleView handleView) {
        setContentView(layoutResId);
        if (handleView != null) {
            handleView.handle(getWindow().getDecorView());
        }
    }
}
