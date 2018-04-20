package com.github.liaoheng.common.network;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.github.liaoheng.common.util.FileUtils;
import com.github.liaoheng.common.util.SystemException;

import java.io.File;

/**
 * Glide Config
 *
 * @author liaoheng
 * @version 2016-06-24 14:19
 */
public class CNGlideModule {

    public static void applyOptions(Context context, GlideBuilder builder) {
        try {
            File imgCache = FileUtils.createCacheSDAndroidDirectory(CommonNet.DISK_CACHE_DIR);
            builder.setDiskCache(new DiskLruCacheFactory(imgCache.getAbsolutePath(),
                    (int) CommonNet.IMAGE_DISK_CACHE_SIZE));
        } catch (SystemException ignored) {
        }
    }
}
