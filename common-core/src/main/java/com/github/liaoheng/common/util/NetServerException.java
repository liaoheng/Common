package com.github.liaoheng.common.util;

/**
 * 网络服务器异常
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
@SystemExceptionNoVessel
public class NetServerException extends NetException {
    private String mErrorBody;

    public NetServerException(String errorMessage, String errorBody) {
        super(errorMessage);
        mErrorBody = errorBody;
    }

    public NetServerException(String errorBody) {
        super("");
        mErrorBody = errorBody;
    }

    public String getErrorBody() {
        return mErrorBody;
    }

    public void setErrorBody(String mErrorBody) {
        this.mErrorBody = mErrorBody;
    }

    @Override public String toString() {
        String s = super.toString();
        return s + "  ErrorBody:{ " + mErrorBody + " }";
    }
}
