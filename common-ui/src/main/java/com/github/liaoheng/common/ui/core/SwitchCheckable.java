package com.github.liaoheng.common.ui.core;

import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;

/**
 * @author liaoheng
 * @date 2021-09-26 13:39
 */
public interface SwitchCheckable extends Checkable {
    /**
     * 覆写 {@link View#setSelected(boolean)} 方法
     */
    void setSelected(boolean selected);

    /**
     * 更新选中状态
     */
    void updateSelected(boolean selected);

    /**
     * 异步选中状态
     */
    boolean isAsyncSelected();

    /**
     * 支持原生xml方式时不可覆写本方法并需要在updateSelected中调用super.setSelected(selected)方法；覆写 {@link View#isSelected()} 方法时，UI状态只能自行处理。
     */
    boolean isSelected();

    /**
     * 覆写 {@link Checkable#setChecked(boolean)} 方法
     */
    @Override
    void setChecked(boolean checked);

    /**
     * 覆写 {@link View#performClick()} 方法
     */
    boolean performClick();

    void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener);
}
