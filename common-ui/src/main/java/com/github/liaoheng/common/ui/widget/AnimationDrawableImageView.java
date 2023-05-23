package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 加载图片动画
 *
 * @author liaoheng
 * @date 2021-10-29 14:43
 */
public class AnimationDrawableImageView extends BaseAnimationImageView {

    public AnimationDrawableImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AnimationDrawableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationDrawableImageView(Context context) {
        super(context);
    }

    protected void init() {

    }

    @Override
    protected void start() {
        startRotate(true);
    }

    @Override
    protected void stop() {
        startRotate(false);
    }

    public void startRotate(boolean rotate) {
        if (getDrawable() == null) {
            return;
        }
        if (!(getDrawable() instanceof AnimationDrawable)) {
            return;
        }
        if (rotate) {
            ((AnimationDrawable) getDrawable()).start();
        } else {
            ((AnimationDrawable) getDrawable()).stop();
        }
    }

}
