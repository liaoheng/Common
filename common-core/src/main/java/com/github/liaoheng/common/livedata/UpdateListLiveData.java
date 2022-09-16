package com.github.liaoheng.common.livedata;

import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author liaoheng
 * @date 2021-09-10 13:55
 */
public class UpdateListLiveData<T> extends MutableLiveData<List<T>> {
    final Object mDataLock = new Object();

    public UpdateListLiveData() {
        super();
    }

    public UpdateListLiveData(List<T> value) {
        super(value);
    }

    public void add(T t) {
        if (getValue() == null) {
            return;
        }
        synchronized (mDataLock) {
            getValue().add(t);
            postValue(getValue());
        }
    }

    public void addAll(List<T> t) {
        synchronized (mDataLock) {
            addAllNoNotify(t);
            sendNotify();
        }
    }

    public void addAllNoNotify(List<T> t) {
        if (getValue() == null) {
            postValue(t);
            return;
        }
        getValue().addAll(t);
    }

    public void put(int index, T t) {
        if (getValue() == null) {
            return;
        }
        synchronized (mDataLock) {
            getValue().remove(t);
            getValue().add(index, t);
            postValue(getValue());
        }
    }

    public void put(int index, T t, Function<T, Boolean> function) {
        if (getValue() == null) {
            return;
        }
        synchronized (mDataLock) {
            T tt = get(t);
            if (tt != null) {
                if (function.apply(tt)) {
                    return;
                }
            }
            getValue().remove(t);
            getValue().add(index, t);
            postValue(getValue());
        }
    }

    public void update(T t, Consumer<T> function) {
        if (getValue() == null) {
            return;
        }
        synchronized (mDataLock) {
            T tt = get(t);
            if (tt == null) {
                return;
            }
            function.accept(tt);
            postValue(getValue());
        }
    }

    public T get(T t) {
        if (getValue() == null) {
            return null;
        }
        int index = getValue().indexOf(t);
        if (index > -1) {
            return getValue().get(index);
        }
        return null;
    }

    public void sendNotify() {
        if (getValue() == null) {
            return;
        }
        postValue(getValue());
    }

    public void sort(Comparator<? super T> c) {
        if (getValue() == null || getValue().isEmpty()) {
            return;
        }
        Collections.sort(getValue(), c);
    }

    public void remove(T t) {
        if (getValue() == null) {
            return;
        }
        synchronized (mDataLock) {
            getValue().remove(t);
            postValue(getValue());
        }
    }

    public boolean isEmpty() {
        return getValue() == null || getValue().isEmpty();
    }

    public void clear() {
        if (getValue() == null) {
            return;
        }
        synchronized (mDataLock) {
            getValue().clear();
            postValue(getValue());
        }
    }

    public void clearNoNotify() {
        if (getValue() == null) {
            return;
        }
        getValue().clear();
    }

}
