package com.andon.springbootutil.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Andon
 * 2021/11/11
 */
public class TimeUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TimeUtil.class);
    public static final ThreadLocal<SimpleDateFormat> FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static final ThreadLocal<SimpleDateFormat> FORMAT_DAY = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    public static final ThreadLocal<SimpleDateFormat> FORMAT_HOUR = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH"));
    public static final ThreadLocal<SimpleDateFormat> FORMAT_MINUTE = ThreadLocal.withInitial(() -> new SimpleDateFormat("mm"));
    public static final ThreadLocal<SimpleDateFormat> FORMAT_SECOND = ThreadLocal.withInitial(() -> new SimpleDateFormat("ss"));

    public static long getTimeId(String time) {
        long timeId = 0;
        try {
            Date date = FORMAT.get().parse(time);
            timeId = date.getTime() / 1000;
        } catch (ParseException e) {
            LOG.error("FORMAT parse time failure!! error={}", e.getMessage());
        }
        return timeId;
    }

    public static Date parseTime(String time) {
        Date parse = null;
        try {
            parse = TimeUtil.FORMAT.get().parse(time);
        } catch (ParseException e) {
            LOG.error("FORMAT parse time failure!! error={}", e.getMessage());
        }
        return parse;
    }

    public static boolean checkFormat(String time, SimpleDateFormat simpleDateFormat) {
        try {
            Date parse = simpleDateFormat.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void sleep(long timestamp) {
        try {
            Thread.sleep(timestamp);
        } catch (InterruptedException e) {
            LOG.error("Thread sleep failure!! error={}", e.getMessage());
        }
    }

    // 根据日期取得星期几
    public static String getWeek(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }
}