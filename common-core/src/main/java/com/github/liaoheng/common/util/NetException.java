package com.github.liaoheng.common.util;

/**
 * 自定网络服务异常
 *
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
public class NetException extends SystemException {
    public final static int NET_ERROR = 1101;
    public final static String NET_ERROR_STRING = "Net Error";

    public NetException(int type) {
        super(type);
    }

    public NetException(int type, String errorMessage) {
        super(type, errorMessage);
    }

    public NetException(int type, Throwable e) {
        super(type, e);
    }

    public NetException(int type, String errorMessage, Throwable e) {
        super(type, errorMessage, e);
    }

    public NetException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public NetException(String errorMessage) {
        super(errorMessage);
    }

    public NetException(Throwable e) {
        super(e);
    }

    @Override
    public String getTypeMessage() {
        switch (getType()) {
            case NET_ERROR:
                return NET_ERROR_STRING;
            default:
                return super.getTypeMessage();
        }
    }
}
