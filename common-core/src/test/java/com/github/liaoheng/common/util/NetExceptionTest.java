package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.TestApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2016-07-25 13:26
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class NetExceptionTest extends BaseTest {

    @Test
    public void NetExceptionTest1() {
        try {
            throw new SystemException("NetException", new NetException(new TimeoutException("timeout")));
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //loge(cause);
            assertTrue("is not NetException", cause instanceof NetException);
            assertEquals("msg incorrect", e.getMessage(), "NetException");
        }
    }

    @Test
    public void NetExceptionTest2() {
        try {
            throw new NetException("NetException", new TimeoutException("timeout"));
        } catch (NetException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //loge(cause);
            assertTrue("is not TimeoutException", cause instanceof TimeoutException);
            assertEquals("msg incorrect", e.getMessage(), "NetException");
        }
    }

    @Test
    public void NetExceptionTest3() {
        try {
            throw new NetException(new TimeoutException("timeout"));
        } catch (NetException e) {
            //loge(e);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not TimeoutException", cause instanceof TimeoutException);
            assertEquals("msg incorrect", e.getMessage(), "timeout");
        }
    }
}