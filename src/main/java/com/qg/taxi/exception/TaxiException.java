package com.qg.taxi.exception;

import com.qg.taxi.enumeration.StateEnum;

/**
 * Create by ming on 18-8-3 下午2:37
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
public class TaxiException  extends RuntimeException{

    private StateEnum stateEnum;

    public TaxiException(StateEnum stateEnum) {
        super(stateEnum.getInfo());
        this.stateEnum = stateEnum;
    }

    public StateEnum getStateEnum() {
        return stateEnum;
    }
}
