package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.ui.core.SwitchCheckable;
import com.github.liaoheng.common.ui.core.SwitchHelper;

import androidx.appcompat.widget.AppCompatImageButton;

/**
 * Toggle  imageButton
 *
 * @author liaoheng
 * @version 2016-11-4 13:53
 */
public class ToggleImageButton extends AppCompatImageButton implements SwitchCheckable {
    private Drawable mNormalDrawable;
    private Drawable mSelectedDrawable;
    private SwitchHelper mSwitchHelper;

    public ToggleImageButton(Context context) {
        super(context);
    }

    public ToggleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ToggleImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToggleImageButton);
        mNormalDrawable = getDrawable();
        mSelectedDrawable = a.getDrawable(R.styleable.ToggleImageButton_selectedDrawableRes);
        boolean isSelected = a.getBoolean(R.styleable.ToggleImageButton_isSelected, false);
        updateSelected(isSelected);
        boolean enableAsync = a.getBoolean(R.styleable.ToggleImageButton_enableAsync, false);
        a.recycle();
        mSwitchHelper = new SwitchHelper(null, enableAsync);
    }

    @Override
    public void setSelected(boolean selected) {
        mSwitchHelper.setSelected(selected);
    }

    @Override
    public void updateSelected(boolean selected) {
        super.setSelected(selected);
        if (mSelectedDrawable == null) {
            return;
        }
        if (selected) {
            setImageDrawable(mSelectedDrawable);
        } else {
            setImageDrawable(mNormalDrawable);
        }
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
    public void setChecked(boolean checked) {
        mSwitchHelper.toggle();
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

    @Override
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        mSwitchHelper.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
