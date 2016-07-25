package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2016-07-25 15:30
 */
@RunWith(RobolectricGradleTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21) public class NetServerExceptionTest extends BaseTest {

    public class MServerError implements ServerError {
        String message;

        @Override public String message() {
            return message;
        }

        public MServerError setMessage(String message) {
            this.message = message;
            return this;
        }
    }


    @Test
    public void NetServerExceptionTest1(){
        MServerError mServerError=new MServerError().setMessage("404");

        try {
            throw new NetServerException(mServerError);
        } catch (NetServerException e) {
            SystemException exception=new SystemException(e);
            Throwable cause = exception.getCause();
            assertNotNull("is null", cause);
            ShadowLog.d(TAG,"",cause);
            assertTrue("is not NetServerException",
                    cause instanceof NetServerException);
            assertEquals("msg is error", exception.getMessage(), "404");
        }

    }

}