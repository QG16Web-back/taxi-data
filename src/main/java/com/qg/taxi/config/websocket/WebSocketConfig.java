package com.qg.taxi.config.websocket;

import com.qg.taxi.handler.GpsWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author Wilder Gao
 * time:2018/7/30
 * description：
 * motto: All efforts are not in vain
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final GpsWebSocketHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(handler, "/ws").addInterceptors(new HandShake()).setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(handler,"/sockjs/ws").addInterceptors(new HandShake()).withSockJS();
    }

    @Autowired
    public WebSocketConfig(GpsWebSocketHandler handler){
        this.handler = handler;
    }

}
