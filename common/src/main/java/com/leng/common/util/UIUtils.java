package com.leng.common.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 界面工具
 *
 * @author liaoheng
 * @version 2015-07-21 20:05:13
 */
public class UIUtils {
    public static int LENGTH_LONG        = Toast.LENGTH_LONG;
    public static int LENGTH_SHORT       = Toast.LENGTH_SHORT;
    public static int SNACK_LENGTH_SHORT = Snackbar.LENGTH_SHORT;
    public static int SNACK_LENGTH_LONG  = Snackbar.LENGTH_LONG;

    private final static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static Toast         mToast;

    /**
     * 吐丝提示
     *
     * @param context
     * @param hint
     */
    public static void showToast(@NonNull Context context, String hint) {
        showToast(context, hint, LENGTH_LONG);
    }

    /**
     * 吐丝提示 UI线程
     *
     * @param context
     * @param hint
     */
    public static void showToastUI(@NonNull final Context context, final String hint) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(context, hint, LENGTH_LONG);
            }
        });
    }

    /**
     * 吐丝提示
     *
     * @param context
     * @param hint
     * @param duration
     */
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

    public static void showSnackUI(@NonNull final Window window, final String hint) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                showSnack(window, hint);
            }
        });
    }

    public static void showSnackUI(@NonNull final View view, final String hint) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                showSnack(view, hint);
            }
        });
    }

    public static void showSnack(@NonNull Activity activity, String hint) {
        showSnack(activity.getWindow(), hint);
    }

    public static void showSnack(@NonNull Activity activity, @StringRes int hint) {
        showSnack(activity, activity.getResources().getString(hint));
    }

    public static void showSnack(@NonNull Window window, String hint) {
        showSnack(window, hint, SNACK_LENGTH_LONG);
    }

    public static void showSnack(@NonNull final Window window, final String hint,
                                 final int duration) {
        getRootView(window, new OperateCallback.EmptyOperateCallback<View>() {
            @Override
            public void onSuccess(View view) {
                showSnack(view, hint, duration);
            }
        });
    }

    public static void showSnack(@NonNull View view, String hint) {
        showSnack(view, hint, SNACK_LENGTH_LONG);
    }

    public static void showSnack(@NonNull View view, String hint, int duration) {
        Snackbar.make(view, hint, duration).setAction("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    public static void showLogSnack(String TAG, @NonNull View view, String hint, int duration) {
        showSnack(view, hint, duration);
        L.i(TAG, hint);
    }

    public static void showLogSnack(String TAG, @NonNull final Activity activity, final String hint,
                                    final int duration) {
        showSnack(activity.getWindow(), hint, duration);
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

    /**
     * Activity的根Layout对象
     *
     * @param window
     * @param mCall
     * @return
     */
    private static void getRootView(Window window, final OperateCallback<View> mCall) {
        if (window == null) {
            return;
        }
        ViewGroup group = (ViewGroup) window.findViewById(android.R.id.content);
        View view = group.getChildAt(0);
        if (!isRootView(view)) {
            findRootView(view, mCall);
        } else {
            mCall.onSuccess(view);
        }
    }

    private static void findRootView(View view, OperateCallback<View> mCall) {
        ViewGroup group = (ViewGroup) view;
        for (int i = 0; i < group.getChildCount(); i++) {
            View view1 = group.getChildAt(i);
            if (isRootView(view1)) {
                mCall.onSuccess(view1);
                return;
            }
        }
    }

    private static boolean isRootView(View view) {
        if (view instanceof CoordinatorLayout) {
            return true;
        } else if (view instanceof FrameLayout) {
            return true;
        } else if (view instanceof LinearLayout) {
            return true;
        } else if (view instanceof RelativeLayout) {
            return true;
        } else if (view instanceof GridLayout) {
            return true;
        } else {
            return false;
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
        return showProgressDialog(context, "加载中...");
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
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 显示Dialog
     *
     * @return
     */
    public static void showDialog(Dialog dialog) {
        if (dialog != null) {
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
     * 保存提示框
     *
     * @param context
     * @param call
     * @return
     */
    public static AlertDialog createSaveAlertDialog(Context context,
                                                    final OperateCallback<DialogInterface> call) {
        return createAlertDialog(context, "是否保存当前内容?", call);
    }

    /**
     * 保存提示框
     *
     * @param context
     * @param call
     * @return
     */
    public static AlertDialog showSaveAlertDialog(Context context,
                                                  final OperateCallback<DialogInterface> call) {
        AlertDialog alb = createSaveAlertDialog(context, call);
        alb.show();
        return alb;
    }

    /**
     * 是否提示框
     *
     * @param context
     * @param call
     * @return
     */
    public static AlertDialog createYNAlertDialog(Context context, String message,
                                                  final OperateCallback<DialogInterface> call) {
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
                                                final OperateCallback<DialogInterface> call) {
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
                                                    final OperateCallback<DialogInterface> call) {
        return createAlertDialog(context, message, "知道了", null,
            new OperateCallback.EmptyOperateCallback<DialogInterface>() {
                @Override
                public void onSuccess(DialogInterface result) {
                    call.onSuccess(result);
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
                                                  final OperateCallback<DialogInterface> call) {
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
                                                final OperateCallback<DialogInterface> call) {
        return createAlertDialog(context, message, "是", "否", call);
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
                                              final OperateCallback<DialogInterface> call) {
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
                                                final OperateCallback<DialogInterface> call) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message);
        if (StringUtils.isNotEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    call.onSuccess(dialog);
                    call.onFinish(dialog);
                }
            });
        }
        if (StringUtils.isNotEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    call.onError(dialog);
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
        //全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    public static void findCompoundButton(View view, OperateCallback<CompoundButton> mCall) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                findCompoundButton(group.getChildAt(i), mCall);
            }
        } else if (view instanceof CompoundButton) {
            mCall.onSuccess((CompoundButton) view);
        }
    }

    public static void findImage(View view, OperateCallback<ImageView> mCall) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                findImage(group.getChildAt(i), mCall);
            }
        } else if (view instanceof ImageView) {
            mCall.onSuccess((ImageView) view);
        }
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
                                            int resultCode) {
        Intent intent;
        if (o instanceof Activity) {
            intent = new Intent((Activity) o, clazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            ((Activity) o).startActivityForResult(intent, resultCode);
        } else if (o instanceof Fragment) {
            intent = new Intent(((Fragment) o).getActivity(), clazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            ((Fragment) o).startActivityForResult(intent, resultCode);
        } else if (o instanceof android.app.Fragment) {
            intent = new Intent(((android.app.Fragment) o).getActivity(), clazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            ((android.app.Fragment) o).startActivityForResult(intent, resultCode);
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

    public static void viewInVisible(View view) {
        if (view == null) {
            return;
        }
        view.setVisibility(View.INVISIBLE);
    }

    public static void viewGone(View view) {
        if (view == null) {
            return;
        }
        view.setVisibility(View.GONE);
    }

    public static void addView(@NonNull ViewGroup group, @NonNull View child) {
        group.removeAllViews();
        group.addView(child);
    }

    public static <T extends View> T inflate(Context context, @LayoutRes int resource) {
        return inflate(context, resource, null);
    }

    public static <T extends View> T inflate(Context context, @LayoutRes int resource,
                                             ViewGroup root) {
        return inflate(context, resource, root, root != null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T inflate(Context context, @LayoutRes int resource,
                                             ViewGroup root, boolean attachToRoot) {
        return (T) LayoutInflater.from(context).inflate(resource, root, attachToRoot);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(@NonNull View view, @IdRes int resource) {
        return (T) view.findViewById(resource);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(@NonNull Activity activity, @IdRes int resource) {
        return (T) activity.findViewById(resource);
    }

}
