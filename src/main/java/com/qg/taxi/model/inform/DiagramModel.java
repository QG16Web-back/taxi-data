package com.qg.taxi.model.inform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：用httpClient发送给数据挖掘预测的实体类
 * motto: All efforts are not in vain
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagramModel {
    private String model;
    private List<Integer> countList;
}
