package com.qg.taxi.dao.mysql;

import com.qg.taxi.model.gps.GPS;
import com.qg.taxi.model.inform.CalculateGeoHash;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：
 * motto: All efforts are not in vain
 */
@Mapper
@Repository
public interface GeoHashDao {

    /**
     * 统计某一个geohash 中出租车的总量，一张表代表一天
     * 也就是统计一天某一个geohash出租车的总量
     * @param geoHash  geohash
     * @param tableName 表名
     * @return
     */
    List<CalculateGeoHash> selectGuangZhou(@Param("geoHash") String geoHash,
                                           @Param("tableName") String tableName);



    /**
     * 获得某条路对应的坐标点（当然首先要找到路对应的坐标点，也就是一个区域）
     * @param latmin 最小纬度
     * @param latmax 最大纬度
     * @param logmin 最小经度
     * @param logmax 最大经度
     * @return gps坐标点集合
     */
    List<GPS> getRoadAreaGps(@Param("latmin") double latmin,
                             @Param("latmax") double latmax,
                             @Param("logmin") double logmin,
                             @Param("logmax") double logmax);
}
