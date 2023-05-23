package com.github.liaoheng.common.ui.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

import com.github.liaoheng.common.ui.model.PagerTab;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liaoheng
 * @date 2023-05-23 18:37
 */
public class PagerAdapter {
    private final DataSetObservable mObservable = new DataSetObservable();
    private List<PagerTab> channels = new ArrayList<>();

    public void notifyChanged() {
        mObservable.notifyChanged();
    }

    public void clear(){
        channels.clear();
    }

    public void add(PagerTab channel) {
        channels.add(channel);
    }

    public void remove(PagerTab channel){
        channels.remove(channel);
    }

    public void registerAdapterDataObserver(DataSetObserver observer) {
        mObservable.registerObserver(observer);
    }

    public void unregisterAdapterDataObserver(DataSetObserver observer) {
        mObservable.unregisterObserver(observer);
    }

    public PagerTab getItem(int position) {
        return channels.get(position);
    }

    public int getCount() {
        return channels.size();
    }
}
