package com.qg.taxi.dao.mysql;

import com.qg.taxi.model.gps.GPS;
import com.qg.taxi.model.gps.OnOffGps;
import com.qg.taxi.model.inform.Yct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Create by ming on 18-8-3 上午11:38
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Mapper
@Repository
public interface YctDao {

    /**
     * 获取GPS点
     *
     * @param PLATENO   车牌
     * @param startTime 起始时间
     * @param endTime   终止时间
     * @param tableName 表名
     * @return gps list
     */
    List<GPS> getGps(@Param("PLATENO") String PLATENO,
                     @Param("startTime") Date startTime,
                     @Param("endTime") Date endTime,
                     @Param("tableName") String tableName);

    /**
     * 获取上下车地点
     *
     * @param PLATENO   车牌
     * @param startTime 起始时间
     * @param tableName 表名
     * @return 上下车GPS
     */
    OnOffGps getOnOffGps(@Param("PLATENO") String PLATENO,
                         @Param("startTime") Date startTime,
                         @Param("tableName") String tableName);

    /**
     * 获取羊城通List
     *
     * @return yct list
     */
    List<String> getYctList();

    /**
     * 获取详细的信息
     *
     * @return list
     */
    List<Yct> getYctInfo();
}
