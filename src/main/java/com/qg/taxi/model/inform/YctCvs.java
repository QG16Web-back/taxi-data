package com.qg.taxi.model.inform;

import lombok.Data;

/**
 * Create by ming on 18-8-3 上午11:51
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Data
public class YctCvs {

    private String PLATENO;

    private double GET_OFF_LATITUDE;

    private double GET_ON_LATITUDE;

    private double GET_OFF_LONGITUDE;

    private double GET_ON_LONGITUDE;

    private String WORK_END_TIME;

    private String WORK_BEGIN_TIME;

    private String GET_OFF_STREET;

    private String GET_ON_STREET;
}
