package com.github.liaoheng.common.plus.adapter;

import android.content.Context;
import android.view.View;
import java.util.Collection;
import java.util.List;

/**
 *  Base Adapter Interface
 * @author liaoheng
 * @version 2016-03-01 14:17
 */
public interface IBaseAdapter<K> {

    /**
     * Interface definition for a callback to be invoked when an item in this AdapterView has been
     * clicked.
     */
    interface OnItemClickListener<K> {

        /**
         * Callback method to be invoked when an item in this AdapterView has been clicked. <p>
         * Implementers can call getItemAtPosition(position) if they need to access the data
         * associated with the selected item.
         *
         * @param item Current list item
         * @param view The view within the AdapterView that was clicked (this will be a view
         *                 provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        void onItemClick(K item, View view, int position);
    }

    Context getContext();

    List<K> getList();

    void setList(List<K> list);

    boolean isEmpty();

    /**
     * update list
     */
    void update(List<K> list);

    /**
     * @see List#clear()
     */
    void clear();

    /**
     * @see List#addAll(Collection)
     */
    void addAll(List<K> list);

    /**
     * @see List#addAll(int, Collection)
     */
    void addAll(int location, List<K> list);

    /**
     * @see List#add(int, Object)
     */
    void add(int location, K item);

    /**
     * @see List#add(Object)
     */
    void add(K item);

    /**
     * @see List#remove(int)
     */
    void remove(int location);

    /**
     * @see List#remove(Object)
     */
    void remove(K item);
}
