package com.github.liaoheng.common.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private AtomicInteger mOldSize = new AtomicInteger(0);
    private OnItemClickListener<K> mOnItemClickListener;

    public BaseRecyclerAdapter(Context context) {
        this(context, new ArrayList<K>());
    }

    public BaseRecyclerAdapter(Context context, List<K> list) {
        mContext = context;
        mList = list;
    }

    public View inflate(@LayoutRes int resource, @NonNull ViewGroup root) {
        return inflate(resource, root, false);
    }

    public View inflate(@LayoutRes int resource, @NonNull ViewGroup root, boolean attachToRoot) {
        return LayoutInflater.from(getContext()).inflate(resource,root,attachToRoot);
    }

    public int getOldSize() {
        return mOldSize.get();
    }

    public void setOldSize() {
        mOldSize.set(getItemCount());
    }

    public void setOldSize(int size) {
        mOldSize.set(size);
    }

    @Override public Context getContext() {
        return mContext;
    }

    @Override public List<K> getList() {
        return mList;
    }

    @Override public void setList(List<K> list) {
        mList = list;
    }

    @Override public boolean isEmpty() {
        return getList() == null || getList().isEmpty();
    }

    @Override public void update(List<K> list) {
        setList(list);
    }

    @Override public void clear() {
        if (isEmpty()) {
            return;
        }
        getList().clear();
    }

    @Override public void addAll(List<K> list) {
        if (null == getList()) {
            setList(list);
        } else {
            getList().addAll(list);
        }
    }

    @Override public void addAll(int location, List<K> list) {
        if (null == getList()) {
            setList(list);
        } else {
            getList().addAll(location, list);
        }
    }

    @Override public void add(int location, K o) {
        getList().add(location, o);
    }

    @Override public void add(K o) {
        getList().add(o);
    }

    @Override public void remove(int location) {
        getList().remove(location);
    }

    @Override public void remove(K item) {
        getList().remove(item);
    }

    @Override public abstract V onCreateViewHolder(ViewGroup parent, int viewType);

    @Override public void onBindViewHolder(V holder, int position) {
        K item = getList().get(position);
        setOnItemClick(item, holder.itemView, position);
        onBindViewHolderItem(holder, item, position);
    }

    /**
     * @see RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)
     * @param item  Current list item
     */
    public abstract void onBindViewHolderItem(V holder, K item, int position);

    @Override public int getItemCount() {
        return isEmpty() ? 0 : getList().size();
    }

    public void setOnItemClickListener(OnItemClickListener<K> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    protected void setOnItemClick(final K item, View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(item, v, position);
                }
            }
        });
    }
}
