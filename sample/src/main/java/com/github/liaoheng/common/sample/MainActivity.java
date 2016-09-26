package com.github.liaoheng.common.sample;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.github.liaoheng.common.network.GlideCompressTransformation;
import com.github.liaoheng.common.network.OkHttp3Utils;
import com.github.liaoheng.common.ui.base.CPRxBaseActivity;
import com.github.liaoheng.common.ui.core.CPInputDialogClickListener;
import com.github.liaoheng.common.ui.core.ProgressHelper;
import com.github.liaoheng.common.ui.view.CPInputDialog;
import com.github.liaoheng.common.util.Callback;
import com.github.liaoheng.common.util.L;
import com.github.liaoheng.common.util.SystemException;
import com.github.liaoheng.common.util.UIUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends CPRxBaseActivity {

    @BindView(R.id.image)
    ImageView image;

    private ProgressHelper mProgressHelper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mProgressHelper = ProgressHelper.with(this);
    }

    @OnClick(R.id.open_single_input_dialog) void openSingleInputDialog() {
        CPInputDialog.single(getActivity(), R.style.AppTheme_Dialog).setMessage("Single Input")
                .setClickListener(new CPInputDialogClickListener.EmptyCPInputDialogClickListener() {
                    @Override public void onYes(CPInputDialog dialog, EditText editText,
                                                String text) {
                        UIUtils.showToast(getApplicationContext(),text);
                    }
                }).show();
    }

    @OnClick(R.id.open_multi_input_dialog) void openMultiInputDialog() {
        CPInputDialog.multi(getActivity(), R.style.AppTheme_Dialog).setMessage("Multi Input")
                .setClickListener(new CPInputDialogClickListener.EmptyCPInputDialogClickListener() {
                    @Override public void onYes(CPInputDialog dialog, EditText editText,
                                                String text) {
                        UIUtils.showToast(getApplicationContext(),text);
                    }
                }).show();
    }


    @OnClick(R.id.load_image) void loadImage() {
        photo();
    }

    private void photo() {

        OkHttp3Utils.get()
                .getAsyncToJsonString(lifecycle(),"https://api.douban.com/v2/album/103756651/photos",
                        new Callback.EmptyCallback<String>() {
                            @Override public void onPreExecute() {
                                mProgressHelper.visible();
                            }

                            @Override public void onError(SystemException e) {
                                mProgressHelper.gone();
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
                                    mProgressHelper.gone();
                                } catch (Exception e) {
                                    L.getToast().e(TAG, getActivity(), e);
                                }
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
