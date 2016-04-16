package com.leng.common.plus.core;

import java.util.Collections;
import java.util.List;

import android.util.SparseArray;

/**
 * 列表数据除重
 * @author liaoheng
 * @version 2015-10-30 16:37
 */
public class ListDuplicateHelper<K> {
    private SparseArray<K> mCacheListMap = new SparseArray<>();
    List<K>                oldList;

    public ListDuplicateHelper(List<K> oldList) {
        this.oldList = oldList;
    }

    public interface Key<K> {
        int getKey(K item);
    }

    public void addDuplicate(K item, int key) {
        if (null == this.mCacheListMap.get(key)) {
            this.mCacheListMap.put(key, item);
            oldList.add(item);
        }
    }

    public void addDuplicate(int index, K item, int key) {
        if (null == this.mCacheListMap.get(key)) {
            this.mCacheListMap.put(key, item);
            oldList.add(index, item);
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
        clearDuplicate(oldList);
        addAllDuplicate(newList, call);
    }

    public void removeDuplicate(int key) {
        this.mCacheListMap.remove(key);
    }

    public void clearDuplicate(List<K> oldList) {
        this.mCacheListMap.clear();
        oldList.clear();
    }

}
