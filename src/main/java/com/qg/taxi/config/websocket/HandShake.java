package com.qg.taxi.config.websocket;

import com.qg.taxi.hbase.Login;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.IOException;
import java.util.Map;

/**
 * @author Wilder Gao
 * time:2018/7/30
 * description：
 * motto: All efforts are not in vain
 */
@Slf4j
@Component
public class HandShake implements HandshakeInterceptor {

    private static Configuration conf;

    public static Configuration getConf() {
        return conf;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        log.info("握手之前: " + serverHttpRequest.getRemoteAddress().toString() + "进行初始化");
        try {
            Login.submitLogin(conf);
        } catch (IOException e) {
            log.error("Failed to login because ", e);
        }
        log.info("验证成功");
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        log.info("握手之后: " + serverHttpRequest.getRemoteAddress().toString());
    }
}
