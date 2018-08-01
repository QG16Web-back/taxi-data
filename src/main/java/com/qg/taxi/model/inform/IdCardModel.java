package com.qg.taxi.model.inform;

import lombok.Data;

/**
 * @author Wilder Gao
 * time:2018/7/14
 * description：
 * motto: All efforts are not in vain
 */
@Data
public class IdCardModel {
    /**
     * 分别代表上车经度、上车纬度、下车经度、下车纬度
     *
     */
    private String cardId;
    private double onX;
    private double onY;
    private double offX;
    private double offY;

    public IdCardModel(String cardId, double onX, double onY, double offX, double offY) {
        this.cardId = cardId;
        this.onX = onX;
        this.onY = onY;
        this.offX = offX;
        this.offY = offY;
    }

    @Override
    public String toString() {
        return "["+onX+","+onY+","+offX+","+offY+"],\n";
    }
}
