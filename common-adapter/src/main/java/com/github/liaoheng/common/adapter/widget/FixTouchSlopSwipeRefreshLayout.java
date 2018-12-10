package com.github.liaoheng.common.adapter.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * SwipeRefreshLayout solve the drop-down and left and right swipe gestures conflict
 * @author http://stackoverflow.com/questions/23989910/horizontalscrollview-inside-swiperefreshlayout
 */
public class FixTouchSlopSwipeRefreshLayout extends SwipeRefreshLayout {

    private int   mTouchSlop;
    private float mPrevX;

    public FixTouchSlopSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @SuppressLint("Recycle") @Override public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }
}
