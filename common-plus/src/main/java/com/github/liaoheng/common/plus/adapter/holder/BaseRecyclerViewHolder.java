package com.github.liaoheng.common.plus.adapter.holder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.liaoheng.common.util.UIUtils;

/**
 * Base ViewHolder for RecyclerView
 *
 * @author liaoheng
 * @version 2015-10-27 17:20
 */
public class BaseRecyclerViewHolder<K> extends RecyclerView.ViewHolder implements IBaseViewHolder<K> {

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        if (itemView != null) return UIUtils.findViewById(itemView, id);
        return null;
    }

    @Override
    public Context getContext() {
        return itemView == null ? null : itemView.getContext();
    }

    @Override
    public void onHandle(K item) {
    }
}
