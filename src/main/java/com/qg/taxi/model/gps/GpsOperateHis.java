package com.qg.taxi.model.gps;

import lombok.Data;

import java.util.Date;

/**
 * 运营记录历史数据，根据表2创建的实体类
 *
 * @author Wilder Gao
 * date 2018/3/25
 */
@Data
public class GpsOperateHis {
    private long id;
    /**
     * 设备Id
     */
    private String equipmentId;
    /**
     * 车牌号
     */
    private String plateNo;
    /**
     * 公司Id
     */
    private long companyId;
    /**
     * 司机卡编号
     */
    private String chauffeurNo;
    /**
     * //车队代码
     */
    private String teamCode;
    /**
     *   //空车里程
     */
    private double emptyMile;
    /**
     *  //空车开始时间
     */
    private Date emptyBeginTime;
    /**
     * //运营开始时间
     */
    private Date workBeginTime;
    /**
     * //运营结束时间
     */
    private Date workEndTime;
    /**
     * //运营单价
     */
    private double unitPrice;
    /**
     * //运营里程
     */
    private double loadMile;
    /**
     * //慢速计时
     */
    private int slowCountTime;
    /**
     * //运营金额
     */
    private double operateMoney;
    /**
     * //评价。0未评价，1非常满意，2满意，3不满意
     */
    private int evaluate;
    /**
     * //交易标识码
     */
    private String tradeCode;
    /**
     * //上车经度
     */
    private double getOnLongitude;
    /**
     * //上车纬度
     */
    private double getOnLatitude;
    /**
     * //下车经度
     */
    private double getOffLongitude;
    /**
     * //下车纬度
     */
    private double getOffLatitude;
    /**
     * //根据经纬度生成geoHash
     */
    private String geoHash;
    /**
     * //时间代表
     */
    private int timeRepre;
    /**
     *   //为插入Hbase做准备的行键
     */
    private String rowKey;

}
