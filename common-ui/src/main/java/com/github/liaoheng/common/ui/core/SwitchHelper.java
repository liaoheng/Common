package com.github.liaoheng.common.ui.core;

import android.widget.CompoundButton;

/**
 * @author liaoheng
 * @date 2021-09-26 12:48
 */
public class SwitchHelper implements SwitchCheckable {

    private final SwitchCheckable mCheckable;
    private final boolean mEnableAsync;
    private boolean mAsyncSelected;
    private boolean mSelected;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    public SwitchHelper(SwitchCheckable checkable, boolean enableAsync) {
        mCheckable = checkable;
        mEnableAsync = enableAsync;
    }

    @Override
    public void setChecked(boolean checked) {
        setSelected(checked);
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(null, checked);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        if (mEnableAsync) {
            mAsyncSelected = selected;
            return;
        }
        mCheckable.updateSelected(selected);
    }

    @Override
    public void updateSelected(boolean selected) {
        mSelected = selected;
    }

    @Override
    public boolean isAsyncSelected() {
        return mAsyncSelected;
    }

    @Override
    public boolean isSelected() {
        return mSelected;
    }

    @Override
    public boolean isChecked() {
        return mCheckable.isSelected();
    }

    @Override
    public void toggle() {
        setChecked(!mCheckable.isSelected());
    }

    @Override
    public boolean performClick() {
        toggle();
        return true;
    }

    @Override
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

}
