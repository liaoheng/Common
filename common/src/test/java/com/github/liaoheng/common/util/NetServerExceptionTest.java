package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.SdkConfig;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2016-07-25 15:30
 */
@RunWith(RobolectricTestRunner.class) @Config(constants = BuildConfig.class, sdk = SdkConfig.FALLBACK_SDK_VERSION) public class NetServerExceptionTest
        extends BaseTest {

    @Test public void NetServerExceptionTest1() {
        try {
            throw new NetServerException("404", "404");
        } catch (NetServerException e) {
            SystemException exception = new SystemException(e);
            Throwable cause = exception.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG, "", cause);
            assertTrue("is not NetServerException", cause instanceof NetServerException);
            assertEquals("msg is error", exception.getMessage(), "404");
        }

    }

    @Test public void NetServerExceptionTest2() {
        try {
            throw new SystemRuntimeException(new NetServerException("404", "404"));
        } catch (SystemRuntimeException e) {
            SystemException exception=new SystemException(e);
            Throwable cause = exception.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG,"",cause);
            assertTrue("is not NetServerException",
                    cause instanceof NetServerException);
            assertEquals("msg is error", exception.getMessage(), "404");
        }

    }

    @Test public void NetServerExceptionTest3() {
        try {
            throw new SystemRuntimeException(new NetServerException("404", "404"));
        } catch (SystemRuntimeException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG, "", cause);
            assertTrue("is not NetServerException", cause instanceof NetServerException);
            assertEquals("msg is error", e.getMessage(), "404");
        }

    }

}