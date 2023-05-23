package com.github.liaoheng.common.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.MotionEvent;

import com.github.liaoheng.common.util.UIUtils;

/**
 * 点击缩放帮助类
 *
 * @author liaoheng
 * @date 2023-02-15 16:37
 */
public class ClickScaleButtonHelper {

    private AnimatorSet mDownAnimatorSet;
    private AnimatorSet mUpAnimatorSet;
    public float mScaleXTo = 0.96f;
    public float mScaleYTo = 0.96f;
    public int mDuration = 150;

    public ClickScaleButtonHelper() {
    }

    public ClickScaleButtonHelper(float scaleXTo, float scaleYTo, int duration) {
        this.mScaleXTo = scaleXTo;
        this.mScaleYTo = scaleYTo;
        this.mDuration = duration;
    }

    /**
     * 缩放长度
     *
     * @param scaleXTo 比例，1~0
     */
    public void setScaleXTo(float scaleXTo) {
        mScaleXTo = scaleXTo;
    }

    /**
     * 缩放高度
     *
     * @param scaleYTo 比例，1~0
     */
    public void setScaleYTo(float scaleYTo) {
        mScaleYTo = scaleYTo;
    }

    /**
     * 缩放动画时长
     *
     * @param duration 毫秒
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (UIUtils.isViewFastClick()) {//todo
                return true;
            }
            if (mUpAnimatorSet.isRunning()) {
                mUpAnimatorSet.end();
            }
            if (!mDownAnimatorSet.isRunning()) {
                mDownAnimatorSet.start();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mDownAnimatorSet.isRunning()) {
                mDownAnimatorSet.end();
            }
            if (!mUpAnimatorSet.isRunning()) {
                mUpAnimatorSet.start();
            }
        }
        return false;
    }

    @SuppressLint("ObjectAnimatorBinding")
    protected void onAttachedToWindow(Object target) {
        mDownAnimatorSet = new AnimatorSet();
        mDownAnimatorSet.play(ObjectAnimator.ofFloat(target, "scaleX", 1.0f, mScaleXTo))
                .with(ObjectAnimator.ofFloat(target, "scaleY", 1.0f, mScaleYTo));
        mDownAnimatorSet.setDuration(mDuration);
        mUpAnimatorSet = new AnimatorSet();
        mUpAnimatorSet.play(ObjectAnimator.ofFloat(target, "scaleX", mScaleXTo, 1.0f))
                .with(ObjectAnimator.ofFloat(target, "scaleY", mScaleYTo, 1.0f));
        mUpAnimatorSet.setDuration(mDuration);
    }

    protected void onDetachedFromWindow() {
        if (mUpAnimatorSet != null) {
            mUpAnimatorSet.end();
            mUpAnimatorSet.cancel();
            mUpAnimatorSet = null;
        }
        if (mDownAnimatorSet != null) {
            mDownAnimatorSet.end();
            mDownAnimatorSet.cancel();
            mDownAnimatorSet = null;
        }
    }
}
