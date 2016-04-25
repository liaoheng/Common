package com.github.liaoheng.common.util;

import android.support.annotation.NonNull;
import android.util.AndroidException;

/**
 * 自定异常
 *
 * @author liaoheng
 * @version 2015-07-21 20:06:58
 */
public class SystemException extends AndroidException {
    public final static String DEFALUT               = "内部错误！";
    public final static String DATA_ERROR            = "参数错误！";
    public final static String PICTURE_ACQUIRE_ERROR = "无法获取图片！";

    private String errorCode;

    /**
     * 将当前异常抛出自定义运行异常
     */
    public void throwRuntimeException() {
        throw new SystemRuntimeException(this);
    }

    /**
     * 判断当前异常或包装异常为NetException
     * @return true 是 
     */
    public boolean isNetException() {
        if (this instanceof NetException || getCause() instanceof NetException) {
            return true;
        }
        return false;
    }

    public SystemException(String errorMessage, @NonNull Throwable e) {
        super(errorMessage, e);
    }

    public SystemException(String errorMessage) {
        super(errorMessage);
    }

    public SystemException(@NonNull Throwable e) {
        super(e.getMessage(), e.getCause());
    }

    public SystemException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public SystemException(String errorMessage, String errorCode, @NonNull Throwable e) {
        super(errorMessage, e);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
