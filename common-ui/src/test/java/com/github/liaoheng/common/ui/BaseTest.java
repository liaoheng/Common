package com.github.liaoheng.common.ui;

import org.junit.Before;
import org.robolectric.shadows.ShadowLog;

/**
 * @author liaoheng
 * @version 2016-07-25 15:31
 */
public class BaseTest {

    public final String TAG = this.getClass().getSimpleName();

    @Before public void setUp() throws Exception {
        ShadowLog.stream = System.out;
    }

    public void log(String msg, Object... o) {
        ShadowLog.d(TAG, String.format(msg, o));
    }
}
