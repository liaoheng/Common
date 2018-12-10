package com.github.liaoheng.common.adapter.internal;

/*
 * Copyright (C) 2014 darnmason
 * Copyright (C) 2016 liaoheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.View;
import android.view.ViewGroup;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <p>
 * RecyclerView adapter designed to wrap an existing adapter allowing the addition of
 * header views and footer views.
 * </p>
 * <p>
 * I implemented it to aid with the transition from ListView to RecyclerView where the ListView's
 * addHeaderView and addFooterView methods were used. Using this class you may initialize your
 * header views in the Fragment/Activity and add them to the adapter in the same way you used to
 * add them to a ListView.
 * </p>
 * <p>
 * I also required to be able to swap out multiple adapters with different content, therefore
 * setAdapter may be called multiple times.
 * </p>
 * Created by darnmason on 07/11/2014.
 * @author https://gist.github.com/darnmason/7bbf8beae24fe7296c8a
 * @author liaoheng
 * @version 2016-4-7
 */
public class HeaderViewRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADERS_START     = Integer.MIN_VALUE;
    private static final int FOOTERS_START     = Integer.MIN_VALUE + 10;
    private static final int ITEMS_START       = Integer.MIN_VALUE + 20;
    private static final int ADAPTER_MAX_TYPES = 100;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mWrappedAdapter;
    private List<View>           mHeaderViews, mFooterViews;
    private Map<Class, Integer>  mItemTypesOffset;

    public boolean hasHeader() {
        return mHeaderViews != null && !mHeaderViews.isEmpty();
    }

    public boolean hasFooter() {
        return mFooterViews != null && !mFooterViews.isEmpty();
    }

    /**
     * Construct a new header view recycler adapter
     */
    public HeaderViewRecyclerAdapter() {
        mHeaderViews = new LinkedList<>();
        mFooterViews = new LinkedList<>();
        mItemTypesOffset = new LinkedHashMap<>();
    }

    /**
     * Construct a new header view recycler adapter
     * @param adapter The underlying adapter to wrap
     */
    @SuppressWarnings("unchecked") public HeaderViewRecyclerAdapter(
            RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        this();
        setWrappedAdapter((RecyclerView.Adapter<RecyclerView.ViewHolder>) adapter);
    }

    /**
     * Replaces the underlying adapter, notifying RecyclerView of changes
     * @param adapter The new adapter to wrap
     */
    @SuppressWarnings("unchecked") public void setAdapter(
            RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        if (mWrappedAdapter != null && mWrappedAdapter.getItemCount() > 0) {
            notifyItemRangeRemoved(getHeaderCount(), mWrappedAdapter.getItemCount());
        }
        setWrappedAdapter((RecyclerView.Adapter<RecyclerView.ViewHolder>) adapter);
        notifyItemRangeInserted(getHeaderCount(), mWrappedAdapter.getItemCount());
    }

    public boolean isHeader(int position) {
        return hasHeader() && position < getHeaderCount();
    }

    public boolean isFooter(int position) {
        return hasFooter() &&  position >= (mWrappedAdapter.getItemCount()+getHeaderCount());
    }

    @Override
    public int getItemViewType(int position) {
        int itemCount = mWrappedAdapter.getItemCount();
        if (isHeader(position)) {
            return HEADERS_START + position;
        }else if (isFooter(position)){
            return FOOTERS_START+(position - getHeaderCount()- itemCount );
        }else{
            return mWrappedAdapter.getItemViewType(position - getHeaderCount());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType <=HEADERS_START + getHeaderCount()){
            return new StaticViewHolder(mHeaderViews.get(viewType - HEADERS_START));
        }else if (viewType <= FOOTERS_START + getFooterCount()){
            return new StaticViewHolder(mFooterViews.get(viewType - FOOTERS_START));
        }else{
            return mWrappedAdapter.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (isFooter(position) || isHeader(position)){
            return;
        }
        mWrappedAdapter.onBindViewHolder(viewHolder, position - getHeaderCount());
    }
    /**
     * Add a static view to appear at the start of the RecyclerView. Headers are displayed in the
     * order they were added.
     * @param views The header view to add all
     */
    public void addHeaderView(List<View> views) {
        if (views == null)
            return;
        mHeaderViews.addAll(views);
    }

    /**
     * Add a static view to appear at the end of the RecyclerView. Footers are displayed in the
     * order they were added.
     * @param views The footer view to add all
     */
    public void addFooterView(List<View> views) {
        if (views == null)
            return;
        mFooterViews.addAll(views);
    }

    /**
     * Add a static view to appear at the start of the RecyclerView. Headers are displayed in the
     * order they were added.
     * @param view The header view to add
     */
    public void addHeaderView(View view) {
        if (view == null)
            return;
        mHeaderViews.add(view);
    }

    /**
     * Add a static view to appear at the end of the RecyclerView. Footers are displayed in the
     * order they were added.
     * @param view The footer view to add
     */
    public void addFooterView(View view) {
        if (view == null)
            return;
        mFooterViews.add(view);
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getFooterCount() + getWrappedItemCount();
    }

    /**
     * @return The item count in the underlying adapter
     */
    public int getWrappedItemCount() {
        return mWrappedAdapter.getItemCount();
    }

    /**
     * @return The number of header views added
     */
    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    /**
     * @return The number of footer views added
     */
    public int getFooterCount() {
        return mFooterViews.size();
    }

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getWrappedAdapter() {
        return mWrappedAdapter;
    }

    /**
     * @return header views
     */
    public List<View> getHeaderViews() {
        return mHeaderViews;
    }

    /**
     * @return footer views
     */
    public List<View> getFooterViews() {
        return mFooterViews;
    }

    private void setWrappedAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        if (mWrappedAdapter != null)
            mWrappedAdapter.unregisterAdapterDataObserver(mDataObserver);
        mWrappedAdapter = adapter;
        Class adapterClass = mWrappedAdapter.getClass();
        if (!mItemTypesOffset.containsKey(adapterClass))
            putAdapterTypeOffset(adapterClass);
        mWrappedAdapter.registerAdapterDataObserver(mDataObserver);
    }

    private void putAdapterTypeOffset(Class adapterClass) {
        mItemTypesOffset.put(adapterClass,
                ITEMS_START + mItemTypesOffset.size() * ADAPTER_MAX_TYPES);
    }

    private int getAdapterTypeOffset() {
        return mItemTypesOffset.get(mWrappedAdapter.getClass());
    }

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + getHeaderCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + getHeaderCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + getHeaderCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int hCount = getHeaderCount();
            notifyItemRangeChanged(fromPosition + hCount, toPosition + hCount + itemCount);
        }
    };

    private static class StaticViewHolder extends RecyclerView.ViewHolder {

        public StaticViewHolder(View itemView) {
            super(itemView);
        }
    }
}