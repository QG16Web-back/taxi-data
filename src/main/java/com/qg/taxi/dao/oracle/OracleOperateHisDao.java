package com.qg.taxi.dao.oracle;

import com.qg.taxi.model.gps.GpsOperateHis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface OracleOperateHisDao {
    /**
     * 从oracle数据中查询第二张表（出租车历史表）指定行的数据
     * @param start 开始行
     * @param end   结束行
     * @return  数据集合
     */
    List<GpsOperateHis> selectOperateHisByNum(@Param("start") long start, @Param("end") long end);
}
