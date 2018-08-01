package com.qg.taxi.model.gps;

import lombok.Data;

import java.util.Date;

/**
 * @author Wilder Gao
 * time:2018/7/23
 * description：
 * motto: All efforts are not in vain
 */
@Data
public class GpsLog {
    /**
     *  增加的属性，为了方便MySQL操作，已经设置添加时自动增加主键
     */
    private long id;
    /**
     * 车牌号
     */
    private String licensePlateNo;
    /**
     * GPS时间
     */
    private Date gpsDate;
    /**
     * 记录时间
     */
    private Date inDate;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 速度
     */
    private int speed;
    /**
     * 方向
     */
    private int direction;
    /**
     * 高度
     */
    private double height;
    /**
     * 车辆状态 (1防盗 ; 2 防劫; 4空车 ; 5 重车; 6 点火; 7 熄火)
     */
    private String carStat1;

    private String carStat2;
    /**
     * 经纬度所在geohash
     */
    private String geoHash;
    /**
     * 为HBase 设计的行键
     */
    private String rowKey;
    /**
     * 小时时间戳
     */
    private int hourRepre;

}
