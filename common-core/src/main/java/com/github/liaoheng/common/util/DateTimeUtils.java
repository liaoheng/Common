package com.github.liaoheng.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期时间处理
 *
 * @author liaoheng
 * @version 2016-11-21 10:55
 */
public class DateTimeUtils {
    public final static String DATAFORMAT_YYYYMMDD = "YYYY-MM-dd";
    public final static String DATAFORMAT_YYYYMMDDHHMMSS = "YYYY-MM-dd HH:mm:ss";

    /**
     * 得到当前时间的utc标准时间
     *
     * @param time {@link LocalTime}
     */
    public static LocalTime getLocalTimeZoneUTC(LocalTime time) {
        DateTime dateTime = DateTime.now().withTime(time);//当前时区
        return getLocalTimeZoneUTC(dateTime);
    }

    /**
     * 得到传入日期时间的utc标准时间
     *
     * @param dateTime {@link DateTime}
     */
    public static LocalTime getLocalTimeZoneUTC(DateTime dateTime) {
        return getDateTimeZoneUTC(dateTime).toLocalTime();
    }

    /**
     * 得到传入日期时间的utc标准日期时间
     *
     * @param dateTime {@link DateTime}
     */
    public static DateTime getDateTimeZoneUTC(DateTime dateTime) {
        return dateTime.toDateTime(DateTimeZone.UTC);//utc
    }

    /**
     * 得到传入utc日期时间对应本地日期时间
     *
     * @param utcDateTime {@link DateTime}
     */
    public static DateTime getDateTimeZoneLocal(DateTime utcDateTime) {
        return DateTime.now().withDate(utcDateTime.getYear(), utcDateTime.getMonthOfYear(),
                utcDateTime.getDayOfMonth()).withTime(utcDateTime.toLocalTime());
    }

    /**
     * 得到传入utc标准时间对应本地日期时间
     *
     * @param utcTime {@link LocalTime}
     */
    public static DateTime getDateTimeZoneLocal(LocalTime utcTime) {
        return DateTime.now().withTime(utcTime);
    }

    /**
     * 得到传入utc标准时间对应本地时间
     *
     * @param utcTime {@link LocalTime}
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

    /**
     * 传入时间比当前时间小，则传入时间加24小时。
     */
    public static DateTime checkTimeToNextDay(LocalTime time) {
        DateTime now = DateTime.now();
        DateTime set = DateTime.now().withTime(time);
        if (set.toLocalTime().isBefore(now.toLocalTime())) {
            now = now.plusDays(1);
        }
        return new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour());
    }

    /**
     * 解析标准GMT时间
     *
     * @param date 时间字符串(EEE MMM dd HH:mm:ss z yyyy)
     */
    public static Date standardGMTparse(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",
                Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 上一次操作与当前操作的距离，单位天
     *
     * @param lastDate 上一次操作, local date
     * @param day 距离，天
     * @return true，超过或等于@param day
     */
    public static boolean isToDaysDo(long lastDate, int day) {
        if (lastDate == -1) {
            return true;
        }

        DateTime last = new DateTime(lastDate);
        DateTime now = DateTime.now();
        return isToDaysDo(last, now, day);
    }

    /**
     * 上一次操作与当前操作的距离，单位天
     *
     * @param last 上一次操作, local date
     * @param now 当前操作, local date
     * @param day 距离，天
     * @return true，超过或等于@param day
     */
    public static boolean isToDaysDo(DateTime last, DateTime now, int day) {
        int days = Days.daysBetween(last.toLocalDate(), now.toLocalDate()).getDays();
        return days >= day;
    }

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";
    private static final String YESTERDAY = "昨天";

    /**
     * 计算时间在多久之前
     *
     * @param date {@link Date}
     */
    public static String format(Date date) {
        long delta = new Date().getTime() - date.getTime();
        if (delta < ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return YESTERDAY;
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
}
