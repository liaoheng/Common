package com.github.liaoheng.common.util;

import android.content.Context;

import com.github.liaoheng.common.Common;
import com.github.liaoheng.common.cache.DiskLruCache;
import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author liaoheng
 * @version 2018-12-28 15:11
 */
public class CacheUtils {
    private static CacheUtils mCacheUtils;

    public static CacheUtils get() {
        if (mCacheUtils == null) {
            mCacheUtils = new CacheUtils();
        }
        return mCacheUtils;
    }

    private DiskLruCache diskLruCache;

    public void init(Context context, String cacheDir) {
        try {
            init(FileUtils.getProjectSpaceCacheDirectory(context, cacheDir));
        } catch (IOException ignored) {
        }
    }

    public void init(File cachePath) throws IOException {
        init(cachePath, 1024 * 1024 * 10);
    }

    /**
     * @param maxSize Bytes
     */
    public void init(File cachePath, long maxSize) throws IOException {
        init(cachePath, maxSize, Common.sAppVersion);
    }

    /**
     * @param maxSize Bytes
     */
    public void init(File cachePath, long maxSize, int appVersion) throws IOException {
        //valueCount : 同一个key可以对应多少个缓存文件
        diskLruCache = DiskLruCache.open(cachePath, appVersion, 1,
                maxSize);
    }

    public void clear() {
        if (diskLruCache == null) {
            return;
        }
        try {
            diskLruCache.clear();
        } catch (IOException ignored) {
        }
    }

    public File put(String key, File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return put(key, inputStream);
        } catch (IOException ignored) {
        }
        return null;
    }

    public File put(int key, InputStream inputStream) {
        return put(String.valueOf(key), inputStream);
    }

    public File put(String key, InputStream inputStream) {
        if (diskLruCache == null) {
            return null;
        }
        DiskLruCache.Editor edit = null;
        try {
            edit = diskLruCache.edit(key);
            try (OutputStream outputStream = edit.newOutputStream(0)) {
                ByteStreams.copy(inputStream, outputStream);
            }
            edit.commit();
            diskLruCache.flush();
        } catch (IOException e) {
            try {
                if (edit != null) {
                    edit.abort();
                }
            } catch (IOException ignored) {
            }
        }
        if (edit == null || edit.isHasErrors()) {
            return null;
        }
        return edit.getEntry().getCleanFile(0);
    }

    public File get(String key) {
        if (diskLruCache == null) {
            return null;
        }
        try {
            return diskLruCache.getFile(key, 0);
        } catch (Exception ignored) {
        }
        return null;
    }
}
