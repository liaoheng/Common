package com.github.liaoheng.common.ui.util;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.liaoheng.common.util.UIUtils;

/**
 * 为子控件传递按压事件
 *
 * @author liaoheng
 * @date 2023-02-14 09:39
 */
@SuppressLint("ClickableViewAccessibility")
public class TouchChildPressed implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ViewGroup vv = (ViewGroup) v;
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (UIUtils.isViewFastClick()) {//todo
                return true;
            }
            for (int i = 0; i < vv.getChildCount(); i++) {
                vv.getChildAt(i).setPressed(true);
            }
        } else if (MotionEvent.ACTION_UP == event.getAction()) {
            for (int i = 0; i < vv.getChildCount(); i++) {
                vv.getChildAt(i).setPressed(false);
            }
        } else {
            return false;
        }
        return false;
    }
}
