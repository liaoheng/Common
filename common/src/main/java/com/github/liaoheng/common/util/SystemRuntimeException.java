package com.github.liaoheng.common.util;

/**
 * 自定运行异常
 *
 * @author liaoheng
 */
public class SystemRuntimeException extends RuntimeException implements ISystemException {

    public SystemRuntimeException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public SystemRuntimeException(String errorMessage) {
        super(errorMessage);
    }

    public SystemRuntimeException(Throwable e) {
        super(e.getMessage(), e);
    }

    @Override public Throwable getCause() {
        Throwable cause = super.getCause();
        if (cause != null && cause instanceof ISystemException) {
            return cause.getCause();
        }
        return cause;
    }
}
