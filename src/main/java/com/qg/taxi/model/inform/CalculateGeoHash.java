package com.qg.taxi.model.inform;

import lombok.Data;

/**
 * @author Wilder Gao
 * time:2018/5/4
 * Description：geohash的中心坐标和这个geohash的出租车数量
 */
@Data
public class CalculateGeoHash {
    private int x;
    private int y;
    private String geoHash;
    private int count;
}
