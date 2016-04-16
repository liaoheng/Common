package com.leng.common.util;

import android.support.annotation.NonNull;
import android.util.AndroidRuntimeException;

/**
 * 自定运行异常
 *
 * @author liaoheng
 */
public class SystemRuntimeException extends AndroidRuntimeException {

    private String errorCode;

    /**
     * 是否为SystemRuntimeException
     * @param throwable {@link Throwable}
     * @return 是就返回包装的错误
     */
    public static Throwable isSystemRuntimeException(Throwable throwable) {
        if (throwable instanceof SystemRuntimeException) {
            return throwable.getCause();
        }
        return throwable;
    }

    public SystemRuntimeException(String errorMessage, @NonNull Throwable e) {
        super(errorMessage, e);
    }

    public SystemRuntimeException(String errorMessage) {
        super(errorMessage);
    }

    public SystemRuntimeException(@NonNull Throwable e) {
        super(e.getMessage(), e.getCause());
    }

    public SystemRuntimeException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public SystemRuntimeException(String errorMessage, String errorCode, @NonNull Throwable e) {
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
