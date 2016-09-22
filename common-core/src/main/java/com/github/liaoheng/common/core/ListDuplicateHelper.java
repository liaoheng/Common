package com.github.liaoheng.common.core;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import java.util.Collections;
import java.util.List;

/**
 * 列表数据除重，使用不可重复缓存实现
 *
 * @author liaoheng
 * @version 2015-10-30 16:37
 */
public class ListDuplicateHelper<K> {
    private SparseArray<K> mCacheListMap = new SparseArray<>();
    private List<K>                  mOriginalList;
    private DataOperationListener<K> mHaveDataOperationListener;
    private DataOperationListener<K> mHaveNotDataOperationListener;

    public ListDuplicateHelper(List<K> originalList,
                               DataOperationListener<K> haveNotDataOperationListener) {
        this(originalList, null, haveNotDataOperationListener);
    }

    public ListDuplicateHelper(@NonNull List<K> originalList,
                               DataOperationListener<K> haveDataOperationListener,
                               DataOperationListener<K> haveNotDataOperationListener) {
        mOriginalList = originalList;
        mHaveDataOperationListener = haveDataOperationListener;
        mHaveNotDataOperationListener = haveNotDataOperationListener;
    }

    public ListDuplicateHelper(List<K> originalList) {
        this(originalList, null);
    }

    public interface DataOperationListener<K> {
        void handle(int location, K cacheData, K newData);
    }

    public interface Key<K> {
        int getKey(K item);
    }

    public K getCacheData(int key) {
        return mCacheListMap.get(key);
    }

    private void operation(DataOperationListener<K> operationListener, int location, K cacheData,
                           K newData) {
        if (operationListener != null) {
            operationListener.handle(location, cacheData, newData);
        }
    }

    /**
     *  @see List#add(Object)
     * @param newData 最新数据
     * @param cacheKey 数据缓存关键字
     */
    public void addDuplicate(K newData, int cacheKey) {
        addDuplicate(newData, cacheKey, mHaveDataOperationListener, mHaveNotDataOperationListener);
    }

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

    public void addDuplicate(int location, K newData, int cacheKey) {
        addDuplicate(location, newData, cacheKey, mHaveDataOperationListener,
                mHaveNotDataOperationListener);
    }

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

    public void addAllDuplicate(List<K> newList, Key<K> newData) {
        addAllDuplicate(newList, newData, mHaveDataOperationListener,
                mHaveNotDataOperationListener);
    }

    public void addAllDuplicate(List<K> newList, Key<K> call,
                                DataOperationListener<K> haveDataOperationListener,
                                DataOperationListener<K> haveNotDataOperationListener) {
        for (K newData : newList) {
            addDuplicate(newData, call.getKey(newData), haveDataOperationListener,
                    haveNotDataOperationListener);
        }
    }

    public void addAllDuplicate(int location, List<K> newList, Key<K> call) {
        addAllDuplicate(location, newList, call, mHaveDataOperationListener,
                mHaveNotDataOperationListener);
    }

    public void addAllDuplicate(int location, List<K> newList, Key<K> call,
                                DataOperationListener<K> haveDataOperationListener,
                                DataOperationListener<K> haveNotDataOperationListener) {
        Collections.reverse(newList);
        for (K newData : newList) {
            addDuplicate(location, newData, call.getKey(newData), haveDataOperationListener,
                    haveNotDataOperationListener);
        }
    }

    public void updateDuplicate(List<K> newList, Key<K> call) {
        clearDuplicate();
        addAllDuplicate(newList, call);
    }

    public void updateDuplicate(List<K> newList, Key<K> call,
                                DataOperationListener<K> haveDataOperationListener,
                                DataOperationListener<K> haveNotDataOperationListener) {
        clearDuplicate();
        addAllDuplicate(newList, call, haveDataOperationListener, haveNotDataOperationListener);
    }

    public void removeDuplicate(int key) {
        mCacheListMap.remove(key);
    }

    public void clearDuplicate() {
        mCacheListMap.clear();
        mOriginalList.clear();
    }

    public DataOperationListener<K> getHaveDataOperationListener() {
        return mHaveDataOperationListener;
    }

    public DataOperationListener<K> getHaveNotDataOperationListener() {
        return mHaveNotDataOperationListener;
    }
}
