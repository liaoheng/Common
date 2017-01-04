package com.github.liaoheng.common.adapter.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import com.github.liaoheng.common.adapter.base.BaseListAdapter;
import com.github.liaoheng.common.adapter.base.BaseRecyclerAdapter;
import com.github.liaoheng.common.adapter.base.IBaseAdapter;

/**
 * @author liaoheng
 * @version 2016-12-19 16:13
 */
public class ListLinearLayout<T> extends LinearLayoutCompat {

    private final String TAG = ListLinearLayout.class.getSimpleName();

    public ListLinearLayout(Context context) {
        super(context);
        init();
    }

    public ListLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayoutCompat.VERTICAL);
    }

    private void checkNull(String hint, Object o) {
        if (o == null) {
            throw new NullPointerException(hint);
        }
    }

    private ListAdapter                                   mSystemListAdapter;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mSystemRecyclerAdapter;

    private DataSetObserver mSystemDataSetObserver = new DataSetObserver() {
        @Override public void onChanged() {
            Log.i(TAG, " ListView.DataSetObserver onChanged");
            updateDataAdapter(mSystemListAdapter);
        }
    };

    private RecyclerView.AdapterDataObserver mSystemDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override public void onChanged() {
            Log.i(TAG, " RecyclerView.AdapterDataObserver onChanged");
            updateDataAdapter(mSystemRecyclerAdapter);
        }
    };

    public void setAdapter(ListAdapter adapter) {
        checkNull("ListAdapter is null", adapter);
        release();
        mSystemListAdapter = adapter;
        mSystemListAdapter.registerDataSetObserver(mSystemDataSetObserver);
        updateDataAdapter(mSystemListAdapter);
    }

    private void updateDataAdapter(ListAdapter adapter) {
        removeAllViews();
        if (adapter.isEmpty()) {
            return;
        }
        for (int position = 0; position < adapter.getCount(); position++) {
            View view = adapter.getView(position, null, this);
            addView(view);
        }
    }

    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        checkNull("RecyclerView.Adapter is null", adapter);
        release();
        mSystemRecyclerAdapter = adapter;
        mSystemRecyclerAdapter.registerAdapterDataObserver(mSystemDataObserver);
        updateDataAdapter(mSystemRecyclerAdapter);
    }

    private void updateDataAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        removeAllViews();
        if (adapter.getItemCount() == 0) {
            return;
        }
        for (int position = 0; position < adapter.getItemCount(); position++) {
            RecyclerView.ViewHolder viewHolder = adapter
                    .onCreateViewHolder(this, adapter.getItemViewType(position));
            adapter.onBindViewHolder(viewHolder, position);
            addView(viewHolder.itemView);
        }
    }

    private BaseListAdapter<T>                                        mListAdapter;
    private BaseRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> mRecyclerAdapter;
    private IBaseAdapter.OnItemClickListener<T>                       mOnItemClickListener;

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override public void onChanged() {
            Log.i(TAG, " BaseListAdapter.DataSetObserver onChanged");
            updateData(mListAdapter);
        }
    };

    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override public void onChanged() {
            Log.i(TAG, " BaseRecyclerAdapter.AdapterDataObserver onChanged");
            updateData(mRecyclerAdapter);
        }
    };

    public void setAdapter(BaseListAdapter<T> adapter) {
        setAdapter(adapter, null);
    }

    public void setAdapter(BaseRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> adapter) {
        setAdapter(adapter, null);
    }

    public void setAdapter(BaseListAdapter<T> adapter,
                           IBaseAdapter.OnItemClickListener<T> onItemClickListener) {
        checkNull("BaseListAdapter is null", adapter);
        release();
        mOnItemClickListener = onItemClickListener;
        mListAdapter = adapter;
        mListAdapter.registerDataSetObserver(dataSetObserver);
        updateData(mListAdapter);
    }

    public void setAdapter(BaseRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> adapter,
                           IBaseAdapter.OnItemClickListener<T> onItemClickListener) {
        checkNull("BaseRecyclerAdapter is null", adapter);
        release();
        mOnItemClickListener = onItemClickListener;
        mRecyclerAdapter = adapter;
        mRecyclerAdapter.registerAdapterDataObserver(dataObserver);
        updateData(mRecyclerAdapter);
    }

    private void updateData(IBaseAdapter<T> adapter) {
        removeAllViews();
        if (adapter.isEmpty()) {
            return;
        }
        for (int position = 0; position < adapter.getList().size(); position++) {
            final T item = adapter.getList().get(position);
            View view = new View(getContext());
            if (adapter instanceof BaseListAdapter) {
                view = ((BaseListAdapter<T>) adapter).getItemView(item, position, null, this);
            }
            if (adapter instanceof BaseRecyclerAdapter) {
                BaseRecyclerAdapter<T, RecyclerView.ViewHolder> recyclerAdapter = (BaseRecyclerAdapter<T, RecyclerView.ViewHolder>) adapter;
                RecyclerView.ViewHolder viewHolder = recyclerAdapter
                        .onCreateViewHolder(this, recyclerAdapter.getItemViewType(position));
                recyclerAdapter.onBindViewHolderItem(viewHolder, item, position);
                view = viewHolder.itemView;
            }
            addView(view);
            final int finalPosition = position;
            if (this.mOnItemClickListener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override public void onClick(View v) {
                        mOnItemClickListener.onItemClick(item, v, finalPosition);
                    }
                });
            }
        }
    }

    private void release() {
        if (mListAdapter != null) {
            mListAdapter.unregisterDataSetObserver(dataSetObserver);
            mListAdapter = null;
        }
        if (mRecyclerAdapter != null) {
            mRecyclerAdapter.unregisterAdapterDataObserver(dataObserver);
            mRecyclerAdapter = null;
        }
        if (mSystemListAdapter != null) {
            mSystemListAdapter.unregisterDataSetObserver(mSystemDataSetObserver);
            mSystemListAdapter = null;
        }
        if (mSystemRecyclerAdapter != null) {
            mSystemRecyclerAdapter.unregisterAdapterDataObserver(mSystemDataObserver);
            mSystemRecyclerAdapter = null;
        }
    }
}
