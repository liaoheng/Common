package com.github.liaoheng.common.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.liaoheng.adapter.base.BaseListAdapter;
import com.github.liaoheng.adapter.base.BaseRecyclerAdapter;
import com.github.liaoheng.adapter.base.IBaseAdapter;
import com.github.liaoheng.adapter.holder.BaseRecyclerViewHolder;
import com.github.liaoheng.common.sample.databinding.ActivityListBinding;
import com.github.liaoheng.ui.base.CUBaseActivity;
import com.github.liaoheng.util.UIUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liaoheng
 * @version 2016-12-19 16:38
 */
public class ListActivity extends CUBaseActivity {


    void update(){

        List<String> strings =new ArrayList<>();
        for (int i = 5; i < 10; i++) {
            strings.add(String.valueOf(i));
        }

        //listAdapter.setList(null);
        //listAdapter.notifyDataSetChanged();
        recyclerAdapter.addAll(strings);
        recyclerAdapter.notifyDataSetChanged();
        //systemListAdapter.list.addAll(strings);
        //systemListAdapter.notifyDataSetChanged();
    }

    ActivityListBinding mViewBinding;

    ListAdapter listAdapter;

    RecyclerAdapter recyclerAdapter;

    SystemListAdapter systemListAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        mViewBinding.listUpdate.setOnClickListener(v -> update());

        List<String> strings =new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            strings.add(String.valueOf(i));
        }



         listAdapter = new ListAdapter(this, strings);
        recyclerAdapter = new RecyclerAdapter(this,strings);
        mViewBinding.list.setAdapter(recyclerAdapter,new IBaseAdapter.OnItemClickListener<String>() {
            @Override public void onItemClick(String item, View view, int position) {
                UIUtils.showToast(getApplicationContext(),item);
            }
        });

        systemListAdapter=new SystemListAdapter(this,strings);
        //list.setAdapter(systemListAdapter);

        //list.setAdapter(recyclerAdapter);

        //list.setAdapter(listAdapter,new IBaseAdapter.OnItemClickListener<String>() {
        //        @Override public void onItemClick(String item, View view, int position) {
        //            UIUtils.showToast(getApplicationContext(),item);
        //        }
        //    });

        //list.setAdapter(listAdapter);
    }

    public class RecyclerViewHolder extends BaseRecyclerViewHolder<String> {
        TextView text;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
             text = (TextView) itemView.findViewById(R.id.list_item_text);
        }

        @Override public void onHandle(String item, int position, Object args) {
            text.setText(item);
        }
    }

    public class RecyclerAdapter extends BaseRecyclerAdapter<String,RecyclerViewHolder> {

        public RecyclerAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @NonNull
        @Override public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerViewHolder(inflate(R.layout.view_list_item,parent));
        }

        @Override public void onBindViewHolderItem(@NonNull RecyclerViewHolder holder, String item,
                                                   int position) {
                holder.onHandle(item,position,null);
        }
    }

    public class ListAdapter extends BaseListAdapter<String> {

        public ListAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override public View getItemView(String item, int position, View convertView,
                                          ViewGroup parent) {
            View view = inflate(R.layout.view_list_item,parent);
            TextView text = (TextView) view.findViewById(R.id.list_item_text);
            text.setText(item);
            return view;
        }
    }

    public class SystemListAdapter extends BaseAdapter {
        List<String> list;
        Context context;
        public SystemListAdapter(Context context, List<String> list) {
            this.context =context;
            this.list=list;
        }

        @Override public int getCount() {
            return list.size();
        }

        @Override public Object getItem(int position) {
            return list.get(position);
        }

        @Override public long getItemId(int position) {
            return position;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflate(R.layout.view_list_item,parent,false);
            TextView text = (TextView) view.findViewById(R.id.list_item_text);
            text.setText(list.get(position));
            return view;
        }
    }

}
