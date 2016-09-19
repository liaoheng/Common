package com.github.liaoheng.common.plus.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.liaoheng.common.adapter.base.BaseListAdapter;
import com.github.liaoheng.common.adapter.holder.BaseViewHolder;
import com.github.liaoheng.common.plus.model.SpinnerItem;
import java.util.List;

/**
 * Spinner 适配器
 *
 * @author liaoheng
 * @version 2015年9月22日
 */
public class SpinnerAdapter extends BaseListAdapter<SpinnerItem> {

    public SpinnerAdapter(Context mContext, List<SpinnerItem> mList) {
        super(mContext, mList);
    }

    @Override public View getItemView(SpinnerItem item, int position, View convertView,
                                      ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            holder = new ViewHolder(convertView);
            holder.setTag();
        } else {
            holder = ViewHolder.getTag(convertView);
        }
        holder.onHandle(item, position);
        return convertView;
    }

    class ViewHolder extends BaseViewHolder<SpinnerItem> {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = findViewById(android.R.id.text1);
        }

        @Override public void onHandle(SpinnerItem item, int position) {
            textView.setText(item.getName());
        }
    }
}