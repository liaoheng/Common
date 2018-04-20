package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.BuildConfig;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2018-04-18 15:05
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE)
public class DateTimeUtilsTest extends BaseTest {

    @Test
    public void checkTimeToNextDay() {
        DateTime dateTime = DateTime.now().minusHours(1);
        DateTime dateTime1 = DateTimeUtils.checkTimeToNextDay(dateTime.toLocalTime());
        log("old time : %s", dateTime.toString());
        log("new time : %s", dateTime1.toString());
        assertTrue("day is equals", dateTime1.getDayOfMonth() == dateTime.getDayOfMonth() + 1);
    }

}