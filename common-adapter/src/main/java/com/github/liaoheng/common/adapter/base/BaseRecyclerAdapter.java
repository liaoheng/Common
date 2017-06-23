package com.github.liaoheng.common.adapter.base;

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
public abstract class BaseRecyclerAdapter<T, V extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<V> implements IBaseRecyclerAdapter<T> {

    private Context mContext;
    private List<T> mList;
    private AtomicInteger mOldSize = new AtomicInteger(0);
    private OnItemClickListener<T> mOnItemClickListener;
    private OnItemLongClickListener<T> mOnItemLongClickListener;

    public BaseRecyclerAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public BaseRecyclerAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
    }

    public View inflate(@LayoutRes int resource, @NonNull ViewGroup root) {
        return inflate(resource, root, false);
    }

    public View inflate(@LayoutRes int resource, @NonNull ViewGroup root, boolean attachToRoot) {
        return LayoutInflater.from(getContext()).inflate(resource, root, attachToRoot);
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

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public List<T> getList() {
        return mList;
    }

    @Override
    public void setList(List<T> list) {
        mList = list;
    }

    @Override
    public boolean isEmpty() {
        return getList() == null || getList().isEmpty();
    }

    @SuppressWarnings("UnusedAssignment")
    @Override
    public void update(T item) {
        if (isEmpty()) {
            return;
        }
        for (T t : getList()) {
            if (t.equals(item)) {
                t = item;
                break;
            }
        }
    }

    @Override
    public void clear() {
        if (isEmpty()) {
            return;
        }
        getList().clear();
    }

    @Override
    public void addAll(List<T> list) {
        if (null == getList()) {
            setList(list);
        } else {
            getList().addAll(list);
        }
    }

    @Override
    public void addAll(int location, List<T> list) {
        if (null == getList()) {
            setList(list);
        } else {
            getList().addAll(location, list);
        }
    }

    @Override
    public void add(int location, T o) {
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

    @Override
    public abstract V onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(V holder, int position) {
        T item = getList().get(position);
        if (item == null) {
            onBindViewHolderItem(holder, null, position);
            return;
        }
        initOnItemClick(item, holder.itemView, position);
        initOnItemLongClick(item, holder.itemView, position);
        onBindViewHolderItem(holder, item, position);
    }

    /**
     * @param item Current list item
     * @see RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    public abstract void onBindViewHolderItem(V holder, T item, int position);

    @Override
    public int getItemCount() {
        return isEmpty() ? 0 : getList().size();
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @SuppressWarnings("WeakerAccess")
    protected void initOnItemClick(final T item, @NonNull View itemView, final int position) {
        if (mOnItemClickListener == null) {
            return;
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(item, v, position);
            }
        });
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @SuppressWarnings("WeakerAccess")
    protected void initOnItemLongClick(final T item,@NonNull View itemView, final int position) {
        if (mOnItemLongClickListener == null) {
            return;
        }
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mOnItemLongClickListener.onItemLongClick(item, v, position);
            }
        });
    }

    @Override
    public OnItemClickListener<T> getOnItemClickListener() {
        return mOnItemClickListener;
    }

    @Override
    public OnItemLongClickListener<T> getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }
}
