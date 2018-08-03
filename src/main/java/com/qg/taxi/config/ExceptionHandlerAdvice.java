package com.qg.taxi.config;

import com.qg.taxi.dto.RequestResult;
import com.qg.taxi.exception.TaxiException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Create by ming on 18-8-3 下午2:36
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {

    @ExceptionHandler(TaxiException.class)
    public RequestResult handleException(TaxiException e) {
        return new RequestResult<>(e.getStateEnum(), null);
    }
}
