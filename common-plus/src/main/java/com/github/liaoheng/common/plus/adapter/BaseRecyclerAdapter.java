package com.github.liaoheng.common.plus.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.ViewGroup;

import com.github.liaoheng.common.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Base Adapter for RecyclerView
 *
 * @author liaoheng
 * @version 2015-05-04 10:41
 */
public abstract class BaseRecyclerAdapter<K, V extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<V> implements IBaseAdapter<K> {

    private Context mContext;
    private List<K> mList;
    private int oldSize;
    private OnItemClickListener<K> mOnItemClickListener;

    public View inflate(@LayoutRes int resource) {
        return UIUtils.inflate(getContext(), resource);
    }

    public View inflate(@LayoutRes int resource, ViewGroup root) {
        return UIUtils.inflate(getContext(), resource, root, false);
    }

    public View inflate(@LayoutRes int resource, ViewGroup root, boolean attachToRoot) {
        return UIUtils.inflate(getContext(), resource, root, attachToRoot);
    }

    public BaseRecyclerAdapter(Context context, List<K> list) {
        this.mContext = context;
        this.mList = list != null ? list : new ArrayList<K>();
    }

    public int getOldSize() {
        return oldSize;
    }

    public void setOldSize() {
        this.oldSize = getItemCount();
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
    public void update(List<K> list) {
        this.mList = list;
    }

    @Override
    public void clear() {
        this.mList = null;
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<K> list) {
        if (null == this.mList) {
            this.mList = list;
        } else {
            this.mList.addAll(list);
        }
    }

    @Override
    public void addAll(int index, List<K> list) {
        if (null == this.mList) {
            this.mList = list;
        } else {
            this.mList.addAll(index, list);
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

    @Override
    public abstract V onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(V holder, int position) {
        K item = getList().get(position);
        setOnItemClick(item, holder.itemView, position);
        onBindViewHolderItem(holder, item, position);
    }

    public abstract void onBindViewHolderItem(V holder, K k, int position);

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener<K> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    protected void setOnItemClick(final K item, View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(item, v, position);
                }
            }
        });
    }
}
