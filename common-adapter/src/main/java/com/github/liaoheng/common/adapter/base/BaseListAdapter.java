package com.github.liaoheng.common.adapter.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Base Adapter for ListView
 *
 * @author liaoheng
 * @version 2015-04-24 15:50
 */
public abstract class BaseListAdapter<T> extends BaseAdapter implements IBaseAdapter<T> {

    private List<T> mList;
    private Context mContext;

    public BaseListAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public BaseListAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
    }

    public View inflate(@LayoutRes int resource, @NonNull ViewGroup root) {
        return inflate(resource, root, false);
    }

    public View inflate(@LayoutRes int resource, @NonNull ViewGroup root, boolean attachToRoot) {
        return LayoutInflater.from(getContext()).inflate(resource,root,attachToRoot);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public List<T> getList() {
        return mList;
    }

    @Override public void setList(List<T> list) {
        mList = list;
    }

    @Override public boolean isEmpty() {
        return getList() == null || getList().isEmpty();
    }

    @Override
    public void update(List<T> list) {
        setList(list);
    }

    @Override
    public void clear() {
        if (isEmpty()) {
            return;
        }
        getList().clear();
    }

    @Override public void addAll(List<T> list) {
        if (null == getList()) {
            setList(list);
        } else {
            getList().addAll(list);
        }
    }

    @Override public void addAll(int location, List<T> list) {
        if (null == getList()) {
            setList(list);
        } else {
            getList().addAll(location, list);
        }
    }

    @Override public void add(int location, T item) {
        getList().add(location, item);
    }

    @Override
    public void add(T o) {
        getList().add(o);
    }

    @Override
    public void remove(int location) {
        getList().remove(location);
    }

    @Override
    public void remove(T object) {
        getList().remove(object);
    }

    @Override
    public int getCount() {
        return getList() == null ? 0 : getList().size();
    }

    @Override
    public Object getItem(int position) {
        return isEmpty() ? null : getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return isEmpty() ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(isEmpty() ? null : getList().get(position), position, convertView,
                parent);
    }

    /**
     * @see BaseAdapter#getView(int, View, ViewGroup)
     * @param item Current list item
     */
    public abstract View getItemView(T item, int position, View convertView, ViewGroup parent);
}
