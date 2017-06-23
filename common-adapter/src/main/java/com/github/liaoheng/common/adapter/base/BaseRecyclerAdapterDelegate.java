package com.github.liaoheng.common.adapter.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates2.AdapterDelegate;

import java.util.List;

/**
 * A simplified {@link AdapterDelegate} when the underlying adapter's dataset is a  {@linkplain
 * List}. This class helps to reduce writing boilerplate code like casting list item and casting
 * ViewHolder.
 *
 * <p>
 * For instance if you have a list of animals (different kind of animals in classes like Cat, Dog
 * etc. assuming all have a common super class Animal) you want to display in your adapter and
 * you want to create a CatAdapterDelegate then this class would look like this:
 * {@code
 * class CatAdapterDelegate extends BaseRecyclerAdapterDelegate<Cat, Animal, CatViewHolder>{
 *
 * @param <I> The type of the item that is managed by this AdapterDelegate. Must be a subtype of T
 * @param <T> The generic type of the list, in other words: {@code List<T>}
 * @param <VH> The type of the ViewHolder
 * @author Hannes Dorfmann
 * @author liaoheng
 * @version 2016-09-23 11:44
 * @Override protected boolean isForViewType(Animal item, List<Animal> items, position){
 * return item instanceof Cat;
 * }
 * @Override public CatViewHolder onCreateViewHolder(ViewGroup parent){
 * return new CatViewHolder(inflater.inflate(R.layout.item_cat, parent, false));
 * }
 * @Override protected void onBindViewHolder(Cat item, CatViewHolder viewHolder);
 * viewHolder.setName(cat.getName());
 * ...
 * }
 * }
 *
 * }
 * </p>
 * adapter委托实现,修改于<a href="https://github.com/sockeqwe/AdapterDelegates/blob/2.0.1/library/src/main/java/com/hannesdorfmann/adapterdelegates2/AbsListItemAdapterDelegate.java">AbsListItemAdapterDelegate</a>
 */
public abstract class BaseRecyclerAdapterDelegate<I extends T, T, VH extends RecyclerView.ViewHolder>
        implements AdapterDelegate<List<T>> ,IBaseRecyclerAdapter<I> {
    private Context mContext;
    private IBaseAdapter.OnItemClickListener<I> mOnItemClickListener;
    private IBaseAdapter.OnItemLongClickListener<I> mOnItemLongClickListener;

    public BaseRecyclerAdapterDelegate(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public List<I> getList() {
        return null;
    }

    @Override
    public void setList(List<I> list) {

    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void update(I item) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void addAll(List<I> list) {

    }

    @Override
    public void addAll(int location, List<I> list) {

    }

    @Override
    public void add(int location, I item) {

    }

    @Override
    public void add(I item) {

    }

    @Override
    public void remove(int location) {

    }

    @Override
    public void remove(I item) {

    }

    public View inflate(@LayoutRes int resource, @NonNull ViewGroup root) {
        return inflate(resource, root, false);
    }

    public View inflate(@LayoutRes int resource, @NonNull ViewGroup root, boolean attachToRoot) {
        return LayoutInflater.from(getContext()).inflate(resource, root, attachToRoot);
    }

    @Override
    public final boolean isForViewType(@NonNull List<T> items, int position) {
        return isForViewType(items.get(position), items, position);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(
            @NonNull List<T> items, final int position,
            @NonNull final RecyclerView.ViewHolder holder) {
        I item = (I) items.get(position);
        initOnItemClick(item, holder.itemView, position);
        initOnItemLongClick(item,holder.itemView,position);
        onBindViewHolderItem((VH) holder, item, position);
    }

    protected void initOnItemClick(final I item,View itemView, final int position) {
        if (mOnItemClickListener == null) {
            return;
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(item, v, position);
            }
        });
    }

    protected void initOnItemLongClick(final I item, View itemView, final int position) {
        if (mOnItemLongClickListener == null) {
            return;
        }
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                return mOnItemLongClickListener.onItemLongClick(item, v, position);
            }
        });
    }

    @Override
    public void setOnItemClickListener(IBaseAdapter.OnItemClickListener<I> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener<I> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public IBaseAdapter.OnItemClickListener<I> getOnItemClickListener() {
        return mOnItemClickListener;
    }

    @Override
    public OnItemLongClickListener<I> getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    /**
     * Called to determine whether this AdapterDelegate is the responsible for the given item in the list or not
     * element
     *
     * @param item The item from the list at the given position
     * @param items The items from adapters dataset
     * @param position The items position in the dataset (list)
     * @return true if this AdapterDelegate is responsible for that, otherwise false
     */
    public abstract boolean isForViewType(@NonNull T item, List<T> items, int position);

    /**
     * @param item Current list item
     * @see RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    public abstract void onBindViewHolderItem(VH holder, I item, int position);

}
