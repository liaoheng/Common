package com.github.liaoheng.common.adapter.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Base Adapter for FragmentStatePagerAdapter
 *
 * @author liaoheng
 * @version 2015-11-27 14:56:32
 */
public abstract class BaseFragmentStatePagerAdapter<T> extends FragmentStatePagerAdapter
        implements IBaseAdapter<T> {

    private Context mContext;
    private List<T> mList;

    public BaseFragmentStatePagerAdapter(FragmentManager fm, Context context) {
        this(fm, context, new ArrayList<T>());
    }

    public BaseFragmentStatePagerAdapter(FragmentManager fm, Context context, List<T> list) {
        super(fm);
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : getList().size();
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

    @Override public void update(List<T> list) {
        setList(list);
    }

    @Override public boolean isEmpty() {
        return getList() == null || getList().isEmpty();
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

    @Override public void add(int location, T o) {
        getList().add(location, o);
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
    public void remove(T item) {
        getList().remove(item);
    }

    @Override public void clear() {
        if (isEmpty()) {
            return;
        }
        getList().clear();
    }

    @Override public Fragment getItem(int position) {
        return getItemFragment(isEmpty() ? null : getList().get(position), position);
    }

    /**
     * @see FragmentStatePagerAdapter#getItem
     * @param item Current list item
     */
    public abstract Fragment getItemFragment(T item, int position);

}
