package com.github.liaoheng.common.plus.util;

import android.graphics.Bitmap;

import com.github.liaoheng.common.util.BitmapUtils;

/**
 * 压缩
 * @author http://www.28im.com/android/a951799.html
 */
public class CompressTransformation implements com.squareup.picasso.Transformation {
    private int scale;
    private int w;
    private int h;

    /**
     * 压缩
     * @param scale 压缩到多少KB
     */
    public CompressTransformation(int scale) {
        this.scale = scale;
    }
    /**
     * 压缩
     * @param w 到多宽
     * @param h  到多高
     */
    public CompressTransformation(int w, int h) {
        this.w = w;
        this.h = h;
    }

    /**
     * 压缩
     * @param scale 压缩到多少KB
     */
    public CompressTransformation(int scale, int w, int h) {
        this.scale = scale;
        this.w = w;
        this.h = h;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        Bitmap output = BitmapUtils.compressBitmapByQualityAll(source, scale, w, h);

        if (source != output) {
            source.recycle();
        }

        return output;
    }

    @Override
    public String key() {
        return "CompressTransformation";
    }
}
