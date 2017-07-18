package com.github.liaoheng.common.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.ui.core.CUInputDialogClickListener;
import com.github.liaoheng.common.ui.core.ProgressHelper;
import com.github.liaoheng.common.util.InputMethodUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 文字输入对话框，带进度条，<br/>
 * 自定义样式可继承:LCP.Input.Dialog || Theme.AppCompat.Light.Dialog.Alert || Theme.AppCompat.Dialog.Alert,
 * 不需要标题栏添加:
 * <pre>
 *     &lt;item name="windowActionBar"&gt;false&lt;/item&gt;
 *     &lt;item name="windowNoTitle"&gt;true&lt;/item&gt;
 * </pre>
 *
 * @author liaoheng
 * @version 2016-08-30 14:10
 */
public class CUInputDialog extends AppCompatDialog {

    public CUInputDialog(Context context) {
        super(context, R.style.LCU_Input_Dialog);
        init(0);
    }

    public CUInputDialog(Context context, @StyleRes int theme) {
        super(context, theme);
        init(0);
    }

    public CUInputDialog(Context context, @StyleRes int theme, @LayoutRes int layout) {
        super(context, theme);
        init(layout);
    }

    private EditText mEditText;
    private TextView mMessage;
    private Button mCancel;
    private Button mOK;

    private CUInputDialogClickListener mClickListener;
    private ProgressHelper mProgressHelper;

    private List<OnShowListener> mOnShowListeners = new ArrayList<>();
    private List<OnDismissListener> mOnDismissListeners = new ArrayList<>();

    private void init(@LayoutRes int layout) {
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (layout <= 0) {
            setContentView(R.layout.lcu_view_input_dialog);
        } else {
            setContentView(layout);
        }
        initAction();
    }

    private CUInputDialog initAction() {
        mProgressHelper = ProgressHelper.with(getWindow().getDecorView());

        mMessage = (TextView) findViewById(R.id.lcu_input_dialog_message);
        mEditText = (EditText) findViewById(R.id.lcu_input_dialog_edit_text);
        mOK = (Button) findViewById(R.id.lcu_input_dialog_ok);
        mCancel = (Button) findViewById(R.id.lcu_input_dialog_cancel);
        View.OnClickListener oKClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener == null) {
                    return;
                }
                mClickListener.onYes(getDialog(), getEditText(), getEditTextString());
                mClickListener.onFinish(getDialog());
            }
        };
        View.OnClickListener cancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener == null) {
                    return;
                }
                mClickListener.onCancel(getDialog(), getEditTextString());
                mClickListener.onFinish(getDialog());
            }
        };
        mOK.setOnClickListener(oKClickListener);
        mCancel.setOnClickListener(cancelClickListener);

        mOnShowListeners.add(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodUtils.showSoftInput(getEditText());
            }
        });

        super.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                for (OnShowListener mOnShowListener : mOnShowListeners) {
                    mOnShowListener.onShow(dialog);
                }
            }
        });

        mOnDismissListeners.add(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getProgressHelper().hide();
                getEditText().setText("");
            }
        });
        super.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                for (OnDismissListener onDismissListener : mOnDismissListeners) {
                    onDismissListener.onDismiss(dialog);
                }
            }
        });
        return this;
    }

    @Override
    public void dismiss() {
        InputMethodUtils.hideSoftInput(getEditText());
        super.dismiss();
    }

    /**
     * see {@link CUInputDialog#addOnShowListener(OnShowListener)}
     */
    @Deprecated
    @Override
    public void setOnShowListener(OnShowListener listener) {
        throw new IllegalStateException("You cannot use");
    }

    /**
     * see {@link CUInputDialog#addOnDismissListener(OnDismissListener)}
     */
    @Deprecated
    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        throw new IllegalStateException("You cannot use");
    }

    public CUInputDialog addOnShowListener(OnShowListener listener) {
        mOnShowListeners.add(listener);
        return this;
    }

    public CUInputDialog addOnDismissListener(OnDismissListener listener) {
        mOnDismissListeners.add(listener);
        return this;
    }

    public CUInputDialog enableSingleText() {
        mEditText.setSingleLine();
        mEditText.setMaxLines(1);
        mEditText.setHorizontalScrollBarEnabled(true);
        return this;
    }

    public CUInputDialog enableMultiText() {
        mEditText.setMaxLines(4);
        mEditText.setMinLines(2);
        mEditText.setGravity(Gravity.TOP | Gravity.START);
        mEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
        mEditText.setVerticalScrollBarEnabled(true);
        return this;
    }


    /**
     * 默认对话框标题
     *
     * @see {@link #setTitle(CharSequence)}
     */
    public CUInputDialog setMTitle(CharSequence title) {
        super.setTitle(title);
        return this;
    }

    /**
     * 默认对话框标题
     *
     * @see {@link #setTitle(int)}
     */
    public CUInputDialog setMTitle(@StringRes int titleId) {
        super.setTitle(titleId);
        return this;
    }

    public CUInputDialog setMessage(CharSequence message) {
        mMessage.setText(message);
        return this;
    }

    public CUInputDialog setEditText(CharSequence text) {
        mEditText.setText(text);
        if (!TextUtils.isEmpty(text)) {
            mEditText.setSelection(text.length());
        }
        mEditText.requestFocus();
        return this;
    }

    public CUInputDialog setCancelText(CharSequence text) {
        mCancel.setText(text);
        return this;
    }

    public CUInputDialog setOKText(CharSequence text) {
        mOK.setText(text);
        return this;
    }

    public CUInputDialog setClickListener(CUInputDialogClickListener clickListener) {
        mClickListener = clickListener;
        return this;
    }

    public CUInputDialog enableCanNotClose() {
        super.setCancelable(false);
        super.setCanceledOnTouchOutside(false);
        return this;
    }

    public String getEditTextString() {
        return mEditText.getText().toString();
    }

    public EditText getEditText() {
        return mEditText;
    }

    public CUInputDialog getDialog() {
        return this;
    }

    public ProgressHelper getProgressHelper() {
        return mProgressHelper;
    }

    public void showProgress() {
        getProgressHelper().show();
    }

    public void dismissProgress() {
        getProgressHelper().hide();
    }

    public static CUInputDialog single(Context context) {
        return with(context).enableSingleText();
    }

    public static CUInputDialog single(Context context, @StyleRes int theme) {
        return with(context, theme).enableSingleText();
    }

    public static CUInputDialog multi(Context context) {
        return with(context).enableMultiText();
    }

    public static CUInputDialog multi(Context context, @StyleRes int theme) {
        return with(context, theme).enableMultiText();
    }

    public static CUInputDialog with(Context context) {
        return new CUInputDialog(context);
    }

    public static CUInputDialog with(Context context, @StyleRes int theme) {
        return new CUInputDialog(context, theme);
    }
}
