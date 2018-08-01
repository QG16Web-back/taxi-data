package com.qg.taxi.dao.oracle;

import com.qg.taxi.model.gps.GpsLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Wilder Gao
 * time:2018/7/23
 * description：第三张表的一些操作
 * motto: All efforts are not in vain
 */
@Mapper
@Repository
public interface GpsLogDao {
    /**
     * 按行查询第三张表（出租车轨迹表）的相关信息
     * @param start 开始行数
     * @param end   结束行数
     * @return  数据集合
     */
    List<GpsLog> selectToGPSPoint(@Param("start") long start, @Param("end") long end);


    /**
     * 在指定行中找到某辆车在某个时间段的相关信息
     * @param plateNo 车牌号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param start     开始行数
     * @param end   结束函数
     * @return  数据集合
     */
    List<GpsLog> selectFormTimeByCar(@Param("plateNo") String plateNo, @Param("startTime") Date startTime,
                                     @Param("endTime") Date endTime, @Param("start") long start,
                                     @Param("end") long end);
}
