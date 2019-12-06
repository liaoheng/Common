package com.github.liaoheng.common.util;

import com.github.liaoheng.common.core.SystemExceptionHelper;

/**
 * 自定异常
 *
 * @author liaoheng
 * @version 2015-07-21 20:06:58
 */
public class SystemException extends Exception implements ISystemException {
    private SystemExceptionHelper mExceptionHelper;

    public SystemException(String errorMessage, Throwable e) {
        super(errorMessage);
        mExceptionHelper = SystemExceptionHelper.with(e);
    }

    public SystemException(String errorMessage) {
        super(errorMessage);
    }

    public SystemException(Throwable e) {
        this(e.getMessage(), e);
    }

    protected Object mErrorBody;

    @SuppressWarnings("unchecked")
    public <T> T getErrorBody() {
        return (T) mErrorBody;
    }

    public void setErrorBody(Object errorBody) {
        mErrorBody = errorBody;
    }

    @Override
    public Throwable getCause() {
        if (mExceptionHelper == null) {
            return getOriginalCause();
        }
        return mExceptionHelper.getCause();
    }

    /**
     * {@link Throwable#getCause()}
     */
    @Override
    public Throwable getOriginalCause() {
        return super.getCause();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        if (mExceptionHelper == null || mExceptionHelper.getThrowable() == null) {
            return super.toString();
        }
        return mExceptionHelper.throwableToString(this);
    }

    /**
     * {@link Throwable#getMessage()}
     */
    @Override
    public String getOriginalMessage() {
        return super.getMessage();
    }

    public Throwable getThrowable() {
        return mExceptionHelper.getThrowable();
    }

    public void setThrowable(Throwable throwable) {
        mExceptionHelper.setThrowable(throwable);
    }
}
