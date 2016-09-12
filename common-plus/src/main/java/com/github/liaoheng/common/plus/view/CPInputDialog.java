package com.github.liaoheng.common.plus.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.github.liaoheng.common.plus.R;
import com.github.liaoheng.common.plus.core.CPInputDialogClickListener;
import com.github.liaoheng.common.plus.core.ProgressHelper;
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
public class CPInputDialog extends AppCompatDialog {

    public CPInputDialog(Context context) {
        super(context, R.style.LCP_Input_Dialog);
        init(0);
    }

    public CPInputDialog(Context context, int theme) {
        super(context, theme);
        init(0);
    }

    public CPInputDialog(Context context, int theme, @LayoutRes int layout) {
        super(context, theme);
        init(layout);
    }

    EditText mEditText;
    TextView mMessage;
    Button   mCancel;
    Button   mOK;

    CPInputDialogClickListener mClickListener;
    ProgressHelper             mProgressHelper;

    List<OnShowListener>    mOnShowListeners    = new ArrayList<>();
    List<OnDismissListener> mOnDismissListeners = new ArrayList<>();

    private void init(@LayoutRes int layout) {
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (layout <= 0) {
            setContentView(R.layout.lcp_view_input_dialog);
        } else {
            setContentView(layout);
        }
        initAction();
    }

    public CPInputDialog initAction() {
        mProgressHelper = ProgressHelper.with(getWindow().getDecorView());

        mMessage = (TextView) findViewById(R.id.lcp_input_dialog_message);
        mEditText = (EditText) findViewById(R.id.lcp_input_dialog_edit_text);
        mOK = (Button) findViewById(R.id.lcp_input_dialog_ok);
        mCancel = (Button) findViewById(R.id.lcp_input_dialog_cancel);
        View.OnClickListener oKClickListener = new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mClickListener == null) {
                    return;
                }
                mClickListener.onYes(getDialog(), getEditText(), getEditTextString());
                mClickListener.onFinish(getDialog());
            }
        };
        View.OnClickListener cancelClickListener = new View.OnClickListener() {
            @Override public void onClick(View v) {
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
            @Override public void onShow(DialogInterface dialog) {
                InputMethodUtils.showSoftInput(getEditText());
            }
        });

        super.setOnShowListener(new OnShowListener() {
            @Override public void onShow(DialogInterface dialog) {
                for (OnShowListener mOnShowListener : mOnShowListeners) {
                    mOnShowListener.onShow(dialog);
                }
            }
        });

        mOnDismissListeners.add(new OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
                getProgressHelper().gone();
                getEditText().setText("");
            }
        });
        super.setOnDismissListener(new OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
                for (OnDismissListener onDismissListener : mOnDismissListeners) {
                    onDismissListener.onDismiss(dialog);
                }
            }
        });
        return this;
    }

    @Override public void dismiss() {
        InputMethodUtils.hideSoftInput(getEditText());
        super.dismiss();
    }

    @Override public void setOnShowListener(OnShowListener listener) {
        throw new IllegalStateException("You cannot use");
    }

    @Deprecated @Override public void setOnDismissListener(OnDismissListener listener) {
        throw new IllegalStateException("You cannot use");
    }

    public CPInputDialog addOnDismissListener(OnDismissListener listener) {
        mOnDismissListeners.add(listener);
        return this;
    }

    public CPInputDialog addOnShowListener(OnShowListener listener) {
        mOnShowListeners.add(listener);
        return this;
    }

    public CPInputDialog enableSingleText() {
        mEditText.setSingleLine();
        mEditText.setMaxLines(1);
        mEditText.setHorizontalScrollBarEnabled(true);
        return this;
    }

    public CPInputDialog enableMultiText() {
        mEditText.setMaxLines(4);
        mEditText.setMinLines(2);
        mEditText.setGravity(Gravity.TOP | Gravity.START);
        mEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
        mEditText.setVerticalScrollBarEnabled(true);
        return this;
    }

    public CPInputDialog setMessage(CharSequence message) {
        mMessage.setText(message);
        return this;
    }

    /**
     * 默认对话框标题
     * @param title
     * @return
     */
    public CPInputDialog setMTitle(CharSequence title) {
        super.setTitle(title);
        return this;
    }

    public CPInputDialog setEditText(CharSequence text) {
        mEditText.setText(text);
        if (!TextUtils.isEmpty(text)) {
            mEditText.setSelection(text.length());
        }
        mEditText.requestFocus();
        return this;
    }

    public CPInputDialog setCancelText(CharSequence text) {
        mCancel.setText(text);
        return this;
    }

    public CPInputDialog setOKText(CharSequence text) {
        mOK.setText(text);
        return this;
    }

    public CPInputDialog setClickListener(CPInputDialogClickListener clickListener) {
        mClickListener = clickListener;
        return this;
    }

    public CPInputDialog enableCanNotClose() {
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

    public CPInputDialog getDialog() {
        return this;
    }

    public ProgressHelper getProgressHelper() {
        return mProgressHelper;
    }

    public void showProgress() {
        getProgressHelper().visibleParent();
    }

    public void dismissProgress() {
        getProgressHelper().goneParent();
    }

    public static CPInputDialog single(Context context) {
        return new CPInputDialog(context).enableSingleText();
    }

    public static CPInputDialog single(Context context, int theme) {
        return new CPInputDialog(context, theme).enableSingleText();
    }

    public static CPInputDialog multi(Context context) {
        return new CPInputDialog(context).enableMultiText();
    }

    public static CPInputDialog multi(Context context, int theme) {
        return new CPInputDialog(context, theme).enableMultiText();
    }

    public static CPInputDialog with(Context context) {
        return new CPInputDialog(context);
    }
}
