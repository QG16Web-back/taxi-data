package com.qg.taxi.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：
 * motto: All efforts are not in vain
 */
public interface HttpApiService {

    /**
     * 提交和接收json数据，接收后返回的是一个Map的对象
     *
     * @param url  请求url
     * @param json JSON字符串
     * @return Map<String,Object>
     * @throws IOException IOException
     */
    List doPostJsonMap(String url, String json) throws IOException;

    /**
     * 提交和接收doGet请求
     *
     * @param url 请求url
     * @return map结果
     */
    Map<String, Object> doGetJsonMap(String url) throws IOException;

    /**
     * 带有参数的doGet请求
     * @param url url
     * @param map 参数
     * @return 结果
     * @throws URISyntaxException
     * @throws IOException
     */
    Map<String, Object> doGet(String url, Map<String, Object> map)
            throws URISyntaxException, IOException;


}
