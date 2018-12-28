package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.TestApplication;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2018-04-18 15:05
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class DateTimeUtilsTest extends BaseTest {

    @Test
    public void checkTimeToNextDay() {
        DateTime dateTime = DateTime.now().minusHours(1);
        DateTime dateTime1 = DateTimeUtils.checkTimeToNextDay(dateTime.toLocalTime());
        log("old time : %s", dateTime.toString());
        log("new time : %s", dateTime1.toString());
        assertEquals("day is equals", dateTime1.getDayOfMonth(), dateTime.getDayOfMonth() + 1);
    }

    @Test
    public void isToDaysDoTest() {
        DateTime now = DateTime.now();
        //log("now : %s", now.toString());
        DateTime next = DateTime.now().minusHours(now.getHourOfDay() + 1);
        //log("next : %s", next.toString());
        boolean toDaysDo = DateTimeUtils.isToDaysDo(next, now, 1);
        //log("toDaysDo : %s", toDaysDo);
        assertTrue(toDaysDo);
    }

}