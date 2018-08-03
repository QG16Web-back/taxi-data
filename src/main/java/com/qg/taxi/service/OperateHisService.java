package com.qg.taxi.service;

import com.qg.taxi.model.gps.GPS;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Wilder Gao
 * time:2018/7/23
 * description：
 * motto: All efforts are not in vain
 */
public interface OperateHisService {
    /**
     * 表2插入操作
     * @param start
     * @param end
     * @param k
     * @param size 每一次插入的大小
     * @throws ParseException
     */
    void insertOperateHisToMysql(int start, int end, int k, int size) throws ParseException;


    /**
     * 获得这个区域的gps坐标，按照时间排列
     * @param latmin
     * @param lonmin
     * @param latmax
     * @param lonmax
     * @param date
     * @return
     */
    Map<Integer, List<GPS>> getAreaGpsMapByDay(double latmin, double lonmin,
                                               double latmax, double lonmax,
                                               Date date);
}
