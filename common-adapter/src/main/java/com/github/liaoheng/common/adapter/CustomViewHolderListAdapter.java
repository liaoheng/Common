package com.github.liaoheng.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.github.liaoheng.common.adapter.base.BaseListAdapter;
import com.github.liaoheng.common.adapter.holder.BaseListViewHolder;
import java.util.List;

/**
 *  Custom ViewHolder for ListAdapter
 * @author liaoheng
 * @version 2016-10-8 13:45
 */
public class CustomViewHolderListAdapter<K, VH extends BaseListViewHolder>
        extends BaseListAdapter<K> {

    public interface ListAdapterOperation<T, V extends BaseListViewHolder> {

        int getItemViewType(List<T> items, int position);

        @NonNull V createView(ViewGroup parent, int viewType);

        void onHandle(V holder, T item, int position);
    }

    public static abstract class EmptyListAdapterOperation<T, V extends BaseListViewHolder>
            implements ListAdapterOperation<T, V> {
        @Override public int getItemViewType(List items, int position) {
            return 0;
        }
    }

    protected ListAdapterOperation mOperation;

    public CustomViewHolderListAdapter(Context context, ListAdapterOperation operation) {
        super(context);
        mOperation = operation;
    }

    public CustomViewHolderListAdapter(Context context, List<K> list,
                                       ListAdapterOperation operation) {
        super(context, list);
        mOperation = operation;
    }

    @SuppressWarnings("unchecked") @Override public View getItemView(K item, int position,
                                                                     View convertView,
                                                                     ViewGroup parent) {
        VH holder;
        if (convertView == null) {
            holder = (VH) mOperation
                    .createView(parent, mOperation.getItemViewType(getList(), position));
            holder.setTag();
        } else {
            holder = BaseListViewHolder.getTag(convertView);
        }
        mOperation.onHandle(holder, item, position);
        return holder.getItemView();
    }
}
