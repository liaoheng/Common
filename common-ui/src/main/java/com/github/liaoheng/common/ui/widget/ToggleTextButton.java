package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.github.liaoheng.common.core.OnCheckedChangeListener;
import com.github.liaoheng.common.ui.R;

/**
 * @author liaoheng
 * @version 2015年9月22日
 */
public class ToggleTextButton extends AppCompatButton implements Checkable {
    private OnCheckedChangeListener<ToggleTextButton> mOnCheckedChangeListener;
    private boolean                                   mAsync;
    private boolean                                   mAsyncSelect;
    private String                                    mNormalText;
    private String                                    mSelectedText;

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
        if (attrs == null) {
            return;
        }
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.ToggleTextButton);
            this.mAsync = a.getBoolean(R.styleable.ToggleTextButton_enableAsync, false);
            mNormalText = a.getString(R.styleable.ToggleTextButton_normalText);
            if (!TextUtils.isEmpty(mNormalText)) {
                setText(mNormalText);
            }
            mSelectedText = a.getString(R.styleable.ToggleTextButton_selectedText);
            boolean checked = a.getBoolean(R.styleable.ToggleTextButton_checked, false);
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
        if (selected && !TextUtils.isEmpty(mSelectedText)) {
            setText(mSelectedText);
        } else if (!selected && !TextUtils.isEmpty(mNormalText)) {
            setText(mNormalText);
        }
        super.setSelected(selected);
    }
    @Override
    public boolean isChecked() {
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

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override
    public boolean performClick() {
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
            OnCheckedChangeListener<ToggleTextButton> onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }
}
