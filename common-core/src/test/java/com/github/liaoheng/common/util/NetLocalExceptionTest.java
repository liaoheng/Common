package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.TestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * @author liaoheng
 * @version 2016-07-25 13:26
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
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
            //loge(cause);
            assertTrue("is not NetLocalException", cause instanceof NetLocalException);
            assertEquals("msg is error", e.getMessage(), String.valueOf(NetLocalException.NET_ERROR));
            assertEquals("type is error", e.getType(), NetLocalException.NET_ERROR);
        }
    }

    @Test
    public void NetLocalExceptionTest2() {
        try {
            try {
                throw new TimeoutException("timeout");
            } catch (TimeoutException e) {
                throw new NetException(NetLocalException.NET_ERROR, e);
            }
        } catch (NetException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //loge(cause);
            assertTrue("is not TimeoutException", cause instanceof TimeoutException);
            assertEquals("msg is error", e.getMessage(), String.valueOf(NetLocalException.NET_ERROR));
            assertEquals("type is error", e.getType(), NetLocalException.NET_ERROR);
        }
    }

    @Test
    public void NetLocalExceptionTest3() {
        try {
            try {
                throw new TimeoutException("timeout");
            } catch (TimeoutException e) {
                throw new NetException(e);
            }
        } catch (NetException e) {
            //loge(e);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not TimeoutException", cause instanceof TimeoutException);
            assertEquals("msg is error", e.getMessage(), "timeout");
            assertEquals("type is error", e.getType(), 0);
        }
    }

    @Test
    public void NetLocalExceptionTest4() {
        try {
            try {
                throw new TimeoutException("timeout");
            } catch (TimeoutException e) {
                throw new NetException(NetException.NET_ERROR, NetException.NET_ERROR_STRING, e);
            }
        } catch (NetException e) {
            //loge(e);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not TimeoutException", cause instanceof TimeoutException);
            assertEquals("type is error", e.getType(), NetException.NET_ERROR);
            assertEquals("type msg is error", e.getTypeMessage(), NetException.NET_ERROR_STRING);
        }
    }
}