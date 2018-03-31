package com.github.liaoheng.common.util;

/**
 * 网络服务器异常
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
@SystemExceptionNoVessel
public class NetServerException extends NetException {
    private String serverErrorCode;
    private String serverErrorMessage;
    private Object mErrorBody;

    public NetServerException(String errorMessage, Object errorBody) {
        super(errorMessage, null);
        mErrorBody = errorBody;
    }

    public NetServerException(Object errorBody) {
        super("Error", null);
        mErrorBody = errorBody;
    }

    public String getServerErrorCode() {
        return serverErrorCode;
    }

    public void setServerErrorCode(String serverErrorCode) {
        this.serverErrorCode = serverErrorCode;
    }

    public String getServerErrorMessage() {
        return serverErrorMessage;
    }

    public void setServerErrorMessage(String serverErrorMessage) {
        this.serverErrorMessage = serverErrorMessage;
    }

    @SuppressWarnings("unchecked") public <T> T getErrorBody() {
        return (T) mErrorBody;
    }

    public void setErrorBody(Object mErrorBody) {
        this.mErrorBody = mErrorBody;
    }

    @Override public String toString() {
        return "msg: " + getMessage() + " { " + mErrorBody + " } ";
    }
}
