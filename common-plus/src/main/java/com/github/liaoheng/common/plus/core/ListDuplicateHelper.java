package com.github.liaoheng.common.plus.core;

import android.util.SparseArray;

import java.util.Collections;
import java.util.List;

/**
 * 列表数据除重
 *
 * @author liaoheng
 * @version 2015-10-30 16:37
 */
public class ListDuplicateHelper<K> {
    private SparseArray<K> mCacheListMap = new SparseArray<>();
    private List<K> mOriginalList;

    public ListDuplicateHelper(List<K> originalList) {
        this.mOriginalList = originalList;
    }

    public interface Key<K> {
        int getKey(K item);
    }

    public void addDuplicate(K item, int key) {
        if (null == this.mCacheListMap.get(key)) {
            mCacheListMap.put(key, item);
            mOriginalList.add(item);
        }
    }

    public void addDuplicate(int index, K item, int key) {
        if (null == this.mCacheListMap.get(key)) {
            mCacheListMap.put(key, item);
            mOriginalList.add(index, item);
        }
    }

    public void addAllDuplicate(List<K> newList, Key<K> call) {
        for (K item : newList) {
            addDuplicate(item, call.getKey(item));
        }
    }

    public void addAllDuplicate(int index, List<K> newList, Key<K> call) {
        Collections.reverse(newList);
        for (K item : newList) {
            addDuplicate(index, item, call.getKey(item));
        }
    }

    public void updateDuplicate(List<K> newList, Key<K> call) {
        clearDuplicate();
        addAllDuplicate(newList, call);
    }

    public void removeDuplicate(int key) {
        mCacheListMap.remove(key);
    }

    public void clearDuplicate() {
        mCacheListMap.clear();
        mOriginalList.clear();
    }

}
