package com.qg.taxi.dto;

import com.qg.taxi.enumeration.StateEnum;
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
    private String info;
    private T data;

    public RequestResult(int state) {
        this.state = state;
    }

    public RequestResult(int state, String stateInfo) {
        this.state = state;
        this.info = stateInfo;
    }

    public RequestResult(StateEnum stateEnum, T data) {
        this.state = stateEnum.getState();
        this.info = stateEnum.getInfo();
        this.data = data;
    }

    public RequestResult(StateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.info = stateEnum.getInfo();
    }
}
