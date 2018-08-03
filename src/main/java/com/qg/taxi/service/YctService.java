package com.qg.taxi.service;

import com.qg.taxi.dto.RequestResult;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Map;

/**
 * Create by ming on 18-8-3 下午1:52
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
public interface YctService {

    /**
     * 获取上下车信息，并写入文件
     *
     * @param request request
     * @return request result
     * @throws ParseException     parseException
     * @throws IOException        ioException
     * @throws URISyntaxException URISyntaxException
     */
    RequestResult getOnOff(HttpServletRequest request) throws ParseException, IOException, URISyntaxException;
}
