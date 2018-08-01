package com.qg.taxi.service.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.qg.taxi.service.HttpApiService;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class HttpApiServiceImpl implements HttpApiService {

    private int OK = 200;
    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private RequestConfig config;

    private Gson gson = new Gson();


    @Override
    public List doPostJsonMap(String url, String json) throws IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.config);

        if (json != null) {
            // 构造一个form表单式的实体
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = this.httpClient.execute(httpPost);
            String responseToString = EntityUtils.toString(response.getEntity(), "utf-8");
            return gson.fromJson(responseToString, List.class);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> doGetJsonMap(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == OK) {
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            return gson.fromJson(result, new TypeToken<Map<String, Object>>() {
            }.getType());
        }
        return null;
    }

    @Override
    public Map<String, Object> doGet(String url, Map<String, Object> map) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        // 调用不带参数的get请求
        return this.doGetJsonMap(uriBuilder.build().toString());
    }
}
