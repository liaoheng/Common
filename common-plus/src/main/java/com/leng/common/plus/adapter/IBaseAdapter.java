package com.leng.common.plus.adapter;

import java.util.List;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author liaoheng
 * @version 2016-03-01 14:17
 */
public interface IBaseAdapter<K> {

    abstract class EmptyBaseAdapter<K> implements IBaseAdapter<K> {
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * AdapterView has been clicked.
     */
    interface OnItemClickListener<K> {

        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param view The view within the AdapterView that was clicked (this
         *            will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        void onItemClick(K item, View view, int position);
    }

    View inflate(@LayoutRes int resource);

    /**
     *  attachToRoot default false
     */
    View inflate(@LayoutRes int resource, ViewGroup root);

    View inflate(@LayoutRes int resource, ViewGroup root, boolean attachToRoot);

    Context getContext();

    List<K> getList();

    void update(List<K> list);

    void clear();

    void addAll(List<K> list);

    void addAll(int index, List<K> list);

    void add(int index, K item);

    void add(K item);

    void remove(int location);

    void remove(K item);
}
