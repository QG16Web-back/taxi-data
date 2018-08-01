package com.qg.taxi.dao.oracle;

import com.qg.taxi.model.gps.GpsMeterDataHis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wilder Gao
 * time:2018/7/23
 * description：
 * motto: All efforts are not in vain
 */
@Mapper
@Repository
@Scope("prototype")
public interface OracleGpsMeterDataHisDao {
    /**
     * 从oracle数据库中查询第二张表（出租车上下车表）指定行的数据
     * @param start 开始行
     * @param end   结束行
     * @return  数据集合
     */
    List<GpsMeterDataHis> selectMeterDataHisByNum(@Param("start") long start, @Param("end") long end);
}
