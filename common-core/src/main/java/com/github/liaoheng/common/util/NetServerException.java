package com.github.liaoheng.common.util;

/**
 * 网络服务器异常
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
@SystemExceptionNoVessel
public class NetServerException extends NetException {
    private Object mErrorBody;

    public NetServerException(String errorMessage, Object errorBody) {
        super(errorMessage);
        mErrorBody = errorBody;
    }

    public NetServerException(Object errorBody) {
        super("");
        mErrorBody = errorBody;
    }

    @SuppressWarnings("unchecked") public <T> T getErrorBody() {
        return (T) mErrorBody;
    }

    public void setErrorBody(Object mErrorBody) {
        this.mErrorBody = mErrorBody;
    }

    @Override public String toString() {
        String s = super.toString();
        return s + "  ErrorBody:{ " + mErrorBody + " }";
    }
}
