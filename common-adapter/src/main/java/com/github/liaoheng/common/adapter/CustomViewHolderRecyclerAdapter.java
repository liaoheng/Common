package com.github.liaoheng.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import com.github.liaoheng.common.adapter.base.BaseRecyclerAdapter;
import com.github.liaoheng.common.adapter.holder.BaseRecyclerViewHolder;
import java.util.List;

/**
 * Custom ViewHolder for RecyclerAdapter
 * @author liaoheng
 * @version 2016-08-01 18:07
 */
public class CustomViewHolderRecyclerAdapter<K, H extends BaseRecyclerViewHolder>
        extends BaseRecyclerAdapter<K, H> {
    public interface RecyclerAdapterOperation<T, V extends BaseRecyclerViewHolder> {
        V createView(ViewGroup parent, int viewType);

        void onHandle(V holder, T item, int position);
    }

    private RecyclerAdapterOperation<K, H> mOperation;

    public CustomViewHolderRecyclerAdapter(Context context,
                                           @NonNull RecyclerAdapterOperation<K, H> mOperation) {
        super(context);
        this.mOperation = mOperation;
    }

    public CustomViewHolderRecyclerAdapter(Context context, List<K> list,
                                           @NonNull RecyclerAdapterOperation<K, H> mOperation) {
        super(context, list);
        this.mOperation = mOperation;
    }

    @Override public H onCreateViewHolder(ViewGroup parent, int viewType) {
        return mOperation.createView(parent, viewType);
    }

    @Override public void onBindViewHolderItem(H holder, K item, int position) {
        mOperation.onHandle(holder, item, position);
    }
}
