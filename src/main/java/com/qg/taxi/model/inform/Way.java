package com.qg.taxi.model.inform;

import lombok.Data;

import java.util.List;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：路网信息，way代表一条路
 * motto: All efforts are not in vain
 */
@Data
public class Way {
    private String id;

    /**
     * 路名
     */
    private String name;

    private String nodeIdList;


    /**
     * 路上的点
     */
    private List<Node> nodes;
}
