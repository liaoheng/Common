package com.github.liaoheng.common.network;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.github.liaoheng.common.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Glide Config
 * <pre>
 * &#64;GlideModule
 * public class MGlideModule extends AppGlideModule {
 *     &#64;Override
 *     public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
 *             CNGlideModule.applyOptions(context,builder);
 *     }
 * }
 *  </pre>
 *
 * @author liaoheng
 * @version 2016-06-24 14:19
 */
public class CNGlideModule {

    public static void applyOptions(Context context, GlideBuilder builder) {
        try {
            File imgCache = FileUtils.getProjectSpaceCacheDirectory(context, CommonNet.DISK_CACHE_DIR);
            applyOptions(builder, imgCache, CommonNet.IMAGE_DISK_CACHE_SIZE);
        } catch (IOException ignored) {
        }
    }

    public static void applyOptions(GlideBuilder builder, File imgCache, long diskCacheSize) {
        builder.setDiskCache(new DiskLruCacheFactory(imgCache.getAbsolutePath(), diskCacheSize));
    }
}
