package com.github.liaoheng.common.network;

import android.graphics.Bitmap;

import com.github.liaoheng.common.util.BitmapUtils;

/**
 * 圆形
 * @author http://www.28im.com/android/a951799.html
 */
public class CNRoundedTransformation implements com.squareup.picasso.Transformation {

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
        return "CNRoundedTransformation";
    }
}