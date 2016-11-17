package com.github.liaoheng.common.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.hannesdorfmann.adapterdelegates2.AdapterDelegate;
import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager;
import java.util.List;

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
 * adapter委托,修改于<a href="https://github.com/sockeqwe/AdapterDelegates/blob/2.0.1/library/src/main/java/com/hannesdorfmann/adapterdelegates2/AbsDelegationAdapter.java">AbsDelegationAdapter</a>
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

    @Override public int getItemViewType(int position) {
        return mAdapterDelegatesManager.getItemViewType(getList(), position);
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mAdapterDelegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mAdapterDelegatesManager.onBindViewHolder(getList(), position, holder);
    }

    @Override public void onBindViewHolderItem(RecyclerView.ViewHolder holder, T item,
                                               int position) {
    }
}
