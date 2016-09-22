package com.github.liaoheng.common.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.CheckedTextView;

/**
 * @author liaoheng
 * @version 2015年9月22日
 */
public class ToggleTextButton extends CheckedTextView implements Checkable {
    private OnCheckedChangeListener onCheckedChangeListener;

    public ToggleTextButton(Context context) {
        super(context);
    }

    public ToggleTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleTextButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isChecked() {
        return isSelected();
    }

    @Override
    public void setChecked(boolean checked) {
        setSelected(checked);

        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(this, checked);
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

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public  interface OnCheckedChangeListener {
         void onCheckedChanged(ToggleTextButton buttonView, boolean isChecked);
    }
}
