package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.TestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * @author liaoheng
 * @version 2016-09-08 11:43
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class MD5UtilsTest extends BaseTest {

    @Test
    public void testMd5ToString() throws Exception {
        String hex32 = MD5Utils.md5Hex("hello");
        assertEquals("md5 falseness", hex32, "5d41402abc4b2a76b9719d911017c592");
        String hex16 = MD5Utils.byteToHex16(MD5Utils.md5("hello"));
        assertEquals("md5 falseness", hex16, "bc4b2a76b9719d91");
    }
}