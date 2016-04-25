package com.github.liaoheng.common.plus.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.ViewGroup;

import com.github.liaoheng.common.util.UIUtils;

/**
 * {@link FragmentStatePagerAdapter}
 * @author liaoheng
 * @version 2015-11-27 14:56:32
 */
public abstract class BaseFragmentStatePagerAdapter<K> extends FragmentStatePagerAdapter
                                                   implements IBaseAdapter<K> {

    private Context mContext;
    private List<K> mList;

    public BaseFragmentStatePagerAdapter(FragmentManager fm, Context context, List<K> list) {
        super(fm);
        this.mContext = context;
        this.mList = list != null ? list : new ArrayList<K>();
    }

    @Override
    public View inflate(@LayoutRes int resource) {
        return UIUtils.inflate(getContext(), resource);
    }

    @Override
    public View inflate(@LayoutRes int resource, ViewGroup root) {
        return UIUtils.inflate(getContext(), resource, root,false);
    }

    @Override
    public View inflate(@LayoutRes int resource, ViewGroup root, boolean attachToRoot) {
        return UIUtils.inflate(getContext(), resource, root, attachToRoot);
    }

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
    public List<K> getList() {
        return mList;
    }

    @Override
    public void update(List<K> mList) {
        this.mList = mList;
    }

    @Override
    public void addAll(List<K> mList) {
        if (null == this.mList) {
            this.mList = mList;
        } else {
            this.mList.addAll(mList);
        }
    }

    @Override
    public void addAll(int index, List<K> mList) {
        if (null == this.mList) {
            this.mList = mList;
        } else {
            this.mList.addAll(index, mList);
        }
    }

    @Override
    public void add(int index, K o) {
        if (null == this.mList) {
            throw new AndroidRuntimeException("add list is null");
        }
        this.mList.add(index, o);
    }

    @Override
    public void add(K o) {
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
    public void remove(K item) {
        if (null == item) {
            throw new AndroidRuntimeException("remove list is null");
        }
        this.mList.remove(item);
    }
}
