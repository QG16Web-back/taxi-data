package com.qg.taxi.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：日期操作表
 * motto: All efforts are not in vain
 */
public class DateUtil {
    /**
     * 得到日期返回数值
     * @param date
     * @return
     */
    public static Map<String, Integer> getDayAndHour(Date date){
        int monthIndex = 3;
        Map<String, Integer> map = new HashMap<>(5);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        int day = localDateTime.getDayOfMonth();

        //如果大于二月份
        if (month == monthIndex){
            day = day + (month - 2 )* 28;
        }
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();

        map.put("month", month);
        map.put("day", day);
        map.put("hour", hour);
        map.put("year",year);
        map.put("minute",minute);

        return map;
    }

    /**
     * 获得当天的前五天日期
     * @param date 当前日期
     * @return  前五天日期数组
     */
    public static int[] getDayBeforeFive(Date date){
        int num = 5;
        int february = 2;
        int[] days = new int[num];
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        if (localDateTime.getMonthValue() == february && localDateTime.getDayOfMonth() < num + 1) {
            return days;
        }
        for (int i = 1 ; i<= num ; i++){
            LocalDateTime dateTime = localDateTime.minusDays(i);
            int month = dateTime.getMonthValue();
            int day = dateTime.getDayOfMonth();
            //假如是3月份，日期加上28和数据库表对应
            if (month == 3){
                day += 28;
            }
            days[num-i] = day;
        }
        return days;
    }
}
