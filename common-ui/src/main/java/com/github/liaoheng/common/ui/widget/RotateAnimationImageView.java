package com.github.liaoheng.common.ui.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 旋转动画
 *
 * @author liaoheng
 * @date 2021-10-29 14:43
 */
public class RotateAnimationImageView extends BaseAnimationImageView {

    public RotateAnimationImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RotateAnimationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateAnimationImageView(Context context) {
        super(context);
    }

    private ObjectAnimator mAnimator;

    protected void init() {
        mAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        mAnimator.setDuration(1800);
        mAnimator.setRepeatCount(Animation.INFINITE);
        mAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mAnimator.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void start() {
        if (mAnimator == null) {
            return;
        }
        if (mAnimator.isPaused()) {
            mAnimator.resume();
        } else {
            mAnimator.start();
        }
    }

    @Override
    protected void stop() {
        if (mAnimator == null) {
            return;
        }
        mAnimator.pause();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator.removeAllListeners();
            mAnimator = null;
        }
    }
}
