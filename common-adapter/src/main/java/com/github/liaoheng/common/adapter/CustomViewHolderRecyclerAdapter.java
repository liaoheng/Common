package com.github.liaoheng.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import com.github.liaoheng.common.adapter.base.BaseRecyclerAdapter;
import com.github.liaoheng.common.adapter.holder.BaseRecyclerViewHolder;
import java.util.List;

/**
 *  Custom ViewHolder for RecyclerAdapter
 * @author liaoheng
 * @version 2016-08-01 18:07
 */
public class CustomViewHolderRecyclerAdapter<K, H extends BaseRecyclerViewHolder>
        extends BaseRecyclerAdapter<K, H> {

    public interface RecyclerAdapterOperation<T, V extends BaseRecyclerViewHolder> {

        int getItemViewType(List<T> items, int position);

        @NonNull V createView(ViewGroup parent, int viewType);

        void onHandle(V holder, T item, int position);
    }

    public static abstract class EmptyRecyclerAdapterOperation<T, V extends BaseRecyclerViewHolder>
            implements RecyclerAdapterOperation<T, V> {

        @Override public int getItemViewType(List items, int position) {
            return 0;
        }
    }

    private RecyclerAdapterOperation<K, H> mOperation;

    public CustomViewHolderRecyclerAdapter(Context context,
                                           @NonNull RecyclerAdapterOperation<K, H> operation) {
        super(context);
        mOperation = operation;
    }

    public CustomViewHolderRecyclerAdapter(Context context, List<K> list,
                                           @NonNull RecyclerAdapterOperation<K, H> operation) {
        super(context, list);
        mOperation = operation;
    }

    @Override public int getItemViewType(int position) {
        return mOperation.getItemViewType(getList(), position);
    }

    @NonNull
    @Override public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mOperation.createView(parent, viewType);
    }

    @Override public void onBindViewHolderItem(@NonNull H holder, K item, int position) {
        mOperation.onHandle(holder, item, position);
    }
}
