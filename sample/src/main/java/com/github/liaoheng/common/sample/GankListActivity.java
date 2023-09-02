package com.github.liaoheng.common.sample;

import android.annotation.SuppressLint;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.liaoheng.common.adapter.base.BaseRecyclerAdapter;
import com.github.liaoheng.common.adapter.base.IBaseAdapter;
import com.github.liaoheng.common.adapter.core.RecyclerViewHelper;
import com.github.liaoheng.common.adapter.holder.BaseRecyclerViewHolder;
import com.github.liaoheng.common.network.OkHttp3Utils;
import com.github.liaoheng.common.ui.base.CURxBaseActivity;
import com.github.liaoheng.common.ui.core.LoadStatusHelper;
import com.github.liaoheng.common.util.AppUtils;
import com.github.liaoheng.common.util.Callback;
import com.github.liaoheng.common.util.JsonUtils;
import com.github.liaoheng.common.util.L;
import com.github.liaoheng.common.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.core.Observable;

/**
 * @author liaoheng
 * @version 2018-12-28 15:27
 */
public class GankListActivity extends CURxBaseActivity {
    GankRecyclerAdapter mAdapter;
    RecyclerViewHelper mRecyclerViewHelper;
    LoadStatusHelper mStatusHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank_list);
        mAdapter = new GankRecyclerAdapter(this);
        mRecyclerViewHelper = RecyclerViewHelper.newBuilder(this).setOnItemClickListener(
                (IBaseAdapter.OnItemClickListener<PixabayImage>) (item, view, position) -> {
                    openBrowser(getActivity(), item.getPageURL());
                }).setAdapter(mAdapter).build();

        mStatusHelper = LoadStatusHelper.with(getWindow().getDecorView());

        Observable<String> photo = OkHttp3Utils.get()
                .getAsyncToJsonString(
                        "https://pixabay.com/api/?key=11234205-21f02ee751cd3cd4f1fa49b70&image_type=photo");
        Utils.addSubscribe(photo.compose(bindToLifecycle()),
                new Callback.EmptyCallback<String>() {
                    @Override
                    public void onPreExecute() {
                        mStatusHelper.isLoading(true);
                        mRecyclerViewHelper.setSwipeRefreshing(true);
                    }

                    @Override
                    public void onPostExecute() {
                        mStatusHelper.isLoading(false);
                        mRecyclerViewHelper.setSwipeRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mStatusHelper.error();
                        L.alog().e(TAG, getActivity(), e);
                    }

                    @Override
                    public void onSuccess(String json) {
                        try {
                            List<PixabayImage> ganks = JsonUtils.parseList(json, "hits", PixabayImage.class);
                            if (ganks==null || ganks.isEmpty()) {
                                mStatusHelper.empty();
                            }
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
    public static class PixabayImage {

        private String largeImageURL;
        private int webformatHeight;
        private int webformatWidth;
        private int imageWidth;
        private int id;
        private int user_id;
        private String pageURL;
        private int imageHeight;
        private String webformatURL;
        private String type;
        private int previewHeight;
        private String tags;
        private String user;
        private int imageSize;
        private int previewWidth;
        private String userImageURL;
        private String previewURL;

        public String getLargeImageURL() { return largeImageURL;}

        public void setLargeImageURL(String largeImageURL) { this.largeImageURL = largeImageURL;}

        public int getWebformatHeight() { return webformatHeight;}

        public void setWebformatHeight(int webformatHeight) { this.webformatHeight = webformatHeight;}

        public int getWebformatWidth() { return webformatWidth;}

        public void setWebformatWidth(int webformatWidth) { this.webformatWidth = webformatWidth;}

        public int getImageWidth() { return imageWidth;}

        public void setImageWidth(int imageWidth) { this.imageWidth = imageWidth;}

        public int getId() { return id;}

        public void setId(int id) { this.id = id;}

        public int getUser_id() { return user_id;}

        public void setUser_id(int user_id) { this.user_id = user_id;}

        public String getPageURL() { return pageURL;}

        public void setPageURL(String pageURL) { this.pageURL = pageURL;}

        public int getImageHeight() { return imageHeight;}

        public void setImageHeight(int imageHeight) { this.imageHeight = imageHeight;}

        public String getWebformatURL() { return webformatURL;}

        public void setWebformatURL(String webformatURL) { this.webformatURL = webformatURL;}

        public String getType() { return type;}

        public void setType(String type) { this.type = type;}

        public int getPreviewHeight() { return previewHeight;}

        public void setPreviewHeight(int previewHeight) { this.previewHeight = previewHeight;}

        public String getTags() { return tags;}

        public void setTags(String tags) { this.tags = tags;}

        public String getUser() { return user;}

        public void setUser(String user) { this.user = user;}

        public int getImageSize() { return imageSize;}

        public void setImageSize(int imageSize) { this.imageSize = imageSize;}

        public int getPreviewWidth() { return previewWidth;}

        public void setPreviewWidth(int previewWidth) { this.previewWidth = previewWidth;}

        public String getUserImageURL() { return userImageURL;}

        public void setUserImageURL(String userImageURL) { this.userImageURL = userImageURL;}

        public String getPreviewURL() { return previewURL;}

        public void setPreviewURL(String previewURL) { this.previewURL = previewURL;}
    }

    public class GankRecyclerViewHolder extends BaseRecyclerViewHolder<PixabayImage> {
        @BindView(R.id.gank_list_item_image)
        ImageView image;
        @BindView(R.id.gank_list_item_content)
        TextView content;

        public GankRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onHandle(@Nullable PixabayImage item, int position) {
            content.setText("Photo by " + item.getUser() + " on Pixabay");
            GlideApp.with(itemView.getContext()).load(item.getPreviewURL()).into(image);
        }
    }

    public class GankRecyclerAdapter extends BaseRecyclerAdapter<PixabayImage, GankRecyclerViewHolder> {

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
        public void onBindViewHolderItem(@NonNull GankRecyclerViewHolder holder, @Nullable PixabayImage item,
                int position) {
            holder.onHandle(item, position);
        }
    }
}
