package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.liaoheng.common.ui.R;

/**
 * 点击缩放按钮
 *
 * @author liaoheng
 * @date 2023-02-10 10:09
 */
public class ClickScaleButton extends androidx.appcompat.widget.AppCompatButton {
    public ClickScaleButtonHelper mScaleButtonHelper;

    public ClickScaleButton(Context context) {
        this(context, null);
    }

    public ClickScaleButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.buttonStyle);
    }

    public ClickScaleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleButtonHelper = new ClickScaleButtonHelper();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClickScaleButton);
            try {
                mScaleButtonHelper.setScaleXTo(
                        a.getFloat(R.styleable.ClickScaleButton_scaleXTo, mScaleButtonHelper.mScaleXTo));
                mScaleButtonHelper.setScaleYTo(
                        a.getFloat(R.styleable.ClickScaleButton_scaleYTo, mScaleButtonHelper.mScaleYTo));
                mScaleButtonHelper.setDuration(
                        a.getInt(R.styleable.ClickScaleButton_duration, mScaleButtonHelper.mDuration));
            } finally {
                a.recycle();
            }
        }
    }

    /**
     * 缩放长度
     *
     * @param scaleXTo 比例，1~0
     */
    public void setScaleXTo(float scaleXTo) {
        mScaleButtonHelper.setScaleXTo(scaleXTo);
    }

    /**
     * 缩放高度
     *
     * @param scaleYTo 比例，1~0
     */
    public void setScaleYTo(float scaleYTo) {
        mScaleButtonHelper.setScaleYTo(scaleYTo);
    }

    /**
     * 缩放动画时长
     *
     * @param duration 毫秒
     */
    public void setDuration(int duration) {
        mScaleButtonHelper.setDuration(duration);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isClickable()) {
            return super.onTouchEvent(event);
        }
        if (mScaleButtonHelper == null) {
            return super.onTouchEvent(event);
        }
        if (mScaleButtonHelper.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mScaleButtonHelper != null) {
            mScaleButtonHelper.onAttachedToWindow(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mScaleButtonHelper != null) {
            mScaleButtonHelper.onDetachedFromWindow();
        }
        super.onDetachedFromWindow();
    }
}
