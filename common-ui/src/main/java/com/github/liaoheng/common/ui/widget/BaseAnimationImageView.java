package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.github.liaoheng.common.util.DelayHandler;

/**
 * 加载动画控件
 *
 * @author liaoheng
 * @date 2021-10-29 14:43
 */
public abstract class BaseAnimationImageView extends AppCompatImageView {

    public BaseAnimationImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseAnimationImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseAnimationImageView(Context context) {
        this(context, null);
    }

    protected void init() {

    }

    protected void start() {

    }

    protected void stop() {

    }

    private final DelayHandler mHandler = new DelayHandler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                start();
            } else if (msg.what == 2) {
                stop();
            }
        }
    };

    private void visibilityChanged(int visibility, boolean isCheck) {
        if (visibility == VISIBLE) {
            if (isCheck && mHandler.hasMessages(1)) {
                return;
            }
            mHandler.removeMessages(2);
            mHandler.sendDelayed(1, 200);
        } else {
            if (isCheck && mHandler.hasMessages(2)) {
                return;
            }
            mHandler.removeMessages(1);
            mHandler.sendDelayed(2, 200);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        visibilityChanged(visibility, false);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        visibilityChanged(visibility, true);
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeMessages(1);
        mHandler.removeMessages(2);
        super.onDetachedFromWindow();
    }
}
