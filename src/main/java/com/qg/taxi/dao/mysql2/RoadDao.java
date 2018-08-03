package com.qg.taxi.dao.mysql2;

import com.qg.taxi.model.gps.GPS;
import com.qg.taxi.model.inform.Node;
import com.qg.taxi.model.inform.Way;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author:Wilder Gao
 * @time:2018/5/3
 * @Discription：
 */
@Mapper
@Repository
public interface RoadDao {
    /**
     * 返回GPS集合
     * @return
     */
    List<GPS> selectRoadGps();

    /**
     * 获得路
     * @return
     */
    List<Way> getWays();

    /**
     * 获得某条路的坐标点
     * @param nodeIdList
     * @return
     */
    List<Node> getRoadNode(@Param("list") List<String> nodeIdList);


}
