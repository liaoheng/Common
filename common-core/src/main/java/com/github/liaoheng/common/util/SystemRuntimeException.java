package com.github.liaoheng.common.util;

import com.github.liaoheng.common.core.SystemExceptionHelper;

/**
 * 自定运行异常
 *
 * @author liaoheng
 */
public class SystemRuntimeException extends RuntimeException implements ISystemException {

    private SystemExceptionHelper mExceptionHelper;

    public SystemRuntimeException(String errorMessage, Throwable e) {
        super(errorMessage);
        mExceptionHelper = SystemExceptionHelper.with(e);
    }

    public SystemRuntimeException(String errorMessage) {
        this(errorMessage, null);
    }

    public SystemRuntimeException(Throwable e) {
        this(e.getMessage(), e);
    }

    @Override public Throwable getCause() {
        return mExceptionHelper.getCause();
    }

    public Throwable getOwnCause() {
        return mExceptionHelper.getCause(this);
    }

    /**
     * {@link Throwable#getCause()}
     * @return
     */
    public Throwable getOriginalCause() {
        return super.getCause();
    }

    @Override public String toString() {
        if (mExceptionHelper == null || mExceptionHelper.getThrowable() == null) {
            return super.toString();
        }
        return mExceptionHelper.throwableToString(this);
    }

    public Throwable getThrowable() {
        return mExceptionHelper.getThrowable();
    }

    public void setThrowable(Throwable throwable) {
        mExceptionHelper.setThrowable(throwable);
    }
}
