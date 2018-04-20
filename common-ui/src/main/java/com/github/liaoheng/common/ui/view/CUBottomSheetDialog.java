package com.github.liaoheng.common.ui.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;

import com.github.liaoheng.common.adapter.core.HandleView;
import com.github.liaoheng.common.util.DisplayUtils;

import java.lang.reflect.Field;

/**
 * 底部卡片对话框
 *
 * @author liaoheng
 * @version 2016-10-08 16:23
 */
public class CUBottomSheetDialog extends BottomSheetDialog {
    private BottomSheetBehavior mBehavior;

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
        if (getBehavior() == null) {
            return;
        }
        float v = DisplayUtils.dp2px(getContext(), peekHeight);
        getBehavior().setPeekHeight((int) v);
    }

    public void setContentView(@LayoutRes int layoutResId, HandleView handleView) {
        setContentView(layoutResId);
        if (handleView != null) {
            handleView.handle(getWindow().getDecorView());
        }
    }

    /**
     * after setContentView()
     *
     * @see BottomSheetBehavior
     */
    public BottomSheetBehavior getBehavior() {
        if (mBehavior == null) {
            try {
                Field field = getClass().getSuperclass().getDeclaredField("mBehavior");
                field.setAccessible(true);
                mBehavior = (BottomSheetBehavior) field.get(this);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return mBehavior;
    }
}
