package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.ui.core.SwitchCheckable;
import com.github.liaoheng.common.ui.core.SwitchHelper;
import com.github.liaoheng.common.util.ResourceUtils;

import androidx.appcompat.widget.AppCompatButton;

/**
 * @author liaoheng
 * @version 2015年9月22日
 */
public class ToggleTextButton extends AppCompatButton implements SwitchCheckable {
    private CharSequence mNormalText;
    private CharSequence mSelectedText;
    private int mNormalTextColor;
    private int mSelectedTextColor;
    private SwitchHelper mSwitchHelper;

    public ToggleTextButton(Context context) {
        super(context);
    }

    public ToggleTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ToggleTextButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToggleTextButton);
        mNormalText = getText();
        mSelectedText = a.getString(R.styleable.ToggleTextButton_selectedText);
        mNormalTextColor = getCurrentTextColor();
        mSelectedTextColor = a.getColor(R.styleable.ToggleTextButton_selectedTextColor,
                ResourceUtils.getAttrColor(context, R.attr.colorAccent));
        boolean isSelected = a.getBoolean(R.styleable.ToggleTextButton_isSelected, false);
        updateSelected(isSelected);
        boolean enableAsync = a.getBoolean(R.styleable.ToggleTextButton_enableAsync, false);
        a.recycle();
        mSwitchHelper = new SwitchHelper(this, enableAsync);
    }

    @Override
    public void setSelected(boolean selected) {
        mSwitchHelper.setSelected(selected);
    }

    @Override
    public void updateSelected(boolean selected) {
        super.setSelected(selected);
        if (mSelectedTextColor > 1) {
            if (selected) {
                setTextColor(mSelectedTextColor);
            } else {
                setTextColor(mNormalTextColor);
            }
        }
        if (TextUtils.isEmpty(mSelectedText)) {
            return;
        }
        if (selected) {
            setText(mSelectedText);
        } else {
            setText(mNormalText);
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
        mSwitchHelper.setChecked(checked);
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
