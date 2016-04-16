package com.leng.common.plus.adapter;

import java.util.List;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.leng.common.util.UIUtils;

/**
 * 列表基础通用适配器
 * @author liaoheng
 * @version 2015-04-24 15:50
 */
public abstract class BaseListAdapter<T> extends BaseAdapter implements IBaseAdapter<T> {

    private List<T> mList;
    private Context mContext;

    public BaseListAdapter(Context mContext, List<T> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public View inflate(@LayoutRes int resource) {
        return UIUtils.inflate(getContext(), resource);
    }

    @Override
    public View inflate(@LayoutRes int resource, ViewGroup root) {
        return UIUtils.inflate(getContext(), resource, root,false);
    }

    public View inflate(@LayoutRes int resource, ViewGroup root, boolean attachToRoot) {
        return UIUtils.inflate(getContext(), resource, root, attachToRoot);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public List<T> getList() {
        return mList;
    }

    @Override
    public void update(List<T> list) {
        this.mList = list;
    }

    @Override
    public void clear() {
        if (mList == null) {
            return;
        }
        mList.clear();
    }

    @Override
    public void addAll(List<T> mList) {
        if (this.mList == null) {
            this.mList = mList;
        } else {
            this.mList.addAll(mList);
        }
    }

    @Override
    public void addAll(int index, List<T> list) {
        if (this.mList == null) {
            this.mList = list;
        } else {
            this.mList.addAll(index, mList);
        }
    }

    @Override
    public void add(int index, T item) {
        if (this.mList == null) {
            throw new AndroidRuntimeException("add list is null");
        }
        this.mList.add(index, item);
    }

    @Override
    public void add(T o) {
        if (this.mList == null) {
            throw new AndroidRuntimeException("add list is null");
        }
        this.mList.add(o);
    }

    @Override
    public void remove(int location) {
        if (location < 0) {
            throw new AndroidRuntimeException("remove location < 0");
        }
        this.mList.remove(location);
    }

    @Override
    public void remove(T object) {
        if (this.mList == null) {
            throw new AndroidRuntimeException("remove list is null");
        }
        this.mList.remove(object);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(getList().get(position), position, convertView, parent);
    }

    public abstract View getItemView(T item, int position, View convertView, ViewGroup parent);
}
