package com.github.liaoheng.common.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.github.liaoheng.common.adapter.holder.IBaseViewHolder;
import java.util.List;

/**
 * Base Adapter for RecyclerView
 *  to use {@link IBaseViewHolder#onHandle(Object, int, Object) } or {@link IBaseViewHolder#onHandle(Object, int)}
 * @author liaoheng
 * @version 2017-2-10
 */
public abstract class BaseHandleRecyclerAdapter<K, V extends RecyclerView.ViewHolder>
        extends BaseRecyclerAdapter<K, V> {

    public BaseHandleRecyclerAdapter(Context context) {
        super(context);
    }

    public BaseHandleRecyclerAdapter(Context context, List<K> list) {
        super(context, list);
    }

    /**
     * @see RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)
     * @param item  Current list item
     */
    @SuppressWarnings("unchecked") public void onBindViewHolderItem(@NonNull V holder, K item,
                                                                    int position) {
        if (holder instanceof IBaseViewHolder) {
            ((IBaseViewHolder) holder).onHandle(item, position, null);
        }
    }
}
