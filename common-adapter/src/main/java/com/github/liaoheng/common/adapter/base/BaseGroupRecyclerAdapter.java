package com.github.liaoheng.common.adapter.base;

import android.content.Context;
import android.view.ViewGroup;

import com.github.liaoheng.common.adapter.holder.BaseRecyclerViewHolder;
import com.github.liaoheng.common.adapter.model.Group;

import java.util.List;

/**
 * @author liaoheng
 * @version 2017-01-25 09:42
 */
public abstract class BaseGroupRecyclerAdapter<K>
        extends BaseRecyclerAdapter<Group<K>, BaseRecyclerViewHolder<K>> {

    public BaseGroupRecyclerAdapter(Context context) {
        super(context);
    }

    public BaseGroupRecyclerAdapter(Context context, List<Group<K>> list) {
        super(context, list);
    }

    @Override public int getItemViewType(int position) {
        return getList().get(position).getType().getCode();
    }

    @Override public BaseRecyclerViewHolder<K> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Group.GroupType.HEADER.getCode()) {
            return onCreateGroupHeaderViewHolder(parent, viewType);
        } else if (viewType == Group.GroupType.FOOTER.getCode()) {
            return onCreateGroupFooterViewHolder(parent, viewType);
        } else {
            return onCreateGroupContentViewHolder(parent, viewType);
        }
    }

    @Override public void onBindViewHolder(BaseRecyclerViewHolder<K> holder, int position) {
        Group<K> item = getList().get(position);
        if (item == null) {
            onBindViewHolderItem(holder, null, position);
            return;
        }
        if (Group.GroupType.CONTENT.equals(item.getType())){
            initOnItemClick(item, holder.itemView, position);
            initOnItemLongClick(item, holder.itemView, position);
        }
        onBindViewHolderItem(holder, item, position);
    }

    @Override public void onBindViewHolderItem(BaseRecyclerViewHolder<K> holder, Group<K> item,
                                               int position) {
        holder.onHandle(item.getContent(), position, item);
    }

    public abstract BaseRecyclerViewHolder<K> onCreateGroupHeaderViewHolder(ViewGroup parent,
                                                                            int viewType);

    public abstract BaseRecyclerViewHolder<K> onCreateGroupFooterViewHolder(ViewGroup parent,
                                                                            int viewType);

    public abstract BaseRecyclerViewHolder<K> onCreateGroupContentViewHolder(ViewGroup parent,
                                                                             int viewType);
}


