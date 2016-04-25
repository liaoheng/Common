package com.github.liaoheng.common.plus.adapter.holder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.liaoheng.common.util.UIUtils;

/**
 * @author liaoheng
 * @version 2015-10-27 17:20
 */
public class RVBaseViewHolder extends RecyclerView.ViewHolder {

    public RVBaseViewHolder(View itemView) {
        super(itemView);
    }

    public <T extends View> T findViewById(@IdRes int id) {
        if (itemView != null)
            return UIUtils.findViewById(itemView, id);
        return null;
    }

    public Context getContext() {
        if (itemView == null)
            return null;
        return itemView.getContext();
    }

    public void recycle(){}
}
