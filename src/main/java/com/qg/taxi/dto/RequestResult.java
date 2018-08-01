package com.qg.taxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：
 * motto: All efforts are not in vain
 */
@Data
@AllArgsConstructor
public class RequestResult<T> {
    private  int state;
    private String stateInfo;
    private T data;

    public RequestResult(int state) {
        this.state = state;
    }
}
