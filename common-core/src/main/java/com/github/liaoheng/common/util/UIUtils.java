package com.github.liaoheng.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.liaoheng.common.R;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * 界面工具
 *
 * @author liaoheng
 * @version 2015-07-21 20:05:13
 */
public class UIUtils {
    public static int TOAST_DEFAULT_TIME = Toast.LENGTH_LONG;

    private static Toast mToast;

    /**
     * 吐丝提示
     */
    public static void showToast(@NonNull Context context, String hint) {
        showToast(context, hint, TOAST_DEFAULT_TIME);
    }

    /**
     * 吐丝提示
     */
    public static void showToast(@NonNull Context context, @StringRes int hint) {
        showToast(context, context.getString(hint), TOAST_DEFAULT_TIME);
    }

    /**
     * 吐丝提示
     */
    @SuppressLint("ShowToast")
    public static void showToast(@NonNull Context context, String hint, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), hint, duration);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(hint);
        }
        mToast.show();
    }

    /**
     * 吐丝提示与log
     */
    public static void showLogToast(String TAG, @NonNull Context context, String hint,
            int duration) {
        showToast(context, hint, duration);
        L.alog().d(TAG, hint);
    }

    /**
     * 吐丝提示与log
     */
    public static void showLogToast(String TAG, @NonNull Context context, String hint) {
        showToast(context, hint);
        L.alog().d(TAG, hint);
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = null;
    }

    public static View getActivityContentView(Activity activity) {
        if (activity == null) {
            return null;
        }
        ViewGroup group = findViewById(activity, android.R.id.content);
        return group.getChildAt(0);
    }

    /**
     * 创建进度条
     */
    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        return dialog;
    }

    /**
     * 创建进度条
     */
    public static ProgressDialog createProgressDialog(Context context, @StringRes int message) {
        return createProgressDialog(context, context.getString(message));
    }

    /**
     * 加载中进度条
     */
    public static ProgressDialog showProgressDialog(@NonNull Context context) {
        return showProgressDialog(context, context.getString(R.string.lcm_loading));
    }

    /**
     * 显示进度条
     */
    public static ProgressDialog showProgressDialog(@NonNull Context context,
            @NonNull String message) {
        ProgressDialog progressDialog = createProgressDialog(context, message);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * 显示进度条
     */
    public static ProgressDialog showProgressDialog(@NonNull Context context,
            @StringRes int message) {
        return createProgressDialog(context, context.getString(message));
    }

    /**
     * 关闭Dialog
     */
    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 显示Dialog
     */
    public static void showDialog(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 创建对像框
     */
    public static Dialog createDialog(Context context, @StyleRes int style) {
        AppCompatDialog dialog = new AppCompatDialog(context, style);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        return dialog;
    }

    /**
     * 创建对像框
     */
    public static Dialog createDialog(Context context) {
        return createDialog(context, 0);
    }

    /**
     * 是否提示框
     */
    public static AlertDialog createYNAlertDialog(Context context, String message,
            final Callback4<DialogInterface> call) {
        return createAlertDialog(context, message, call);
    }

    /**
     * 是否提示框
     */
    public static AlertDialog showYNAlertDialog(Context context, String message,
            final Callback4<DialogInterface> call) {
        AlertDialog dialog = createYNAlertDialog(context, message, call);
        dialog.show();
        return dialog;
    }

    /**
     * 信息提示框
     */
    public static AlertDialog createInfoAlertDialog(Context context, String message,
            final Callback4<DialogInterface> call) {
        return createAlertDialog(context, message, context.getString(R.string.lcm_ok),
                null, new Callback4.EmptyCallback<DialogInterface>() {
                    @Override
                    public void onYes(DialogInterface result) {
                        call.onYes(result);
                        result.dismiss();
                    }
                });
    }

    /**
     * 信息提示框
     */
    public static AlertDialog showInfoAlertDialog(Context context, String message,
            final Callback4<DialogInterface> call) {
        AlertDialog alb = createInfoAlertDialog(context, message, call);
        alb.show();
        return alb;
    }

    /**
     * 对话框
     */
    public static AlertDialog createAlertDialog(Context context, String message,
            final Callback4<DialogInterface> call) {
        return createAlertDialog(context, message, context.getString(R.string.lcm_ok),
                context.getString(R.string.lcm_no), call);
    }

    /**
     * 对话框
     */
    public static AlertDialog showAlertDialog(Context context, String message,
            final Callback4<DialogInterface> call) {
        AlertDialog alb = createAlertDialog(context, message, call);
        alb.show();
        return alb;
    }

    /**
     * 提示框
     */
    public static AlertDialog createAlertDialog(Context context, String message,
            String positiveButtonText,
            String negativeButtonText,
            final Callback4<DialogInterface> call) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message);
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    call.onYes(dialog);
                    call.onFinish(dialog);
                }
            });
        }
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    call.onNo(dialog);
                    call.onFinish(dialog);
                }
            });
        }
        return builder.create();
    }

    /**
     * 全屏
     */
    public static void fullscreen(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void setStatusBarColorRes(Window window, @ColorRes int color) {
        setStatusBarColor(window, ContextCompat.getColor(window.getContext(), color));
    }

    public static void setStatusBarColor(Window window, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(color);
        }
    }

    /**
     * 沉浸模式
     * 使用 <a href=' http://https://github.com/DreaminginCodeZH/SystemUiHelper'>SystemUiHelper</a>
     *
     * @param window {@link Window}
     */
    public static void immersive(Window window) {
        int newUiOptions = window.getDecorView().getSystemUiVisibility();
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        window.getDecorView().setSystemUiVisibility(newUiOptions);
    }

    /**
     * 提示气泡 沉浸模式
     * 使用 <a href=' http://https://github.com/DreaminginCodeZH/SystemUiHelper'>SystemUiHelper</a>
     *
     * @param window {@link Window}
     */
    public static void immersiveSticky(Window window) {
        int newUiOptions = window.getDecorView().getSystemUiVisibility();
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        window.getDecorView().setSystemUiVisibility(newUiOptions);
    }

    public static void startActivity(Context context, Class<? extends Activity> clazz) {
        startActivity(context, clazz, null);
    }

    public static void startActivity(Context context, Class<? extends Activity> clazz,
            Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Class<?> clazz, int resultCode) {
        startActionForResult(activity, clazz, null, resultCode);
    }

    public static void startActivityForResult(Activity activity, Class<?> clazz, Bundle bundle,
            int resultCode) {
        startActionForResult(activity, clazz, bundle, resultCode);
    }

    public static void startActivityForResult(Fragment fragment, Class<?> clazz, int resultCode) {
        startActionForResult(fragment, clazz, null, resultCode);
    }

    public static void startActivityForResult(Fragment fragment, Class<?> clazz, Bundle bundle,
            int resultCode) {
        startActionForResult(fragment, clazz, bundle, resultCode);
    }

    public static void startActionForResult(Object o, Class<?> clazz, Bundle bundle,
            int requestCode) {
        Intent intent;
        if (o instanceof Activity) {
            intent = new Intent((Activity) o, clazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            ((Activity) o).startActivityForResult(intent, requestCode);
        } else if (o instanceof Fragment) {
            intent = new Intent(((Fragment) o).getActivity(), clazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            ((Fragment) o).startActivityForResult(intent, requestCode);
        } else if (o instanceof android.app.Fragment) {
            intent = new Intent(((android.app.Fragment) o).getActivity(), clazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            ((android.app.Fragment) o).startActivityForResult(intent, requestCode);
        }
    }

    public static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static void toggleVisibility(View... views) {
        if (views == null || views.length == 0) {
            return;
        }
        for (View view : views) {
            if (view.getVisibility() == View.GONE) {
                viewVisible(view);
            } else {
                viewGone(view);
            }
        }
    }

    public static void viewVisibility(int visibility, View... views) {
        if (views == null || views.length == 0) {
            return;
        }
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

    public static void viewParentVisibility(int visibility, ViewParent... viewParents) {
        if (viewParents == null || viewParents.length == 0) {
            return;
        }
        for (ViewParent viewParent : viewParents) {
            if (viewParent == null) {
                continue;
            }
            ((View) viewParent).setVisibility(visibility);
        }
    }

    public static void viewVisible(View... views) {
        viewVisibility(View.VISIBLE, views);
    }

    public static void viewParentVisible(ViewParent... viewParents) {
        viewParentVisibility(View.VISIBLE, viewParents);
    }

    public static void viewInVisible(View... views) {
        viewVisibility(View.INVISIBLE, views);
    }

    public static void viewParentInVisible(ViewParent... viewParents) {
        viewParentVisibility(View.INVISIBLE, viewParents);
    }

    public static void viewGone(View... views) {
        viewVisibility(View.GONE, views);
    }

    public static void viewParentGone(ViewParent... viewParents) {
        viewParentVisibility(View.GONE, viewParents);
    }

    public static <T extends View> T inflate(Context context, @LayoutRes int resource) {
        return inflate(context, resource, null);
    }

    public static <T extends View> T inflate(@NonNull Context context, @LayoutRes int resource, ViewGroup root) {
        return inflate(context, resource, root, root != null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T inflate(@NonNull Context context, @LayoutRes int resource, ViewGroup root,
            boolean attachToRoot) {
        return (T) LayoutInflater.from(context).inflate(resource, root, attachToRoot);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(View view, @IdRes int resource) {
        if (view == null) {
            return null;
        }
        return (T) view.findViewById(resource);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(Activity activity, @IdRes int resource) {
        if (activity == null) {
            return null;
        }
        return (T) activity.findViewById(resource);
    }

    /**
     * EditText 显示和隐藏文本
     *
     * @param show true 显示，false 隐藏
     */
    public static void showOrHintEditTextContent(boolean show, @NonNull EditText editText) {
        if (show) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        editText.postInvalidate();
    }

    public static void clearTextView(TextView... textViews) {
        if (textViews == null) {
            return;
        }
        for (TextView textView : textViews) {
            textView.setText("");
        }
    }

}
