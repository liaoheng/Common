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
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
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
import android.widget.Toast;

import com.github.liaoheng.common.R;

/**
 * 界面工具
 *
 * @author liaoheng
 * @version 2015-07-21 20:05:13
 */
public class UIUtils {
    public static int TOAST_DEFAULT_TIME     = Toast.LENGTH_LONG;
    public static int SNACK_BAR_DEFAULT_TIME = Snackbar.LENGTH_LONG;

    private static Toast mToast;

    /**
     * 吐丝提示
     *
     * @param context
     * @param hint
     */
    public static void showToast(@NonNull Context context, String hint) {
        showToast(context, hint, TOAST_DEFAULT_TIME);
    }

    /**
     * 吐丝提示
     *
     * @param context
     * @param hint
     */
    public static void showToast(@NonNull Context context,@StringRes int hint) {
        showToast(context, context.getString(hint), TOAST_DEFAULT_TIME);
    }

    /**
     * 吐丝提示
     *
     * @param context
     * @param hint
     * @param duration
     */
    @SuppressLint("ShowToast")
    public static void showToast(@NonNull Context context, String hint, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, hint, duration);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(hint);
        }
        mToast.show();
    }

    /**
     * 吐丝提示与log
     *
     * @param context
     * @param hint
     * @param duration
     */
    public static void showLogToast(String TAG, @NonNull Context context, String hint,
                                    int duration) {
        showToast(context, hint, duration);
        L.i(TAG, hint);
    }

    /**
     * 吐丝提示与log
     *
     * @param context
     * @param hint
     */
    public static void showLogToast(String TAG, @NonNull Context context, String hint) {
        showToast(context, hint);
        L.i(TAG, hint);
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    /*******
     * Snack  start
     ********/

    public static void showSnack(@NonNull Activity activity, String hint) {
        showSnack(activity, hint, SNACK_BAR_DEFAULT_TIME);
    }

    public static void showSnack(@NonNull Activity activity, @StringRes int hint) {
        showSnack(activity, activity.getResources().getString(hint));
    }

    public static void showSnack(@NonNull final Activity activity, final String hint,
                                 final int duration) {
        showSnack(activity, hint, duration, activity.getText(R.string.lcm_ok).toString(), null);
    }

    public static void showSnack(@NonNull Activity activity, final String hint, final String action,
                                 final Callback2<View> callback2) {
        showSnack(activity, hint, SNACK_BAR_DEFAULT_TIME, action, callback2);
    }

    public static void showSnack(@NonNull Activity activity, final String hint, final int duration,
                                 final String action, final Callback2<View> callback2) {
        getCoordinatorLayout(activity, new Callback4.EmptyCallback<View>() {
            @Override public void onYes(View view) {
                Snackbar.make(view, hint, duration).setAction(action, new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if (callback2 == null) {
                            return;
                        }
                        callback2.onSuccess(v);
                    }
                }).show();
            }
        });
    }

    public static void showSnack(@NonNull View view, String hint) {
        showSnack(view, hint, SNACK_BAR_DEFAULT_TIME);
    }

    public static void showSnack(@NonNull View view, final String hint, final int duration) {
        showSnack(view, hint, duration, view.getResources().getText(R.string.lcm_ok).toString(),
                null);
    }

    public static void showSnack(@NonNull View view, final String hint, final String action,
                                 final Callback2<View> callback2) {
        showSnack(view, hint, SNACK_BAR_DEFAULT_TIME, action, callback2);
    }

    public static void showSnack(@NonNull View view, final String hint, final int duration,
                                 final String action, final Callback2<View> callback2) {
        getCoordinatorLayout(view, new Callback4.EmptyCallback<View>() {
            @Override public void onYes(View view) {
                Snackbar.make(view, hint, duration).setAction(action, new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if (callback2 == null) {
                            return;
                        }
                        callback2.onSuccess(v);
                    }
                }).show();
            }
        });
    }

    public static void showLogSnack(String TAG, @NonNull View view, String hint, int duration) {
        showSnack(view, hint, duration);
        L.i(TAG, hint);
    }

    public static void showLogSnack(String TAG, @NonNull final Activity activity, final String hint,
                                    final int duration) {
        showSnack(activity, hint, duration);
        L.i(TAG, hint);
    }

    public static void showLogSnack(String TAG, @NonNull View view, String hint) {
        showSnack(view, hint);
        L.i(TAG, hint);
    }

    public static void showLogSnack(String TAG, @NonNull Activity activity, final String hint) {
        showSnack(activity, hint);
        L.i(TAG, hint);
    }

    /*******  Snack  end ********/

    public static View getActivityContentView(Activity activity) {
        if (activity == null) {
            return null;
        }
        ViewGroup group = findViewById(activity, android.R.id.content);
        return group.getChildAt(0);
    }

    /**
     * 得到CoordinatorLayout
     *
     * @param activity
     * @param call
     * @return
     */
    public static void getCoordinatorLayout(Activity activity, final Callback4<View> call) {
        getCoordinatorLayout(getActivityContentView(activity), call);
    }

    /**
     * 得到CoordinatorLayout
     *
     * @param view
     * @param call
     * @return
     */
    public static void getCoordinatorLayout(View view, final Callback4<View> call) {
        try {
            Class.forName("android.support.design.widget.CoordinatorLayout");
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("CoordinatorLayout is null");
        }
        if (view == null) {
            return;
        }
        if (!isCoordinatorLayout(view)) {
            findCoordinatorLayout(view, call);
        } else {
            call.onYes(view);
        }
    }

    public static boolean isCoordinatorLayout(View view) {
        return view instanceof CoordinatorLayout;
    }

    private static void findCoordinatorLayout(View view, Callback4<View> mCall) {
        ViewGroup group = (ViewGroup) view;
        for (int i = 0; i < group.getChildCount(); i++) {
            View view1 = group.getChildAt(i);
            if (isCoordinatorLayout(view1)) {
                mCall.onYes(view1);
                return;
            }
        }
    }

    /**
     * 创建进度条
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        return dialog;
    }

    /**
     * 加载中进度条
     *
     * @param context
     * @return
     */
    public static ProgressDialog showProgressDialog(@NonNull Context context) {
        return showProgressDialog(context, context.getString(R.string.lcm_loading));
    }

    /**
     * 显示进度条
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog showProgressDialog(@NonNull Context context,
                                                    @NonNull String message) {
        ProgressDialog progressDialog = createProgressDialog(context, message);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * 关闭Dialog
     *
     * @return
     */
    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 显示Dialog
     *
     * @return
     */
    public static void showDialog(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 创建对像框
     *
     * @param context
     * @param style
     * @return
     */
    public static Dialog createDialog(Context context, @StyleRes int style) {
        AppCompatDialog dialog = new AppCompatDialog(context, style);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        return dialog;
    }

    /**
     * 创建对像框
     *
     * @param context
     * @return
     */
    public static Dialog createDialog(Context context) {
        return createDialog(context, 0);
    }

    /**
     * 是否提示框
     *
     * @param context
     * @param call
     * @return
     */
    public static AlertDialog createYNAlertDialog(Context context, String message,
                                                  final Callback4<DialogInterface> call) {
        return createAlertDialog(context, message, call);
    }

    /**
     * 是否提示框
     *
     * @param context
     * @param call
     * @return
     */
    public static AlertDialog showYNAlertDialog(Context context, String message,
                                                final Callback4<DialogInterface> call) {
        AlertDialog dialog = createYNAlertDialog(context, message, call);
        dialog.show();
        return dialog;
    }

    /**
     * 信息提示框
     *
     * @param context
     * @param message
     * @param call
     * @return
     */
    public static AlertDialog createInfoAlertDialog(Context context, String message,
                                                    final Callback4<DialogInterface> call) {
        return createAlertDialog(context, message, context.getString(R.string.lcm_ok),
                null, new Callback4.EmptyCallback<DialogInterface>() {
                    @Override public void onYes(DialogInterface result) {
                        call.onYes(result);
                        result.dismiss();
                    }
                });
    }

    /**
     * 信息提示框
     *
     * @param context
     * @param message
     * @param call
     * @return
     */
    public static AlertDialog showInfoAlertDialog(Context context, String message,
                                                  final Callback4<DialogInterface> call) {
        AlertDialog alb = createInfoAlertDialog(context, message, call);
        alb.show();
        return alb;
    }

    /**
     * 对话框
     *
     * @param context
     * @param message
     * @param call
     * @return
     */
    public static AlertDialog createAlertDialog(Context context, String message,
                                                final Callback4<DialogInterface> call) {
        return createAlertDialog(context, message, context.getString(R.string.lcm_ok),
                context.getString(R.string.lcm_no), call);
    }

    /**
     * 对话框
     *
     * @param context
     * @param message
     * @param call
     * @return
     */
    public static AlertDialog showAlertDialog(Context context, String message,
                                              final Callback4<DialogInterface> call) {
        AlertDialog alb = createAlertDialog(context, message, call);
        alb.show();
        return alb;
    }

    /**
     * 提示框
     *
     * @param context
     * @param message
     * @param positiveButtonText
     * @param negativeButtonText
     * @param call
     * @return
     */
    public static AlertDialog createAlertDialog(Context context, String message,
                                                String positiveButtonText,
                                                String negativeButtonText,
                                                final Callback4<DialogInterface> call) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message);
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    call.onYes(dialog);
                    call.onFinish(dialog);
                }
            });
        }
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    call.onNo(dialog);
                    call.onFinish(dialog);
                }
            });
        }
        return builder.create();
    }

    /**
     * 全屏
     *
     * @param window
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

    public static void toggleVisibility(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() == View.GONE) {
            viewVisible(view);
        } else {
            viewGone(view);
        }
    }

    public static void viewVisible(View view) {
        if (view == null) {
            return;
        }
        view.setVisibility(View.VISIBLE);
    }

    public static void viewParentVisible(ViewParent viewParent) {
        if (viewParent == null) {
            return;
        }
        ((View) viewParent).setVisibility(View.VISIBLE);
    }

    public static void viewInVisible(View view) {
        if (view == null) {
            return;
        }
        view.setVisibility(View.INVISIBLE);
    }

    public static void viewParentInVisible(ViewParent viewParent) {
        if (viewParent == null) {
            return;
        }
        ((View) viewParent).setVisibility(View.INVISIBLE);
    }

    public static void viewGone(View view) {
        if (view == null) {
            return;
        }
        view.setVisibility(View.GONE);
    }

    public static void viewParentGone(ViewParent viewParent) {
        if (viewParent == null) {
            return;
        }
        ((View) viewParent).setVisibility(View.GONE);
    }

    public static <T extends View> T inflate(Context context, @LayoutRes int resource) {
        return inflate(context, resource, null);
    }

    public static <T extends View> T inflate(Context context, @LayoutRes int resource,
                                             ViewGroup root) {
        return inflate(context, resource, root, root != null);
    }

    @SuppressWarnings("unchecked") public static <T extends View> T inflate(Context context,
                                                                            @LayoutRes int resource,
                                                                            ViewGroup root,
                                                                            boolean attachToRoot) {
        return (T) LayoutInflater.from(context).inflate(resource, root, attachToRoot);
    }

    @SuppressWarnings("unchecked") public static <T extends View> T findViewById(@NonNull View view,
                                                                                 @IdRes int resource) {
        return (T) view.findViewById(resource);
    }

    @SuppressWarnings("unchecked") public static <T extends View> T findViewById(
            @NonNull Activity activity, @IdRes int resource) {
        return (T) activity.findViewById(resource);
    }

    /**
     * EditText 显示和隐藏文本
     *
     * @param show true 显示，false 隐藏
     */
    public static void showOrHintPassword(boolean show, @NonNull EditText editText) {
        if (show) {
            //设置EditText文本为可见的
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //设置EditText文本为隐藏的
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        editText.postInvalidate();
    }

}
