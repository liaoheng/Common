package com.github.liaoheng.common.plus.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.AndroidRuntimeException;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link FragmentStatePagerAdapter}
 *
 * @author liaoheng
 * @version 2015-11-27 14:56:32
 */
public abstract class BaseFragmentStatePagerAdapter<T> extends FragmentStatePagerAdapter
        implements IBaseAdapter<T> {

    private Context mContext;
    private List<T> mList;

    public BaseFragmentStatePagerAdapter(FragmentManager fm, Context context, List<T> list) {
        super(fm);
        this.mContext = context;
        this.mList = list != null ? list : new ArrayList<T>();
    }

    @Override
    public Fragment getItem(int position) {
        return getItemView(getList() == null ? null : getList().get(position), position);
    }

    public abstract Fragment getItemView(T item, int position);

    @Override
    public void clear() {
        if (mList == null) {
            return;
        }
        mList.clear();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
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
    public void update(List<T> mList) {
        this.mList = mList;
    }

    @Override
    public void addAll(List<T> mList) {
        if (null == this.mList) {
            this.mList = mList;
        } else {
            this.mList.addAll(mList);
        }
    }

    @Override
    public void addAll(int index, List<T> mList) {
        if (null == this.mList) {
            this.mList = mList;
        } else {
            this.mList.addAll(index, mList);
        }
    }

    @Override
    public void add(int index, T o) {
        if (null == this.mList) {
            throw new AndroidRuntimeException("add list is null");
        }
        this.mList.add(index, o);
    }

    @Override
    public void add(T o) {
        if (null == this.mList) {
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
    public void remove(T item) {
        if (null == item) {
            throw new AndroidRuntimeException("remove list is null");
        }
        this.mList.remove(item);
    }
}
