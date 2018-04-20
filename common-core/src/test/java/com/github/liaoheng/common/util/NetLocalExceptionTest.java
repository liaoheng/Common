package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.BuildConfig;

import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.SdkConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2016-07-25 13:26
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NetLocalExceptionTest extends BaseTest {

    @Test
    public void NetLocalExceptionTest1() {
        try {
            try {
                throw new TimeoutException("timeout");
            } catch (TimeoutException e) {
                throw new SystemException(NetLocalException.NET_ERROR, new NetLocalException(e));
            }
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG, "", cause);
            assertTrue("is not NetLocalException", cause instanceof NetLocalException);
            assertEquals("msg is error", e.getMessage(), NetLocalException.NET_ERROR);
        }
    }

    @Test
    public void NetLocalExceptionTest2() {
        try {
            try {
                throw new TimeoutException("timeout");
            } catch (TimeoutException e) {
                throw new NetException(NetLocalException.NET_ERROR, new NetLocalException(e));
            }
        } catch (NetException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG, "", cause);
            assertTrue("is not NetLocalException", cause instanceof NetLocalException);
            assertEquals("msg is error", e.getMessage(), NetLocalException.NET_ERROR);
        }
    }
}