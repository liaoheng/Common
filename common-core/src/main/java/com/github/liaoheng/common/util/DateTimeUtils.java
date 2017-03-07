package com.github.liaoheng.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

/**
 * 日期时间处理
 * @author liaoheng
 * @version 2016-11-21 10:55
 */
public class DateTimeUtils {
    public final static String DATAFORMAT_YYYYMMDD="YYYY-MM-dd";
    public final static String DATAFORMAT_YYYYMMDDHHSS="YYYY-MM-dd HH:ss";

    /**
     * 得到当前时间的utc标准时间
     * @param time {@link LocalTime}
     * @return
     */
    public static LocalTime getLocalTimeZoneUTC(LocalTime time) {
        DateTime dateTime = DateTime.now().withTime(time);//当前时区
        return getLocalTimeZoneUTC(dateTime);
    }

    /**
     * 得到传入日期时间的utc标准时间
     * @param dateTime {@link DateTime}
     * @return
     */
    public static LocalTime getLocalTimeZoneUTC(DateTime dateTime) {
        return getDateTimeZoneUTC(dateTime).toLocalTime();
    }

    /**
     * 得到传入日期时间的utc标准日期时间
     * @param dateTime {@link DateTime}
     * @return
     */
    public static DateTime getDateTimeZoneUTC(DateTime dateTime) {
        return dateTime.toDateTime(DateTimeZone.UTC);//utc
    }

    /**
     * 得到传入utc日期时间对应本地日期时间
     * @param utcDateTime {@link DateTime}
     * @return
     */
    public static DateTime getDateTimeZoneLocal(DateTime utcDateTime) {
        return DateTime.now().withDate(utcDateTime.getYear(), utcDateTime.getMonthOfYear(),
                utcDateTime.getDayOfMonth()).withTime(utcDateTime.toLocalTime());
    }

    /**
     * 得到传入utc标准时间对应本地日期时间
     * @param utcTime {@link LocalTime}
     * @return
     */
    public static DateTime getDateTimeZoneLocal(LocalTime utcTime) {
        return DateTime.now().withTime(utcTime);
    }

    /**
     * 得到传入utc标准时间对应本地时间
     * @param utcTime {@link LocalTime}
     * @return
     */
    public static LocalTime getLocalTimeZoneLocal(LocalTime utcTime) {
        return getDateTimeZoneLocal(utcTime).toLocalTime();
    }

    /**
     * 得到前一个月的最后一天 0 :0
     */
    public static DateTime getPreMonthLastDay() {
        DateTime dateTime = DateTime.now().minusMonths(1);
        int maximumValue = dateTime.dayOfMonth().getMaximumValue();
        return new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), maximumValue, 0, 0);
    }
}
