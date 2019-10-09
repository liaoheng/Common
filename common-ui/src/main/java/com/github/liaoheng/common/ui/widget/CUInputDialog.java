package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.ui.core.CUInputDialogClickListener;
import com.github.liaoheng.common.ui.core.ProgressHelper;

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
    private InputMethodManager mInputMethodManager;

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
        mInputMethodManager = ContextCompat.getSystemService(getContext(), InputMethodManager.class);
        initAction();
    }

    private CUInputDialog initAction() {
        mProgressHelper = ProgressHelper.with(getWindow().getDecorView());

        mMessage = findViewById(R.id.lcu_input_dialog_message);
        mEditText = findViewById(R.id.lcu_input_dialog_edit_text);
        mEditText.requestFocus();
        mEditText.post(() -> mInputMethodManager.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT));
        mOK = findViewById(R.id.lcu_input_dialog_ok);
        mCancel = findViewById(R.id.lcu_input_dialog_cancel);
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
        View.OnClickListener cancelClickListener = v -> {
            if (mClickListener == null) {
                return;
            }
            mClickListener.onCancel(getDialog(), getEditTextString());
            mClickListener.onFinish(getDialog());
        };
        mOK.setOnClickListener(oKClickListener);
        mCancel.setOnClickListener(cancelClickListener);

        super.setOnShowListener(dialog -> {
            for (OnShowListener mOnShowListener : mOnShowListeners) {
                mOnShowListener.onShow(dialog);
            }
        });
        mOnDismissListeners.add(dialog -> {
            getProgressHelper().hide();
            getEditText().setText("");
        });
        super.setOnDismissListener(dialog -> {
            for (OnDismissListener onDismissListener : mOnDismissListeners) {
                onDismissListener.onDismiss(dialog);
            }
        });
        return this;
    }

    @Override
    public void dismiss() {
        mInputMethodManager.hideSoftInputFromWindow(getEditText().getWindowToken(),
                InputMethodManager.HIDE_IMPLICIT_ONLY);
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
     */
    public CUInputDialog setMTitle(CharSequence title) {
        super.setTitle(title);
        return this;
    }

    /**
     * 默认对话框标题
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
