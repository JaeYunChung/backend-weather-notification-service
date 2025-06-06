package com.example.publicdatanotification.websocket;

import com.example.publicdatanotification.member.repository.MemberRepository;
import com.example.publicdatanotification.open_api.OpenApiConnection;
import com.example.publicdatanotification.open_api.domain.temp.TempAndRainService;
import com.example.publicdatanotification.translate.MapInfo;
import com.example.publicdatanotification.websocket.controller.WebSocketController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final OpenApiConnection openApiConnection;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final TempAndRainService tempAndRainService;
    private final MapInfo mapInfo;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/ws").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler myWebSocketHandler() {
        return new WebSocketController(objectMapper, openApiConnection, memberRepository, tempAndRainService, mapInfo);
    }
}
