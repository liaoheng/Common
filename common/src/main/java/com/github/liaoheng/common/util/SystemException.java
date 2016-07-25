package com.github.liaoheng.common.util;

/**
 * 自定异常
 *
 * @author liaoheng
 * @version 2015-07-21 20:06:58
 */
public class SystemException extends Exception implements ISystemException {
    private Throwable cause;
    /**
     * 将当前异常抛出自定义运行异常
     */
    public void throwRuntimeException() {
        throw new SystemRuntimeException(this);
    }

    public SystemException(String errorMessage) {
        super(errorMessage);
    }

    public SystemException(Throwable e) {
        this(e.getMessage(), e);
        this.cause = e;
    }

    public SystemException(String errorMessage, Throwable e) {
        super(errorMessage);
        this.cause = e;
    }

    @Override public Throwable getCause() {
        if (cause != null && cause instanceof SystemException) {
            SystemExceptionNoVessel annotation = cause.getClass()
                    .getAnnotation(SystemExceptionNoVessel.class);
            if (annotation != null) {
                return cause;
            }
            Throwable cause1 = this.cause.getCause();
            return cause1 == null ? this : cause1;
        }
        return cause == null ? this : cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
