package com.github.liaoheng.common.sample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.github.liaoheng.common.plus.ui.CPBaseActivity;
import com.github.liaoheng.common.plus.util.GlideCompressTransformation;
import com.github.liaoheng.common.plus.util.OkHttp3Utils;
import com.github.liaoheng.common.util.Callback;
import com.github.liaoheng.common.util.L;
import com.github.liaoheng.common.util.SystemException;
import com.github.liaoheng.common.util.UIUtils;
import com.github.liaoheng.common.util.Utils;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import rx.Subscription;

public class MainActivity extends CPBaseActivity {

    ImageView image;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = UIUtils.createProgressDialog(this, "加载图片信息中...");
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override public void onCancel(DialogInterface dialog) {
                Utils.unsubscribe(douban);
            }
        });

        File externalFilesDir = getExternalFilesDir(null);
        File externalFilesDir1 = getExternalFilesDir("123");

        File externalStoragePublicDirectory1 = Environment.getExternalStoragePublicDirectory("321");

        image = UIUtils.findViewById(this, R.id.image);
        UIUtils.findViewById(this, R.id.load_image).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                photo();
            }
        });
    }

    private Subscription   douban;
    private ProgressDialog progressDialog;

    private void photo() {

        douban = OkHttp3Utils.get()
                .getAsyncToJsonString("https://api.douban.com/v2/album/103756651/photos",
                        new Callback.EmptyCallback<String>() {
                            @Override public void onPreExecute() {
                                progressDialog.show();
                            }

                            @Override public void onError(SystemException e) {
                                UIUtils.dismissDialog(progressDialog);
                                L.getToast().e(TAG, getActivity(), e);
                            }

                            @Override public void onSuccess(String json) {
                                Album album = new Album();
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    int count = jsonObject.getInt("count");
                                    L.i(TAG, "count:" + count);
                                    JSONArray JsonPhotos = jsonObject.getJSONArray("photos");
                                    for (int i = 0; i < JsonPhotos.length(); i++) {
                                        JSONObject photo = JsonPhotos.getJSONObject(i);
                                        String image = photo.getString("image");
                                        album.setItem("" + i, image);
                                    }
                                    Glide.with(getActivity()).load(album.getItems().get(0).getUrl())
                                            .transform(
                                                    new GlideCompressTransformation(getActivity(),
                                                            800)).into(image);
                                } catch (Exception e) {
                                    L.getToast().e(TAG, getActivity(), e);
                                }
                            }

                            @Override public void onFinish() {
                                progressDialog.dismiss();
                            }
                        });
    }

    public class Album implements Serializable {
        public Album() {
        }

        public Album(String name, String url) {
            this.name = name;
            this.url = url;
        }

        private String      name;
        private String      url;
        private List<Album> items;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<Album> getItems() {
            return items;
        }

        public void setItem(String name, String url) {
            if (items == null) {
                items = new ArrayList<>();
            }
            items.add(new Album(name, url));
        }

        public void setItems(List<Album> items) {
            this.items = items;
        }
    }
}
