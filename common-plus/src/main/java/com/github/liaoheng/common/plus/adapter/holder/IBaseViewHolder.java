package com.github.liaoheng.common.plus.adapter.holder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * @author liaoheng
 * @version 2016-06-13 18:31
 */
public interface IBaseViewHolder<K> {
    void onHandle(K item);

    <T extends View> T findViewById(@IdRes int id);

    Context getContext();
}
