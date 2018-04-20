package com.github.liaoheng.common.core;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.Collections;
import java.util.List;

/**
 * 列表数据除重，使用不可重复缓存实现。KEY与数据列表中的数据没有直接关系，需要使用ID等不重复的数据为KEY值。
 *
 * @author liaoheng
 * @version 2015-10-30 16:37
 */
public class ListDuplicateHelper<K> {
    private SparseArray<K> mCacheListMap = new SparseArray<>();
    private List<K> mOriginalList;
    private DataOperationListener<K> mHaveDataOperationListener; //公共新数据时回调
    private DataOperationListener<K> mHaveNotDataOperationListener;//公共重复数据时回调

    /**
     * 列表数据去重复
     *
     * @param originalList 需要去重的数据列表
     * @param haveNotDataOperationListener 重复数据操作时回调
     */
    public ListDuplicateHelper(List<K> originalList,
            DataOperationListener<K> haveNotDataOperationListener) {
        this(originalList, null, haveNotDataOperationListener);
    }

    /**
     * 列表数据去重复
     *
     * @param originalList 需要去重的数据列表
     * @param haveDataOperationListener 新数据操作时回调
     * @param haveNotDataOperationListener 重复数据操作时回调
     */
    public ListDuplicateHelper(@NonNull List<K> originalList,
            DataOperationListener<K> haveDataOperationListener,
            DataOperationListener<K> haveNotDataOperationListener) {
        mOriginalList = originalList;
        mHaveDataOperationListener = haveDataOperationListener;
        mHaveNotDataOperationListener = haveNotDataOperationListener;
    }

    /**
     * 列表数据去重复
     *
     * @param originalList 需要去重的数据列表
     */
    public ListDuplicateHelper(List<K> originalList) {
        this(originalList, null);
    }

    /**
     * 列表数据去重复，搭配{@link ListDuplicateHelper#setOriginalList(List)} 使用
     */
    public ListDuplicateHelper() {
        this(null, null);
    }

    /**
     * 每条数据操作时回调
     */
    public interface DataOperationListener<K> {
        /**
         * 数据操作回调
         *
         * @param location 列表中位置 {@link List#indexOf(Object)}
         * @param cacheData 缓存中的数据
         * @param newData 新的数据
         */
        void handle(int location, K cacheData, K newData);
    }

    /**
     * 自定义缓存KEY
     */
    public interface CacheKey<K> {
        /**
         * 得到缓存KEY
         *
         * @param item 当前项
         * @return key
         */
        int getCacheKey(K item);
    }

    /**
     * 获得缓存数据
     */
    public K getCacheData(int key) {
        return mCacheListMap.get(key);
    }

    /**
     * 删除缓存数据
     */
    public void reomveCacheData(int key) {
        mCacheListMap.remove(key);
    }

    private void operation(DataOperationListener<K> operationListener, int location, K cacheData,
            K newData) {
        if (operationListener != null) {
            operationListener.handle(location, cacheData, newData);
        }
    }

    /**
     * 添加数据
     *
     * @param newData 最新数据
     * @param cacheKey 数据缓存key
     * @see List#add(Object)
     */
    public void addDuplicate(K newData, int cacheKey) {
        addDuplicate(newData, cacheKey, mHaveDataOperationListener, mHaveNotDataOperationListener);
    }

    /**
     * 添加数据
     *
     * @param newData 最新数据
     * @param cacheKey 数据缓存key
     * @param haveDataOperationListener 新数据操作时回调
     * @param haveNotDataOperationListener 重复数据操作时回调
     * @see List#add(Object)
     */
    public void addDuplicate(K newData, int cacheKey,
            DataOperationListener<K> haveDataOperationListener,
            DataOperationListener<K> haveNotDataOperationListener) {
        K cacheData = getCacheData(cacheKey);
        if (null == cacheData) {
            mCacheListMap.put(cacheKey, newData);
            mOriginalList.add(newData);
            operation(haveDataOperationListener, 0, null, newData);
        } else {
            operation(haveNotDataOperationListener, 0, cacheData, newData);
        }
    }

    /**
     * 指定位置添加数据
     *
     * @param location 指定位置
     * @param newData 最新数据
     * @param cacheKey 数据缓存key
     */
    public void addDuplicate(int location, K newData, int cacheKey) {
        addDuplicate(location, newData, cacheKey, mHaveDataOperationListener,
                mHaveNotDataOperationListener);
    }

    /**
     * 指定位置添加数据
     *
     * @param location 指定位置
     * @param newData 最新数据
     * @param cacheKey 数据缓存key
     * @param haveDataOperationListener 新数据操作时回调
     * @param haveNotDataOperationListener 重复数据操作时回调
     */
    public void addDuplicate(int location, K newData, int cacheKey,
            DataOperationListener<K> haveDataOperationListener,
            DataOperationListener<K> haveNotDataOperationListener) {
        K cacheData = getCacheData(cacheKey);
        if (null == cacheData) {
            mCacheListMap.put(cacheKey, newData);
            mOriginalList.add(location, newData);
            operation(haveDataOperationListener, location, null, newData);
        } else {
            operation(haveNotDataOperationListener, location, cacheData, newData);
        }
    }

    /**
     * 添加数据列表中的多个数据，原始数据列表对象不改变，只会放入新列表中的数据 。
     *
     * @param newList 新数据列表
     * @param cacheKey 新数据列表缓存key处理
     */
    public void addAllDuplicate(List<K> newList, CacheKey<K> cacheKey) {
        addAllDuplicate(newList, cacheKey, mHaveDataOperationListener,
                mHaveNotDataOperationListener);
    }

    /**
     * 添加数据列表中的多个数据，原始数据列表对象不改变，只会放入新列表中的数据 。
     *
     * @param newList 新数据列表
     * @param cacheKey 新数据列表缓存key处理
     * @param haveDataOperationListener 新数据操作时回调
     * @param haveNotDataOperationListener 重复数据操作时回调
     */
    public void addAllDuplicate(List<K> newList, CacheKey<K> cacheKey,
            DataOperationListener<K> haveDataOperationListener,
            DataOperationListener<K> haveNotDataOperationListener) {
        for (K newData : newList) {
            addDuplicate(newData, cacheKey.getCacheKey(newData), haveDataOperationListener,
                    haveNotDataOperationListener);
        }
    }

    /**
     * 指定位置添加数据列表中的多个数据，原始数据列表对象不改变，只会放入新列表中的数据 。
     *
     * @param location 指定位置
     * @param newList 新数据列表
     * @param cacheKey 新数据列表缓存key处理
     */
    public void addAllDuplicate(int location, List<K> newList, CacheKey<K> cacheKey) {
        addAllDuplicate(location, newList, cacheKey, mHaveDataOperationListener,
                mHaveNotDataOperationListener);
    }

    /**
     * 指定位置添加数据列表中的多个数据，原始数据列表对象不改变，只会放入新列表中的数据 。
     *
     * @param location 指定位置
     * @param newList 新数据列表
     * @param cacheKey 新数据列表缓存key处理
     * @param haveDataOperationListener 新数据操作时回调
     * @param haveNotDataOperationListener 重复数据操作时回调
     */
    public void addAllDuplicate(int location, List<K> newList, CacheKey<K> cacheKey,
            DataOperationListener<K> haveDataOperationListener,
            DataOperationListener<K> haveNotDataOperationListener) {
        Collections.reverse(newList);
        for (K newData : newList) {
            addDuplicate(location, newData, cacheKey.getCacheKey(newData), haveDataOperationListener,
                    haveNotDataOperationListener);
        }
    }

    /**
     * 更新原始列表数据为新数据列表数据，原始数据列表对象不改变，只会放入新列表中的数据 。
     *
     * @param newList 新数据列表
     * @param cacheKey 新数据列表缓存key处理
     */
    public void updateDuplicate(List<K> newList, CacheKey<K> cacheKey) {
        clearDuplicate();
        addAllDuplicate(newList, cacheKey);
    }

    /**
     * 更新原始列表数据为新数据列表数据，原始数据列表对象不改变，只会放入新列表中的数据 。
     *
     * @param newList 新数据列表
     * @param cacheKey 新数据列表缓存key处理
     * @param haveDataOperationListener 新数据操作时回调
     * @param haveNotDataOperationListener 重复数据操作时回调
     */
    public void updateDuplicate(List<K> newList, CacheKey<K> cacheKey,
            DataOperationListener<K> haveDataOperationListener,
            DataOperationListener<K> haveNotDataOperationListener) {
        clearDuplicate();
        addAllDuplicate(newList, cacheKey, haveDataOperationListener, haveNotDataOperationListener);
    }

    /**
     * 删除数据
     *
     * @param index 数据列表中的位置 {@link List#remove(int)}
     */
    public void removeDuplicate(int index, int key) {
        mCacheListMap.remove(key);
        mOriginalList.remove(index);
    }

    /**
     * 删除数据
     *
     * @param item 数据列表中的位置 {@link List#remove(Object)}
     */
    public void removeDuplicate(K item, int key) {
        mCacheListMap.remove(key);
        mOriginalList.remove(item);
    }

    /**
     * 清除数据，缓存与数据
     */
    public void clearDuplicate() {
        if (null == mOriginalList) {
            return;
        }
        mCacheListMap.clear();
        mOriginalList.clear();
    }

    /**
     * 设置新的需要去重的数据列表
     */
    public void setOriginalList(List<K> originalList) {
        clearDuplicate();
        mOriginalList = originalList;
    }

    /**
     * @param haveDataOperationListener 公共新数据操作时回调
     */
    public void setHaveDataOperationListener(
            DataOperationListener<K> haveDataOperationListener) {
        mHaveDataOperationListener = haveDataOperationListener;
    }

    /**
     * @param haveNotDataOperationListener 公共重复数据操作时回调
     */
    public void setHaveNotDataOperationListener(
            DataOperationListener<K> haveNotDataOperationListener) {
        mHaveNotDataOperationListener = haveNotDataOperationListener;
    }
}
