package com.qg.taxi.dao.oracle;

import com.qg.taxi.model.gps.GpsString;
import com.qg.taxi.model.gps.OnOffGps;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Create by ming on 18-8-3 下午2:00
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Mapper
@Repository
public interface OracleYctDao {

    /**
     * 查询上下车GPS，第二张表
     *
     * @param plateNo 车牌
     * @param time    时间
     * @return 上下车GPS
     */
    OnOffGps getOnOffGpsOracle(@Param("plateNo") String plateNo,
                               @Param("time") String time);

    /**
     * 查询上下车GPS，第三张表
     *
     * @param plateNo   车牌
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 上下车GPS
     */
    GpsString getOnOffGpsOracle2(@Param("plateNo") String plateNo,
                                 @Param("startTime") String startTime,
                                 @Param("endTime") String endTime);
}
