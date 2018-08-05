package com.qg.taxi.service;

import com.qg.taxi.model.gps.GPS;
import com.qg.taxi.model.inform.CalculateGeoHash;
import org.apache.ibatis.annotations.Param;

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
     * @param latmin 最小纬度
     * @param lonmin 最小经度
     * @param latmax    最大纬度
     * @param lonmax    最大经度
     * @param date     查询日期
     * @return  在这个区域这一天出租车的分布
     */
    Map<Integer, List> getAreaGpsMapByDay(double latmin, double lonmin,
                                               double latmax, double lonmax,
                                               Date date);

    /**
     * 获得广州市区域范围一天出租车的分布情况（按照geohash划分）
     * @param date  日期
     * @return  处理后返回的数据集
     */
    List<String[]> getGuangzhouRectangle(Date date);
}
