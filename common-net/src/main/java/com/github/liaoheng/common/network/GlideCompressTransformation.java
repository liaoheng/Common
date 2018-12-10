package com.github.liaoheng.common.network;

import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.github.liaoheng.common.util.BitmapUtils;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

/**
 * 压缩
 *
 * @see <a href='http://bumptech.github.io/glide/doc/transformations.html'>transformations</a>
 */
public class GlideCompressTransformation extends BitmapTransformation {
    private final String ID = GlideCompressTransformation.class.getSimpleName();
    private final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private int scale;
    private int w;
    private int h;

    /**
     * 压缩
     *
     * @param scale 压缩到多少KB
     */
    public GlideCompressTransformation(int scale) {
        this.scale = scale;
    }

    /**
     * 压缩
     *
     * @param w 到多宽
     * @param h 到多高
     */
    public GlideCompressTransformation(int w, int h) {
        this.w = w;
        this.h = h;
    }

    /**
     * 压缩
     *
     * @param scale 压缩到多少KB
     */
    public GlideCompressTransformation(int scale, int w, int h) {
        this.scale = scale;
        this.w = w;
        this.h = h;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return BitmapUtils.compressBitmapByQualityAndScale(toTransform, scale, w, h, false);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GlideCompressTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

}
