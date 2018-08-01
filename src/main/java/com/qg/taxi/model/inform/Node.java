package com.qg.taxi.model.inform;

import lombok.Data;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：
 * motto: All efforts are not in vain
 */
@Data
public class Node implements Comparable<Node>{
    private String id;
    /**
     * GPS经纬度坐标
     */
    private double lat;
    private double lon;

    /**
     * 百度地图经纬度坐标
     */
    private double bdLat;
    private double bdLon;

    /**
     * 通过实现接口对路上的坐标点进行排序
     * @param o
     * @return
     */
    @Override
    public int compareTo(Node o) {
        return Double.compare(o.getBdLon(), this.getBdLon());
    }
}
