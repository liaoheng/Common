package com.github.liaoheng.common.util;

import android.util.LruCache;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

/**
 * Time LruCache
 *
 * @author liaoheng
 * @date 2021-09-23 15:37
 */
public class TimeCache<K, V> {
    private long defaultDuring = TimeUnit.MINUTES.toMillis(120);
    private final LruCache<K, Cache<V>> lruCache;

    /**
     * construct.
     *
     * @param maxSize maxSize
     */
    public TimeCache(int maxSize) {
        this.lruCache = new LruCache<>(maxSize);
    }

    /**
     * construct
     *
     * @param maxSize       maxSize
     * @param defaultDuring defaultDuring(milliseconds)
     */
    public TimeCache(int maxSize, long defaultDuring) {
        this(maxSize);
        this.defaultDuring = defaultDuring;
    }

    /**
     * get value by key
     *
     * @param key key
     * @return value, return null when not found or value expired
     */
    public V get(@NonNull K key) {
        Cache<V> cacheItem = lruCache.get(key);

        if (cacheItem == null) {
            return null;
        }

        if (isCacheItemAlive(cacheItem)) {
            return cacheItem.value;
        } else {
            lruCache.remove(key);
            return null;
        }
    }

    public static boolean isCacheItemAlive(Cache<?> item) {
        return item.deleteTime > System.currentTimeMillis();
    }

    /**
     * put a value by key
     *
     * @param key    key
     * @param value  value
     * @param during during(milliseconds)
     * @return previous value, return null if not found
     */
    public V put(@NonNull K key, @NonNull V value, long during) {
        if (during < 0) {
            throw new IllegalArgumentException("during should >= 0");
        }

        Date date = new Date();
        long time = date.getTime();
        Cache<V> cacheItem = new Cache<>(value, time, time, time + during);

        Cache<V> previous = lruCache.put(key, cacheItem);

        if (previous != null) {
            cacheItem.createTime = previous.createTime;
            return cacheItem.value;
        } else {
            return null;
        }
    }

    /**
     * put a value by key(during = defaultDuring)
     *
     * @param key   key
     * @param value value
     * @return previous value, return null if not found
     */
    public V put(@NonNull K key, @NonNull V value) {
        return put(key, value, defaultDuring);
    }

    /**
     * remove value by key
     *
     * @param key key
     * @return removed value, return null if not found
     */
    public V remove(@NonNull K key) {
        Cache<V> remove = lruCache.remove(key);
        if (remove == null) {
            return null;
        }
        return remove.value;
    }

    public void clear() {
        lruCache.evictAll();
    }

    static class Cache<V> {
        public Cache(V value, long createTime, long updateTime, long deleteTime) {
            this.value = value;
            this.createTime = createTime;
            this.updateTime = updateTime;
            this.deleteTime = deleteTime;
        }

        private final V value;
        private long createTime;
        private long updateTime;
        private long deleteTime;
    }

}
