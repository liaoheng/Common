package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatImageButton;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.ui.core.SwitchCheckable;
import com.github.liaoheng.common.ui.core.SwitchHelper;

/**
 * Toggle  imageButton
 *
 * @author liaoheng
 * @version 2016-11-4 13:53
 */
public class ToggleImageButton extends AppCompatImageButton implements SwitchCheckable {
    public ToggleImageButton(Context context) {
        super(context);
    }

    public ToggleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ToggleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private Drawable mNormalDrawable;
    private Drawable mSelectedDrawable;
    private SwitchHelper mSwitchHelper;

    public void setSelectedDrawable(Drawable selectedDrawable) {
        mSelectedDrawable = selectedDrawable;
        updateSelected(isChecked());
    }

    public void setNormalDrawable(Drawable normalDrawable) {
        mNormalDrawable = normalDrawable;
        updateSelected(isChecked());
    }

    @Override
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        mSwitchHelper.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToggleImageButton);
        mNormalDrawable = typedArray.getDrawable(R.styleable.ToggleImageButton_normalDrawableRes);
        if (mNormalDrawable == null) {
            mNormalDrawable = getDrawable();
        }
        mSelectedDrawable = typedArray.getDrawable(R.styleable.ToggleImageButton_selectedDrawableRes);
        boolean isSelected = typedArray.getBoolean(R.styleable.ToggleImageButton_isSelected, false);
        updateSelected(isSelected);
        boolean enableAsync = typedArray.getBoolean(R.styleable.ToggleImageButton_enableAsync, false);
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
    public void toggle() {
        mSwitchHelper.toggle();
    }

    @Override
    public boolean performClick() {
        mSwitchHelper.performClick();
        return super.performClick();
    }
}
