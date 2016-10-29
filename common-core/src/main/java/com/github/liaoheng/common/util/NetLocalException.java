package com.github.liaoheng.common.util;

/**
 * 网络本地方异常
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
@SystemExceptionNoVessel
public class NetLocalException extends NetException {
    private int code;
    public NetLocalException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public NetLocalException(String errorMessage) {
        super(errorMessage);
    }

    public NetLocalException(Throwable e) {
        super(e);
    }

    public NetLocalException(int code,Throwable e) {
        super(e);
        this.code = code;
    }

    public NetLocalException(String errorMessage,int code) {
        super(errorMessage);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override public String toString() {
        return "NetLocalException{" +
               "code=" + code +
               "} " + super.toString();
    }
}
