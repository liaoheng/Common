package com.github.liaoheng.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatDialog;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import com.github.liaoheng.common.adapter.core.HandleView;
import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.util.DensityUtils;
import com.github.liaoheng.common.util.DisplayUtils;

/**
 * 底部卡片对话框 see : {@link BottomSheetDialog } 24.2.1
 * @author liaoheng
 * @version 2016-10-08 16:23
 */
public class CUBottomSheetDialog extends AppCompatDialog {

    private BottomSheetBehavior<FrameLayout> mBehavior;

    private boolean mCancelable             = true;
    private boolean mCanceledOnTouchOutside = true;
    private boolean mCanceledOnTouchOutsideSet;

    public CUBottomSheetDialog(@NonNull Context context) {
        this(context, R.style.LCU_BottomSheetDialog);
    }

    public CUBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, getThemeResId(context, theme));
        // We hide the title bar for any style configuration. Otherwise, there will be a gap
        // above the bottom sheet when it is expanded.
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected CUBottomSheetDialog(@NonNull Context context, boolean cancelable,
                                  OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mCancelable = cancelable;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //https://code.google.com/p/android/issues/detail?id=202691
        int screenHeight = DisplayUtils.getScreenHeight(getContext());
        int statusBarHeight = DisplayUtils.getStatusBarHeight(getContext());
        int dialogHeight = screenHeight - statusBarHeight;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }

    /**
     *   dialog default  max height see : {@link BottomSheetBehavior#setPeekHeight(int)}
     * @param peekHeight dip
     */
    public void setPeekHeight(int peekHeight) {
        float v = DensityUtils.dp2px(getContext(), peekHeight);
        mBehavior.setPeekHeight((int) v);
    }

    public void setContentView(@LayoutRes int layoutResId, HandleView handleView) {
        View view = wrapInBottomSheet(layoutResId, null, null);
        super.setContentView(view);
        handleView.handle(view);
    }

    @Override public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(wrapInBottomSheet(layoutResId, null, null));
    }

    @Override public void setContentView(View view) {
        super.setContentView(wrapInBottomSheet(0, view, null));
    }

    @Override public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(wrapInBottomSheet(0, view, params));
    }

    @Override public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        if (mCancelable != cancelable) {
            mCancelable = cancelable;
            if (mBehavior != null) {
                mBehavior.setHideable(cancelable);
            }
        }
    }

    @Override public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel && !mCancelable) {
            mCancelable = true;
        }
        mCanceledOnTouchOutside = cancel;
        mCanceledOnTouchOutsideSet = true;
    }

    private View wrapInBottomSheet(int layoutResId, View view, ViewGroup.LayoutParams params) {
        final CoordinatorLayout coordinator = (CoordinatorLayout) View
                .inflate(getContext(), android.support.design.R.layout.design_bottom_sheet_dialog,
                        null);
        if (layoutResId != 0 && view == null) {
            view = getLayoutInflater().inflate(layoutResId, coordinator, false);
        }
        FrameLayout bottomSheet = (FrameLayout) coordinator
                .findViewById(android.support.design.R.id.design_bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setBottomSheetCallback(mBottomSheetCallback);
        mBehavior.setHideable(mCancelable);
        if (params == null) {
            bottomSheet.addView(view);
        } else {
            bottomSheet.addView(view, params);
        }
        // We treat the CoordinatorLayout as outside the dialog though it is technically inside
        coordinator.findViewById(android.support.design.R.id.touch_outside)
                .setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        if (mCancelable && isShowing() && shouldWindowCloseOnTouchOutside()) {
                            cancel();
                        }
                    }
                });
        return coordinator;
    }

    boolean shouldWindowCloseOnTouchOutside() {
        if (!mCanceledOnTouchOutsideSet) {
            if (Build.VERSION.SDK_INT < 11) {
                mCanceledOnTouchOutside = true;
            } else {
                TypedArray a = getContext().obtainStyledAttributes(
                        new int[] { android.R.attr.windowCloseOnTouchOutside });
                mCanceledOnTouchOutside = a.getBoolean(0, true);
                a.recycle();
            }
            mCanceledOnTouchOutsideSet = true;
        }
        return mCanceledOnTouchOutside;
    }

    private static int getThemeResId(Context context, int themeId) {
        if (themeId == 0) {
            // If the provided theme is 0, then retrieve the dialogTheme from our theme
            TypedValue outValue = new TypedValue();
            if (context.getTheme()
                    .resolveAttribute(android.support.design.R.attr.bottomSheetDialogTheme,
                            outValue, true)) {
                themeId = outValue.resourceId;
            } else {
                // bottomSheetDialogTheme is not provided; we default to our light theme
                themeId = android.support.design.R.style.Theme_Design_Light_BottomSheetDialog;
            }
        }
        return themeId;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override public void onStateChanged(@NonNull View bottomSheet,
                                             @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override public void show() {
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        super.show();
    }
}
