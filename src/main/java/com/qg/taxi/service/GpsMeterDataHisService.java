package com.qg.taxi.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：
 * motto: All efforts are not in vain
 */
public interface GpsMeterDataHisService {

    /**
     * 表1插入操作
     * @param start 起始位置
     * @param end 结束位置
     * @param k 第k部分，比如k=100，则插入单位长度*100这一部分的数据
     * @param size 导入每一次的容量大小
     * @throws ParseException 异常
     */
    void insertMeterHisToMysql(int start, int end, int k, int size) throws ParseException;


    /**
     * 统计一个区域内各个时间段的打车总量
     * 注：一个区域将划分为多个6位的geoHash
     * 一个 geoHash 的划分代表一个键值对
     * @param latmin    最小纬度
     * @param lonmin    最小经度
     * @param latmax    最大纬度
     * @param lonmax    最大经度
     * @param precis    划分geoHash长度
     * @return excel 链接
     */
    String countGeoHashByTime(double latmin, double lonmin,
                              double latmax, double lonmax, int precis, Date date) throws IOException;
}
