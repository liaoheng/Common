package com.github.liaoheng.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.util.UIUtils;

/**
 * 点击右侧图片显示密码
 *
 * @author liaoheng
 * @version 2017-07-05 15:42
 */
public class CURightClickVisibleContentEditText extends CURightClickEditText {

    private Drawable mShowImage;
    private Drawable mHideImage;

    public CURightClickVisibleContentEditText(Context context) {
        super(context);
        init(null);
    }

    public CURightClickVisibleContentEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CURightClickVisibleContentEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = null;
            try {
                a = getContext().obtainStyledAttributes(attrs, R.styleable.CURightClickVisibleContentEditText);
                mShowImage = a.getDrawable(R.styleable.CURightClickVisibleContentEditText_showImage);
                mHideImage = a.getDrawable(R.styleable.CURightClickVisibleContentEditText_hideImage);
            } finally {
                if (mShowImage == null) {
                    mShowImage = ContextCompat.getDrawable(getContext(), R.drawable.lcu_ic_remove_red_eye_black_24dp);
                }
                if (mHideImage == null) {
                    mHideImage = ContextCompat.getDrawable(getContext(), R.drawable.lcu_ic_remove_red_eye_black_24dp);
                }
                if (a != null) {
                    a.recycle();
                }
            }
        }

        selectDrawable(false);

        setRightClickListener(new RightClickListener() {
            @Override
            public void onClick(Drawable right, boolean select) {
                selectDrawable(select);
            }
        });
    }

    private void selectDrawable(boolean select) {
        Drawable left = getCompoundDrawables()[0];
        if (select) {
            setCompoundDrawablesWithIntrinsicBounds(
                    left, null,
                    mShowImage, null);
            UIUtils.showOrHintPassword(true, this);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(
                    left, null,
                    mHideImage, null);
            UIUtils.showOrHintPassword(false, this);
        }
    }
}
