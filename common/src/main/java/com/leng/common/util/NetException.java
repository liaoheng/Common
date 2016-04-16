package com.leng.common.util;

import android.support.annotation.NonNull;

/**
 * 自定网络服务异常
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
public class NetException extends SystemException {
    public final static String NET_ERROR             = "网络操作错误！";
    public NetException(String errorMessage, @NonNull Throwable e) {
        super(errorMessage, e);
    }

    public NetException(String errorMessage) {
        super(errorMessage);
    }

    public NetException(@NonNull Throwable e) {
        super(e);
    }

    public NetException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }

    public NetException(String errorMessage, String errorCode, @NonNull Throwable e) {
        super(errorMessage, errorCode, e);
    }
}
