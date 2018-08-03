package com.qg.taxi.dao.mysql;

import com.qg.taxi.model.gps.GPS;
import com.qg.taxi.model.gps.GpsOperateHis;
import com.qg.taxi.model.inform.CalculateGeoHash;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wilder Gao
 * time:2018/7/23
 * description：mysql对于第二张表的操作
 * motto: All efforts are not in vain
 */
@Mapper
@Repository
public interface OperateHisDao {
    /**
     * 插入表2数据
     * @param graph 表名
     * @param list  数据集合
     * @return  插入结果
     */
    int addGpsOperateHisList(@Param("graph") String graph, @Param("list") List<GpsOperateHis> list);

    /**
     * 查询某个范围是上车
     * @param tableName 表名称
     * @param lngmin    最小经度
     * @param lngmax    最大经度
     * @param latmin    最小纬度
     * @param latmax    最大纬度
     * @return 返回数据集
     */
    List<GPS> selectGeoHashByTime(@Param("tableName") String tableName,
                                  @Param("lngmin") double lngmin,
                                  @Param("lngmax") double lngmax,
                                  @Param("latmin") double latmin,
                                  @Param("latmax") double latmax);

    /**
     * 更新第二张表的rowkey字段，为导入hbase做准备
     * @param tableName
     * @param rowKey
     * @param id
     * @return
     */
    int updateOpsHis(@Param("tableName") String tableName, @Param("rowKey") String rowKey,
                     @Param("id") long id);


    /**
     *  找出每个geohash中上下车总量，以小时为单位
     * @param tableName 表名
     * @param geoHashList   geohash集合
     * @return  每个geohash上下车的数量
     */
    List<Integer> diagramSelect(@Param("tableName") String tableName,
                                @Param("list") List<String> geoHashList);

    /**
     * 获得广州市某一天某个geohash的出租车总量
     * @param geoHash   geohash
     * @param tableName 表名
     * @return  geohash中心点和geohash中车的数量
     */
    List<CalculateGeoHash> selectGuangZhou(@Param("geoHash") String geoHash,
                                           @Param("tableName") String tableName);
}
