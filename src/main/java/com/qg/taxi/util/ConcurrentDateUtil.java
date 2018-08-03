package com.qg.taxi.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Create by ming on 18-8-3 上午9:48
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
public class ConcurrentDateUtil {

    private static ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static Date parse(String dateStr) throws ParseException {
        return threadLocal.get().parse(dateStr);
    }

    public static String format(Date date) {
        return threadLocal.get().format(date);
    }


    private static ThreadLocal<DateFormat> withInitial = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    public static Date ymdParse(String dateStr) throws ParseException {
        return withInitial.get().parse(dateStr);
    }

    public static String ymdFormat(Date date) {
        return withInitial.get().format(date);
    }

    /**
     * 获取两个日期的相隔天数
     *
     * @param oldDate old
     * @param newDate new
     * @return 相隔天数
     */
    public static int getDate(Date oldDate, Date newDate) {
        Calendar d1 = new GregorianCalendar();
        d1.setTime(newDate);
        Calendar d2 = new GregorianCalendar();
        d2.setTime(oldDate);
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }
}
