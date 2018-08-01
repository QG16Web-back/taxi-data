package com.qg.taxi.model.action;

import lombok.Data;

/**
 * @author <a href="http://minsming.com">小铭</a>
 * Date: 2018/3/29
 * No struggle, talent how to match the willfulness.
 * Description: 司机收入
 */
@Data
public class DriverInfo {
    private String number;

    /**
     * 收入
     */
    private double income;

    /**
     * 空载公里数
     */
    private double emptyMileage;

    /**
     * 时间
     */
    private String day;

    private int hour;

}
