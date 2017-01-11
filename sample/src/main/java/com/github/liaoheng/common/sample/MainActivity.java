package com.github.liaoheng.common.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.github.liaoheng.common.network.GlideCompressTransformation;
import com.github.liaoheng.common.network.OkHttp3Utils;
import com.github.liaoheng.common.ui.base.CURxBaseActivity;
import com.github.liaoheng.common.ui.core.CUInputDialogClickListener;
import com.github.liaoheng.common.ui.core.ProgressHelper;
import com.github.liaoheng.common.ui.view.CUBottomSheetDialog;
import com.github.liaoheng.common.ui.view.CUInputDialog;
import com.github.liaoheng.common.util.Callback;
import com.github.liaoheng.common.util.L;
import com.github.liaoheng.common.util.SystemException;
import com.github.liaoheng.common.util.UIUtils;
import com.github.liaoheng.common.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import rx.Observable;

public class MainActivity extends CURxBaseActivity {

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
        CUInputDialog.single(getActivity(), R.style.AppTheme_Dialog).setMessage("Single Input")
                .setClickListener(new CUInputDialogClickListener.EmptyCUInputDialogClickListener() {
                    @Override public void onYes(CUInputDialog dialog, EditText editText,
                                                String text) {
                        UIUtils.showToast(getApplicationContext(),text);
                    }
                }).show();
    }
    @OnClick(R.id.open_multi_input_dialog) void openMultiInputDialog() {
        CUInputDialog.multi(getActivity(), R.style.AppTheme_Dialog).setMessage("Multi Input")
                .setClickListener(new CUInputDialogClickListener.EmptyCUInputDialogClickListener() {
                    @Override public void onYes(CUInputDialog dialog, EditText editText,
                                                String text) {
                        UIUtils.showToast(getApplicationContext(),text);
                    }
                }).show();
    }

    CUBottomSheetDialog cuBottomSheetDialog;

    @OnClick(R.id.open_bottom_sheet_dialog) void openBottomSheetDialog() {
        if (cuBottomSheetDialog == null) {
            cuBottomSheetDialog = new CUBottomSheetDialog(this);
            cuBottomSheetDialog.setContentView(R.layout.view_dialog);
           cuBottomSheetDialog.findViewById(R.id.dialog_close).setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View v) {
                    cuBottomSheetDialog.dismiss();
               }
           });
        }

        cuBottomSheetDialog.setPeekHeight(300);
        cuBottomSheetDialog.show();

    }


    @OnClick(R.id.load_image) void loadImage() {
        photo();
    }

    private void photo() {
        OkHttpClient.Builder builder = OkHttp3Utils
                .getSingleOkHttpClientBuilder();
        OkHttp3Utils utils = OkHttp3Utils.init().build(builder);

        Observable<String> photo = OkHttp3Utils.get()
                .getAsyncToJsonString("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1");
        Utils.addSubscribe(photo.compose(this.<String>bindToLifecycle()),
                new Callback.EmptyCallback<String>() {
                            @Override public void onPreExecute() {
                                mProgressHelper.show();
                            }

                            @Override public void onError(SystemException e) {
                                mProgressHelper.hide();
                                L.getToast().e(TAG, getActivity(), e);
                            }

                            @Override public void onSuccess(String json) {
                                Album album = new Album();
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    JSONArray JsonPhotos = jsonObject.getJSONArray("results");
                                    for (int i = 0; i < JsonPhotos.length(); i++) {
                                        JSONObject photo = JsonPhotos.getJSONObject(i);
                                        String image = photo.getString("url");
                                        album.setItem("" + i, image);
                                    }
                                    Glide.with(getActivity()).load(album.getItems().get(0).getUrl())
                                            .transform(
                                                    new GlideCompressTransformation(getActivity(),
                                                            800)).into(image);
                                    mProgressHelper.hide();
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
