package com.github.liaoheng.common;

import org.junit.Before;
import org.robolectric.shadows.ShadowLog;

/**
 * @author liaoheng
 * @version 2016-07-25 15:31
 */
public class BaseTest {

    protected final String TAG = this.getClass().getSimpleName();

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
    }

    protected void log(String msg, Object... o) {
        ShadowLog.d(TAG, String.format(msg, o));
    }

    protected void loge(Throwable e) {
        loge(e.getMessage(), e);
    }

    protected void loge(String msg, Throwable e) {
        ShadowLog.e(TAG, msg, e);
    }
}
