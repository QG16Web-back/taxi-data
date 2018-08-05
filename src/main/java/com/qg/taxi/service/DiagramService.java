package com.qg.taxi.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Wilder Gao
 * time:2018/8/5
 * description：
 * motto: All efforts are not in vain
 */
public interface DiagramService {

    /**
     * 区域+模型选择进行预测
     * @param latmin
     * @param lonmin
     * @param latmax
     * @param lonmax
     * @param date
     * @param model
     * @return
     * @throws Exception
     */
    Map<Integer, List> clientDataMining(double latmin, double lonmin,
                                               double latmax, double lonmax,
                                               Date date, String model) throws Exception;
}
