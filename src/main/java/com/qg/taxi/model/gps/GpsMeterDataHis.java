package com.qg.taxi.model.gps;

import lombok.Data;

import java.util.Date;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：基于第一张数据库表建立的实体类
 * motto: All efforts are not in vain
 */
@Data
public class GpsMeterDataHis {
    /**
     * 数据序号
     */
    private long id;
    /**
     * 车牌号码
     */
    private String plateNo;
    /**
     * 司机卡编号
     */
    private String chauffeurNo;
    /**
     * 运营开始时间
     */
    private Date workBeginTime;
    /**
     * 运营结束时间
     */
    private Date workEndTime;
    /**
     * 运营结束单价
     */
    private double price;
    /**
     * 载客里程公里
     */
    private double loadMileage;
    /**
     * 本次计价金额
     */
    private double meterMoney;
    /**
     * 空载里程公里
     */
    private double emptyMileage;
    /**
     * 空车开始时间
     */
    private Date emptyBeginTime;
    /**
     * 支付方式
     */
    private String payMode;
    /**
     * 时间代表，以小时为代表，后面需要以小时进行统计
     */
    private int timeRepre;

    /**
     * 行键
     */
    private String rowKey;
}
