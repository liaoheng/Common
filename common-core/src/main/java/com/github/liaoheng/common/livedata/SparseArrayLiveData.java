package com.github.liaoheng.common.livedata;

import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.github.liaoheng.common.util.Callback1;

/**
 * @author liaoheng
 * @date 2021-09-26 17:17
 */
public class SparseArrayLiveData<T> extends MutableLiveData<SparseArray<T>> {
    final Object mDataLock = new Object();

    public SparseArrayLiveData() {
        super();
    }

    public SparseArrayLiveData(SparseArray<T> value) {
        setValue(value);
    }

    public void putPost(int key, T t) {
        put(key, t, this::setValue);
    }

    public void putSet(int key, T t) {
        put(key, t, this::setValue);
    }

    public void put(int key, T t, Callback1<SparseArray<T>> consumer) {
        SparseArray<T> value = getValue();
        if (value == null) {
            value = new SparseArray<>();
        }
        synchronized (mDataLock) {
            value.put(key, t);
            consumer.accept(value);
        }
    }

    public void removePost(int key) {
        remove(key, this::postValue);
    }

    public void removeSet(int key) {
        remove(key, this::setValue);
    }

    public void remove(int key, Callback1<SparseArray<T>> consumer) {
        if (getValue() == null) {
            return;
        }
        synchronized (mDataLock) {
            getValue().remove(key);
            consumer.accept(getValue());
        }
    }

    @Nullable
    public T get(int key) {
        if (getValue() == null) {
            return null;
        }
        return getValue().get(key);
    }

}
