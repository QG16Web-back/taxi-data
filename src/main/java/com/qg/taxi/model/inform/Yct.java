package com.qg.taxi.model.inform;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Create by ming on 18-8-3 上午11:41
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Yct {

    private Integer meterDataNo;

    /**
     * 车牌号
     */
    private String plateNo;

    /**
     * 运营开始时间
     */
    private Date workBeginTime;

    /**
     * 运营结束时间
     */
    private Date workEndTime;

    /**
     * 羊城通消费逻辑卡号
     */
    private String yctLogicCardNo;
}
