package com.example.publicdatanotification.websocket.controller;


import com.example.publicdatanotification.open_api.OpenApiConnection;
import com.example.publicdatanotification.open_api.RainDataDto;
import com.example.publicdatanotification.websocket.LocationDataResponse;
import com.example.publicdatanotification.websocket.LocationInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketController extends TextWebSocketHandler {
    //기기ID → 세션
    private final Map<String, WebSocketSession> deviceSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final OpenApiConnection openApiConnection;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Received text message: " + payload);
        LocationInfoDto dto = objectMapper.readValue(payload, LocationInfoDto.class);

        String deviceId = dto.deviceId();
        deviceSessions.putIfAbsent(deviceId, session);
        WebSocketSession targetSession = deviceSessions.get(deviceId);

        String url = "";
        String path = "";
        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .path(path)
                .queryParam("nx", dto.longitude())
                .queryParam("ny", dto.latitude())
                .build().toUri();
        ResponseEntity<LocationDataResponse> response = restTemplate.getForEntity(uri, LocationDataResponse.class);
        RainDataDto rainDataDto = openApiConnection.getRainData(response.getBody());
        String returnValue = objectMapper.writeValueAsString(rainDataDto);
        if (targetSession != null && targetSession.isOpen()) {
            targetSession.sendMessage(new TextMessage(returnValue));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결 끊긴 세션 정리
        deviceSessions.values().removeIf(s -> s.getId().equals(session.getId()));
    }

}