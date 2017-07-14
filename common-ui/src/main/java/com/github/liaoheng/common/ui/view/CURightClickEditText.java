package com.github.liaoheng.common.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 右边图片可以点击的 EditText
 *
 * @author liaoheng
 * @version 2017-07-05 15:34
 */
public class CURightClickEditText extends AppCompatEditText {
    public CURightClickEditText(Context context) {
        super(context);
    }

    public CURightClickEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CURightClickEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private RightClickListener mRightClickListener;
    private boolean mSelect;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
        Drawable drawable = getCompoundDrawables()[2];
        //如果右边没有图片，不再处理
        if (drawable == null) {
            return super.onTouchEvent(event);
        }
        //如果不是按下事件，不再处理
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return super.onTouchEvent(event);
        }
        if (event.getX() > getWidth()
                - getPaddingRight()
                - drawable.getIntrinsicWidth()) {
            toggle();
            if (mRightClickListener != null) {
                mRightClickListener.onClick(drawable, mSelect);
            }
        }
        return super.onTouchEvent(event);
    }

    public void toggle() {
        mSelect = !mSelect;
    }

    public boolean isSelect() {
        return mSelect;
    }

    public RightClickListener getRightClickListener() {
        return mRightClickListener;
    }

    public void setRightClickListener(RightClickListener mRightClickListener) {
        this.mRightClickListener = mRightClickListener;
    }

    public interface RightClickListener {
        void onClick(Drawable right, boolean select);
    }
}
