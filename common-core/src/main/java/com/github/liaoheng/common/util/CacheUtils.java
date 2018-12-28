package com.github.liaoheng.common.util;

import android.content.Context;
import com.github.liaoheng.common.BuildConfig;
import com.github.liaoheng.common.cache.DiskLruCache;
import org.apache.commons.io.IOUtils;

import java.io.*;

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

    private void init(Context context, String cacheDir) throws IOException {
        init(getFileCachePath(context, cacheDir));
    }

    private void init(File cachePath) throws IOException {
        diskLruCache = DiskLruCache.open(cachePath, BuildConfig.VERSION_CODE, 1,
                1024 * 1024 * 10);//Bytes
    }

    public static File getFileCachePath(Context context, String cacheDir) throws IOException {
        try {
            File sdExternalPath = FileUtils.getProjectSpacePath(context);
            return FileUtils.createHideMediaDirectory(
                    FileUtils.createPath(sdExternalPath.getAbsolutePath(), cacheDir));
        } catch (SystemException e) {
            throw new IOException(e);
        }
    }

    public void put(String key, File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            put(key, inputStream);
        } catch (FileNotFoundException ignored) {
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public File put(int key, InputStream inputStream) {
        return put(String.valueOf(key), inputStream);
    }

    public File put(String key, InputStream inputStream) {
        DiskLruCache.Editor edit = null;
        OutputStream outputStream = null;
        try {
            edit = diskLruCache.edit(key);
            outputStream = edit.newOutputStream(0);
            IOUtils.copy(inputStream, outputStream);
            edit.commit();
            diskLruCache.flush();
        } catch (IOException e) {
            try {
                if (edit != null) {
                    edit.abort();
                }
            } catch (IOException ignored) {
            }
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        if (edit == null || edit.isHasErrors()) {
            return null;
        }
        return edit.getEntry().getCleanFile(0);
    }

    public File get(String key) {
        try {
            return diskLruCache.getFile(key, 0);
        } catch (Exception ignored) {
        }
        return null;
    }
}
