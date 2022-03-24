package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.ui.core.SwitchCheckable;
import com.github.liaoheng.common.ui.core.SwitchHelper;

/**
 * @author liaoheng
 * @version 2022-03-24 15:48:45
 */
public class ToggleButton extends SelectButton implements SwitchCheckable {
    public ToggleButton(Context context) {
        super(context);
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private SwitchHelper mSwitchHelper;

    @Override
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        mSwitchHelper.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToggleButton);
        boolean enableAsync = typedArray.getBoolean(R.styleable.ToggleButton_enableAsync, false);
        typedArray.recycle();
        mSwitchHelper = new SwitchHelper(this, enableAsync);
    }

    @Override
    public void setChecked(boolean checked) {
        mSwitchHelper.setChecked(checked);
    }

    @Override
    public void setSelected(boolean selected) {
        mSwitchHelper.setSelected(selected);
    }

    @Override
    public void updateSelected(boolean selected) {
        super.setSelected(selected);
    }

    @Override
    public boolean isAsyncSelected() {
        return mSwitchHelper.isAsyncSelected();
    }

    @Override
    public boolean isChecked() {
        return mSwitchHelper.isChecked();
    }

    @Override
    public void toggle() {
        mSwitchHelper.toggle();
    }

    @Override
    public boolean performClick() {
        mSwitchHelper.performClick();
        return super.performClick();
    }
}
