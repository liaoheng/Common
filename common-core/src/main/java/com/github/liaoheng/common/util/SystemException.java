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
    private int mType;

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public SystemException(int type) {
        this(type, String.valueOf(type));
    }

    public SystemException(int type, String errorMessage) {
        this(errorMessage);
        mType = type;
    }

    public SystemException(int type, Throwable e) {
        this(type, String.valueOf(type), e);
    }

    public SystemException(int type, String errorMessage, Throwable e) {
        super(errorMessage, e);
        mType = type;
    }

    public SystemException(String errorMessage, Throwable e) {
        super(errorMessage);
        mExceptionHelper = SystemExceptionHelper.with(e);
    }

    public SystemException(String errorMessage) {
        super(errorMessage);
    }

    public SystemException(String errorMessage, String myErrorMessage) {
        this(errorMessage, new SystemDataException(myErrorMessage));
    }

    public SystemException(Throwable e) {
        this(e.getMessage(), e);
    }

    public String getTypeMessage() {
        switch (mType) {
            case 0:
                return "";
            case INTERNAL_ERROR:
                return INTERNAL_ERROR_STRING;
            default:
                return UNKNOWN_ERROR_STRING;
        }
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
