package com.github.liaoheng.common.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageButton;
import com.github.liaoheng.common.core.OnCheckedChangeListener;
import com.github.liaoheng.common.ui.R;

/**
 *  Toggle  imageButton
 * @author liaoheng
 * @version 2016-11-4 13:53
 */
public class ToggleImageButton extends ImageButton implements Checkable {
    private OnCheckedChangeListener<ToggleImageButton> mOnCheckedChangeListener;
    private boolean                                    mAsync;
    private boolean                                    mAsyncSelect;
    private Drawable                                   mNormalDrawable;
    private Drawable                                   mSelectedDrawable;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) public ToggleImageButton(Context context,
                                                                      AttributeSet attrs,
                                                                      int defStyleAttr,
                                                                      int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.ToggleImageButton);
            this.mAsync = a.getBoolean(R.styleable.ToggleImageButton_async, false);
            mNormalDrawable = a.getDrawable(R.styleable.ToggleImageButton_normalDrawableRes);
            if (mNormalDrawable != null) {
                setImageDrawable(mNormalDrawable);
            }
            mSelectedDrawable = a.getDrawable(R.styleable.ToggleImageButton_selectedDrawableRes);
            boolean checked = a.getBoolean(R.styleable.ToggleImageButton_checked, false);
            setSelected(checked);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    /**
     *async时，得到上次操作的状态
     */
    public boolean isAsyncSelect() {
        return mAsyncSelect;
    }

    @Override public void setSelected(boolean selected) {
        if (mAsync) {
            mAsyncSelect = selected;
        }
        if (selected && mSelectedDrawable != null) {
            setImageDrawable(mSelectedDrawable);
        } else if (!selected && mNormalDrawable != null) {
            setImageDrawable(mNormalDrawable);
        }
        super.setSelected(selected);
    }

    @Override public boolean isChecked() {
        return isSelected();
    }

    /**
     * async时，操作状态会被记录但不会改变控件状态，需自行调用{@link #setSelected(boolean)}改变。
     * @param checked
     */
    @Override public void setChecked(boolean checked) {
        if (mAsync) {
            mAsyncSelect = checked;
        } else {
            setSelected(checked);
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, checked);
            }
        }
    }

    @Override public void toggle() {
        setChecked(!isChecked());
    }

    @Override public boolean performClick() {
        toggle();
        return super.performClick();
    }

    /**
     * 打开async
     */
    public void enableAsync() {
        this.mAsync = true;
    }

    /**
     * 关闭async
     */
    public void unableAsync() {
        this.mAsync = false;
    }

    public void setOnCheckedChangeListener(
            OnCheckedChangeListener<ToggleImageButton> onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }
}
