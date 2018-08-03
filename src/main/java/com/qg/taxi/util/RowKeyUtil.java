package com.qg.taxi.util;

import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：
 * motto: All efforts are not in vain
 */
public class RowKeyUtil {
    /**
     * 得到固定格式的rowKey
     * @param plateNo 车牌
     * @param geoHash geohash
     * @param date  日期
     * @return
     */
    public static String getRowKey(long id, String plateNo, String geoHash, Date date){
        Random random = new Random();
        Map<String, Integer> dateMap = DateUtil.getDayAndHour(date);
        plateNo = plateNo.replaceAll("粤","");
        String month = dateMap.get("month")>=10? dateMap.get("month").toString() :
                "0"+dateMap.get("month");
        String day = dateMap.get("day")>=10? dateMap.get("day").toString() :
                "0"+dateMap.get("day");
        String hour = dateMap.get("hour")>=10? dateMap.get("hour").toString() :
                "0"+dateMap.get("hour");
        String minute = dateMap.get("minute")>=10? dateMap.get("minute").toString() :
                "0"+dateMap.get("minute");

        return month+day
                +hour
                +minute
                +geoHash
                +plateNo
                +id;
    }

    public static String getRowKeyMeterHis(long id, String plateNo, Date date) {
        Map<String, Integer> dateMap = DateUtil.getDayAndHour(date);
        plateNo = plateNo.replaceAll("粤", "");
        String month = dateMap.get("month") >= 10 ? dateMap.get("month").toString() :
                "0" + dateMap.get("month");
        String day = dateMap.get("day") >= 10 ? dateMap.get("day").toString() :
                "0" + dateMap.get("day");
        String hour = dateMap.get("hour") >= 10 ? dateMap.get("hour").toString() :
                "0" + dateMap.get("hour");
        String minute = dateMap.get("minute") >= 10 ? dateMap.get("minute").toString() :
                "0" + dateMap.get("minute");

        return plateNo + month + day
                + hour
                + minute
                + id;
    }
}
