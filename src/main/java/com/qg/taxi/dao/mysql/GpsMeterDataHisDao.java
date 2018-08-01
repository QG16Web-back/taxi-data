package com.qg.taxi.dao.mysql;

import com.qg.taxi.model.action.DriverInfo;
import com.qg.taxi.model.action.TakeTaxiCount;
import com.qg.taxi.model.excel.CountModel;
import com.qg.taxi.model.gps.Gps;
import com.qg.taxi.model.gps.GpsMeterDataHis;
import com.qg.taxi.model.gps.GpsOperateHis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：mysql 对于第一张表的操作
 * motto: All efforts are not in vain
 */
@Mapper
@Repository
@Scope("prototype")
public interface GpsMeterDataHisDao {
    /**
     * 往 mysql插入数据
     * @param graph 表名
     * @param list 集合
     * @return
     */
    int addGpsMeterHisList(@Param("graph") String graph, @Param("list") List<GpsMeterDataHis> list);

    /**
     * 以geoHash、小时为单位统计出租车的总量
     * @param tableName
     * @param geoHash
     * @return
     */
    List<CountModel> countByTimeGeoHash(@Param("tableName") String tableName,
                                        @Param("geoHash") String geoHash);

    /**
     * 统计某个小时车的空间分布
     * @param tableName
     * @param timeRepre
     * @return
     */
    List<CountModel> countByDay(@Param("tableName") String tableName,
                                @Param("timeRepre") int timeRepre);

    /**
     * 查询某天出租车上下车总量，以小时为单位
     * @param table 表名
     * @return 结果集合
     */
    List<TakeTaxiCount> totalCount(@Param("table") String table);

    /**
     * 查询某天出租车收入情况
     * @param table 表名
     * @return  结果集合
     */
    List<DriverInfo> getIncome(@Param("table") String table);

    /**
     * 查询某个时间段的收入情况（时间段自定义）
     * @param table 表名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return  结果集合
     */
    List<DriverInfo> getIncomeByCustomQuery(@Param("table") String table, @Param("startTime") int startTime, @Param("endTime") int endTime);

    /**
     * 查询某段时间上车情况（时间段自定义）
     * @param table 表名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return  结果集合
     */
    List<TakeTaxiCount> takeTaxiCount(@Param("table") String table, @Param("startTime") int startTime, @Param("endTime") int endTime);

    /**
     * 查询某一天的空载数量（按照车牌分组）
     * @param table 表名
     * @return 结果集合
     */
    List<DriverInfo> getEmptyMileage(@Param("table") String table);

    /**
     * 自定义时间段出租车的空载情况
     * @param table 表名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return  结果集合
     */
    List<DriverInfo> getEmptyMileageByCustomQuery(@Param("table") String table, @Param("startTime") int startTime, @Param("endTime") int endTime);

    /**
     * 查询某一天出租车的收入情况，以小时为单位
     * @param table 表名
     * @return  结果集合
     */
    List<DriverInfo> getIncomeByHour(@Param("table") String table);

    /**
     * 查询某天出租车空载情况，以小时为单位
     * @param table 表名
     * @return  结果集合
     */
    List<DriverInfo> getEmptyMileageByHour(@Param("table") String table);

    /**
     * 获得Id、车牌、时间等信息，为后期组合成rowkey导入hbase做准备
     * @param tableName 表名
     * @return  结果集合
     */
    List<GpsOperateHis> getOpsHisData(@Param("tableName") String tableName);

    /**
     * 得到一天广州市以geohash为单位统计总量，小时划分
     * @param tableName 表名
     * @return  总量结果集合
     */
    List<Gps> geoHashCount(@Param("tableName") String tableName);

    /**
     * 获得第二张表的信息
     * @param tableName 表名称
     * @return  结果集
     */
    List<GpsMeterDataHis> getMeterDataHisData(@Param("tableName") String tableName);

}
