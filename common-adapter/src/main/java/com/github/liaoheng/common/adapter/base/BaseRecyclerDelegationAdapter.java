package com.github.liaoheng.common.adapter.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager;

import java.lang.reflect.Field;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An implementation of an Adapter that already uses a {@link AdapterDelegatesManager} and calls
 * the corresponding {@link AdapterDelegatesManager} methods from Adapter's method like {@link
 * #onCreateViewHolder(ViewGroup, int)}, {@link #onBindViewHolder(RecyclerView.ViewHolder, int)}
 * and {@link #getItemViewType(int)}. So everything is already setup for you. You just have to add
 * the {@link AdapterDelegate}s i.e. in the constructor of a subclass that inheritance from this
 * class:
 * <pre>
 * {@code
 *    class MyAdapter extends BaseRecyclerDelegationAdapter<MyDataSourceType>{
 *        public MyAdaper(){
 *            this.delegatesManager.add(new FooAdapterDelegate());
 *            this.delegatesManager.add(new BarAdapterDelegate());
 *        }
 *    }
 * }
 * </pre>
 *
 * or you can pass a already prepared {@link AdapterDelegatesManager} via constructor like this:
 * <pre>
 * {@code
 *    class MyAdapter extends BaseRecyclerDelegationAdapter<MyDataSourceType>{
 *        public MyAdapter(AdapterDelegatesManager manager){
 *          super(manager)
 *        }
 *    }
 * }
 * </pre>
 *
 * adapter委托,修改于<a href="https://github.com/sockeqwe/AdapterDelegates/blob/4.0.0/library/src/main/java/com/hannesdorfmann/adapterdelegates4/AbsListItemAdapterDelegate.java">AbsDelegationAdapter</a>
 *
 * @author Hannes Dorfmann
 * @author liaoheng
 * @version 2016-09-23 11:15
 */
public class BaseRecyclerDelegationAdapter<T> extends BaseRecyclerAdapter<T, RecyclerView.ViewHolder> {

    private AdapterDelegatesManager<List<T>> mAdapterDelegatesManager;

    public AdapterDelegatesManager<List<T>> getAdapterDelegatesManager() {
        return mAdapterDelegatesManager;
    }

    public BaseRecyclerDelegationAdapter(Context context) {
        super(context);
        mAdapterDelegatesManager = new AdapterDelegatesManager<>();
    }

    public BaseRecyclerDelegationAdapter(Context context, List<T> list) {
        super(context, list);
        mAdapterDelegatesManager = new AdapterDelegatesManager<>();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapterDelegatesManager.getItemViewType(getList(), position);
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("LongLogTag")
    private SparseArrayCompat<AdapterDelegate<T>> getDelegates() {
        try {
            Field delegatesField = getAdapterDelegatesManager().getClass().getDeclaredField("delegates");
            delegatesField.setAccessible(true);
            return (SparseArrayCompat<AdapterDelegate<T>>) delegatesField.get(getAdapterDelegatesManager());
        } catch (Exception e) {
            Log.w("BaseRecyclerDelegationAdapter", e);
        }
        return null;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (getDelegates() != null) {
            //TODO 可以优化一下
            //为各代理Adapter设置点击与长按事件回调
            for (int i = 0; i < getDelegates().size(); i++) {
                IBaseRecyclerAdapter<T> adapter = (IBaseRecyclerAdapter<T>) getDelegates().get(i);

                IBaseRecyclerAdapter<T> fallbackDelegate = null;
                AdapterDelegate<List<T>> fallback = mAdapterDelegatesManager.getFallbackDelegate();
                if (fallback != null) {
                    if (fallback instanceof IBaseRecyclerAdapter) {
                        fallbackDelegate = (IBaseRecyclerAdapter<T>) fallback;
                    }
                }
                if (getOnItemClickListener() != null) {
                    adapter.setOnItemClickListener(getOnItemClickListener());
                    if (fallbackDelegate != null) {
                        fallbackDelegate.setOnItemClickListener(getOnItemClickListener());
                    }
                }
                if (getOnItemLongClickListener() != null) {
                    adapter.setOnItemLongClickListener(getOnItemLongClickListener());
                    if (fallbackDelegate != null) {
                        fallbackDelegate.setOnItemLongClickListener(getOnItemLongClickListener());
                    }
                }
            }
        }
        return mAdapterDelegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        mAdapterDelegatesManager.onBindViewHolder(getList(), position, holder);
    }

    @Override
    public void onBindViewHolderItem(@NonNull RecyclerView.ViewHolder holder, T item,
            int position) {
    }
}
