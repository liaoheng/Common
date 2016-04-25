package com.github.liaoheng.common.plus.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.ViewGroup;

import com.github.liaoheng.common.plus.adapter.holder.RVBaseViewHolder;
import com.github.liaoheng.common.util.UIUtils;

/**
 * RecyclerView列表基础通用适配器
 * @author liaoheng
 * @version 2015-05-04 10:41
 */
public abstract class RVBaseListAdapter<K, V extends RecyclerView.ViewHolder>
                                       extends RecyclerView.Adapter<V>implements IBaseAdapter<K> {

    private Context                mContext;
    private List<K>                mList;
    private int                    oldSize;
    private OnItemClickListener<K> mOnItemClickListener;

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

    /**
     * list适配器
     * @param mContext {@link Context}
     * @param list 为空时使用ArrayList
     */
    public RVBaseListAdapter(Context mContext, List<K> list) {
        this.mContext = mContext;
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
    public void onViewRecycled(V holder) {
        if (holder instanceof RVBaseViewHolder) {
            ((RVBaseViewHolder) holder).recycle();
        }else{
            super.onViewRecycled(holder);
        }
    }

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
