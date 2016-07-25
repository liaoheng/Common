package com.github.liaoheng.common.util;

/**
 * 网络服务器异常
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
@SystemExceptionNoVessel
public class NetServerException extends NetException {

    private ServerError errorObject;

    public NetServerException(String errorMessage) {
        super(errorMessage);
    }

    public NetServerException(String errorMessage, ServerError errorObject) {
        super(errorMessage);
        this.errorObject = errorObject;
    }

    public NetServerException(ServerError errorObject) {
        super(errorObject.message());
        this.errorObject = errorObject;
    }

    public ServerError getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ServerError errorObject) {
        this.errorObject = errorObject;
    }

    @Override public String toString() {
        return "NetServerException{" +
               "errorObject=" + errorObject +
               '}';
    }
}
