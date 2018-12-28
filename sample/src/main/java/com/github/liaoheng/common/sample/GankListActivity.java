package com.github.liaoheng.common.sample;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.liaoheng.common.adapter.base.BaseRecyclerAdapter;
import com.github.liaoheng.common.adapter.base.IBaseAdapter;
import com.github.liaoheng.common.adapter.core.RecyclerViewHelper;
import com.github.liaoheng.common.adapter.holder.BaseRecyclerViewHolder;
import com.github.liaoheng.common.network.OkHttp3Utils;
import com.github.liaoheng.common.ui.base.CURxBaseActivity;
import com.github.liaoheng.common.util.*;
import io.reactivex.Observable;

import java.util.List;

/**
 * @author liaoheng
 * @version 2018-12-28 15:27
 */
public class GankListActivity extends CURxBaseActivity {
    GankRecyclerAdapter mAdapter;
    RecyclerViewHelper mRecyclerViewHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank_list);
        mAdapter = new GankRecyclerAdapter(this);
        mRecyclerViewHelper = RecyclerViewHelper.newBuilder(this).setOnItemClickListener(
                (IBaseAdapter.OnItemClickListener<Gank>) (item, view, position) -> {
                    openBrowser(getActivity(), item.getUrl());
                }).setAdapter(mAdapter).build();

        Observable<String> photo = OkHttp3Utils.get()
                .getAsyncToJsonString("https://gank.io/api/data/Android/20/1");
        Utils.addSubscribe(photo.compose(bindToLifecycle()),
                new Callback.EmptyCallback<String>() {
                    @Override
                    public void onPreExecute() {
                        mRecyclerViewHelper.setSwipeRefreshing(true);
                    }

                    @Override
                    public void onPostExecute() {
                        mRecyclerViewHelper.setSwipeRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.getToast().e(TAG, getActivity(), e);
                    }

                    @Override
                    public void onSuccess(String json) {
                        try {
                            List<Gank> ganks = JsonUtils.parseList(json, "results", Gank.class);
                            mAdapter.setList(ganks);
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            onError(e);
                        }
                    }
                });
    }

    public static void openBrowser(Context context, String url) {
        try {
            new CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .build()
                    .launchUrl(context, Uri.parse(url));
        } catch (Exception ignore) {
            AppUtils.openBrowser(context, url);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gank {
        @JsonProperty("_id") private String id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String getId() { return id;}

        public void setId(String id) { this.id = id;}

        public String getCreatedAt() { return createdAt;}

        public void setCreatedAt(String createdAt) { this.createdAt = createdAt;}

        public String getDesc() { return desc;}

        public void setDesc(String desc) { this.desc = desc;}

        public String getPublishedAt() { return publishedAt;}

        public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt;}

        public String getSource() { return source;}

        public void setSource(String source) { this.source = source;}

        public String getType() { return type;}

        public void setType(String type) { this.type = type;}

        public String getUrl() { return url;}

        public void setUrl(String url) { this.url = url;}

        public boolean isUsed() { return used;}

        public void setUsed(boolean used) { this.used = used;}

        public String getWho() { return who;}

        public void setWho(String who) { this.who = who;}

        public List<String> getImages() { return images;}

        public void setImages(List<String> images) { this.images = images;}
    }

    public class GankRecyclerViewHolder extends BaseRecyclerViewHolder<Gank> {
        @BindView(R.id.gank_list_item_image)
        ImageView image;
        @BindView(R.id.gank_list_item_content)
        TextView content;

        public GankRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onHandle(@Nullable Gank item, int position) {
            content.setText(item.getDesc());
            if (item.getImages() == null || item.getImages().size() < 1) {
                return;
            }
            GlideApp.with(itemView.getContext()).load(item.getImages().get(0)).into(image);
        }
    }

    public class GankRecyclerAdapter extends BaseRecyclerAdapter<Gank, GankRecyclerViewHolder> {

        public GankRecyclerAdapter(Context context) {
            super(context);
        }

        @NonNull
        @Override
        public GankRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = inflate(R.layout.view_gank_list_item, parent);
            return new GankRecyclerViewHolder(inflate);
        }

        @Override
        public void onBindViewHolderItem(@NonNull GankRecyclerViewHolder holder, @Nullable Gank item, int position) {
            holder.onHandle(item, position);
        }
    }
}
