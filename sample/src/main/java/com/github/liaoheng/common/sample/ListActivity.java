package com.github.liaoheng.common.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.liaoheng.common.adapter.base.BaseListAdapter;
import com.github.liaoheng.common.adapter.base.BaseRecyclerAdapter;
import com.github.liaoheng.common.adapter.base.IBaseAdapter;
import com.github.liaoheng.common.adapter.holder.BaseRecyclerViewHolder;
import com.github.liaoheng.common.adapter.view.ListLinearLayout;
import com.github.liaoheng.common.ui.base.CUBaseActivity;
import com.github.liaoheng.common.util.UIUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author liaoheng
 * @version 2016-12-19 16:38
 */
public class ListActivity extends CUBaseActivity {

    @BindView(R.id.list) ListLinearLayout<String> list;

    @OnClick(R.id.list_update)
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



    ListAdapter listAdapter;

    RecyclerAdapter recyclerAdapter;

    SystemListAdapter systemListAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);


        List<String> strings =new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            strings.add(String.valueOf(i));
        }



         listAdapter = new ListAdapter(this, strings);
        recyclerAdapter = new RecyclerAdapter(this,strings);
        list.setAdapter(recyclerAdapter,new IBaseAdapter.OnItemClickListener<String>() {
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

    public class RecyclerViewHolder extends BaseRecyclerViewHolder<String>{
        TextView text;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
             text = (TextView) itemView.findViewById(R.id.list_item_text);
        }

        @Override public void onHandle(String item, int position, Object args) {
            text.setText(item);
        }
    }

    public class RecyclerAdapter extends BaseRecyclerAdapter<String,RecyclerViewHolder>{

        public RecyclerAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerViewHolder(inflate(R.layout.view_list_item,parent));
        }

        @Override public void onBindViewHolderItem(RecyclerViewHolder holder, String item,
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
