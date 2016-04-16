package com.leng.common.plus.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leng.common.plus.model.SpinnerItem;

/**
 *  Spinner 适配器
 * 
 * @author liaoheng
 * @version 2015年9月22日
 */
public class SpinnerAdapter extends BaseListAdapter<SpinnerItem> {

    public SpinnerAdapter(Context mContext, List<SpinnerItem> mList) {
        super(mContext, mList);
    }

    @Override
    public View getItemView(SpinnerItem item, int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(item.getName());
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}