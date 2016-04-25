package com.github.liaoheng.common.plus.util;

import android.graphics.Bitmap;

import com.github.liaoheng.common.util.BitmapUtils;

/**
 * 圆形
 * @author http://www.28im.com/android/a951799.html
 */
public class RoundedTransformation implements com.squareup.picasso.Transformation {

    @Override
    public Bitmap transform(final Bitmap source) {

        Bitmap output =  BitmapUtils.toRoundCorner(source);
        if (source != output) {
            source.recycle();
        }

        return output;
    }

    @Override
    public String key() {
        return "RoundedTransformation";
    }
}
