package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatButton;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.util.ResourceUtils;

/**
 * @author liaoheng
 * @date 2022-01-27 15:02
 */
public class SelectButton extends AppCompatButton {
    public SelectButton(Context context) {
        super(context);
    }

    public SelectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SelectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private CharSequence mNormalText;
    private CharSequence mSelectedText;
    private float mNormalTextSize;
    private float mSelectedTextSize;
    private int mNormalTextColor;
    private int mSelectedTextColor;

    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectButton);
        mNormalText = typedArray.getString(R.styleable.SelectButton_normalText);
        if (mNormalText == null) {
            mNormalText = getText();
        }
        mSelectedText = typedArray.getString(R.styleable.SelectButton_selectedText);
        mNormalTextColor = typedArray.getColor(R.styleable.SelectButton_normalTextColor, -1);
        if (mNormalTextColor == -1) {
            mNormalTextColor = getCurrentTextColor();
        }
        mSelectedTextColor = typedArray.getColor(R.styleable.SelectButton_selectedTextColor,
                ResourceUtils.getAttrColor(context, R.attr.colorAccent));
        mNormalTextSize = typedArray.getDimension(R.styleable.SelectButton_selectedTextSize, -1);
        if (mNormalTextSize == -1) {
            mNormalTextSize = getTextSize();
        }
        mSelectedTextSize = typedArray.getDimension(R.styleable.SelectButton_selectedTextSize, 0);
        boolean isSelected = typedArray.getBoolean(R.styleable.SelectButton_isSelected, false);
        setSelected(isSelected);
        typedArray.recycle();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (mSelectedTextColor > 1) {
            if (selected) {
                setTextColor(mSelectedTextColor);
            } else {
                setTextColor(mNormalTextColor);
            }
        }
        if (mSelectedTextSize > 0) {
            if (selected) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectedTextSize);
            } else {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize);
            }
        }
        if (!TextUtils.isEmpty(mSelectedText)) {
            if (selected) {
                setText(mSelectedText);
            } else {
                setText(mNormalText);
            }
        }
    }
}
