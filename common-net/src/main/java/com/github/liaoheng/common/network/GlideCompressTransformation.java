package com.github.liaoheng.common.network;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.github.liaoheng.common.util.BitmapUtils;

/**
 * 压缩
 * @author http://www.28im.com/android/a951799.html
 */
public class GlideCompressTransformation
        extends com.bumptech.glide.load.resource.bitmap.BitmapTransformation {
    private int scale;
    private int w;
    private int h;

    /**
     * 压缩
     * @param scale 压缩到多少KB
     */
    public GlideCompressTransformation(Context context, int scale) {
        super(context);
        this.scale = scale;
    }

    /**
     * 压缩
     * @param w 到多宽
     * @param h  到多高
     */
    public GlideCompressTransformation(Context context, int w, int h) {
        super(context);
        this.w = w;
        this.h = h;
    }

    /**
     * 压缩
     * @param scale 压缩到多少KB
     */
    public GlideCompressTransformation(Context context, int scale, int w, int h) {
        super(context);
        this.scale = scale;
        this.w = w;
        this.h = h;
    }

    @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth,
                                         int outHeight) {
        Bitmap output = BitmapUtils.compressBitmapByQualityAll(toTransform, scale, w, h);

        if (toTransform != output) {
            toTransform.recycle();
        }

        return output;
    }

    @Override public String getId() {
        return "CompressTransformation";
    }
}
