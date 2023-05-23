package com.github.liaoheng.common.adapter.core;


import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView拖拽简单支持，参考：https://blog.yorek.xyz/android/other/RecyclerView-Sort&Delete
 *
 * @author liaoheng
 * @date 2022-08-15 17:05
 */
public class DragSwipeCallback extends ItemTouchHelper.Callback {
    private final String TAG = this.getClass().getSimpleName();
    private int translationZ = 4;
    private boolean dragEnabled;
    private int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;

    /**
     * 通过此变量通知外界发生了排序、删除等操作
     */
    private final IDragSwipe mAdapter;

    public DragSwipeCallback(IDragSwipe adapter) {
        mAdapter = adapter;
    }

    public void setTranslationZ(int translationZ) {
        this.translationZ = translationZ;
    }

    public void setDragEnabled(boolean dragEnabled) {
        this.dragEnabled = dragEnabled;
    }

    public void setDragFlags(int dragFlags) {
        this.dragFlags = dragFlags;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // dragFlags:拖拽方向，swipeFlags:滑动方向
        return makeMovementFlags(dragFlags, 0);
    }

    //长按拖拽开关
    @Override
    public boolean isLongPressDragEnabled() {
        return dragEnabled;
    }

    //滑动开关
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    /**
     * 拖拽、交换事件
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
            RecyclerView.ViewHolder target) {
        Log.d(TAG, "onMove");
        mAdapter.onItemSwapped(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * 滑动成功的事件
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d(TAG, "onSwiped: " + direction);
    }

    /**
     * 拖拽、滑动时如何绘制列表
     * actionState只会为ACTION_STATE_DRAG或者ACTION_STATE_SWIPE
     */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
            int actionState, boolean isCurrentlyActive) {
        Log.d(TAG, "actionState: " + actionState);
        switch (actionState) {
            case ItemTouchHelper.ACTION_STATE_DRAG:
                // 拖拽时，如果是isCurrentlyActive，则设置translationZ，否则复位
                viewHolder.itemView.setTranslationZ(isCurrentlyActive ? translationZ : 0);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                break;
            case ItemTouchHelper.ACTION_STATE_SWIPE:
                // 滑动时，对view的绘制
                break;
            default:
        }
    }

    /**
     * 在onSelectedChanged、onChildDraw、onChildDrawOver操作完成后可以在此进行清楚操作
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        Log.d(TAG, "clearView");
    }
}
