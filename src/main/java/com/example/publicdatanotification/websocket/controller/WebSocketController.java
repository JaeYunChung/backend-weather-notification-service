package com.example.publicdatanotification.websocket.controller;


import com.example.publicdatanotification.member.repository.MemberRepository;
import com.example.publicdatanotification.open_api.OpenApiConnection;
import com.example.publicdatanotification.open_api.WeatherDataDto;
import com.example.publicdatanotification.open_api.domain.temp.TempAndRainService;
import com.example.publicdatanotification.translate.MapInfo;
import com.example.publicdatanotification.websocket.LocationInfoDto;
import com.example.publicdatanotification.websocket.MainScreenInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketController extends TextWebSocketHandler {
    //기기ID → 세션
    private final Map<String, WebSocketSession> deviceSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final OpenApiConnection openApiConnection;
    private final MemberRepository memberRepository;
    private final TempAndRainService tempAndRainService;
    private final MapInfo mapInfo;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Received text message: " + payload);
        LocationInfoDto dto = objectMapper.readValue(payload, LocationInfoDto.class);

        String deviceId = dto.deviceId();
        float longitude = (float) dto.longitude();
        float latitude = (float) dto.latitude();


        WebSocketSession oldSession = deviceSessions.get(deviceId);
        if (oldSession != null && oldSession.isOpen() && !oldSession.getId().equals(session.getId())) {
            try {
                oldSession.close();
                log.info("기존 세션({})을 닫았습니다. deviceId: {}", oldSession.getId(), deviceId);
            } catch (Exception e) {
                log.warn("기존 세션 닫기 실패: {}", e.getMessage());
            }
        }

        // 항상 최신 세션으로 갱신
        deviceSessions.put(deviceId, session);

        WebSocketSession targetSession = deviceSessions.get(deviceId);


        int[] result = mapInfo.transSlatToGrid(longitude, latitude);

        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY) == 9){
            memberRepository.updateMemberLocation(dto.memberId(), result[0], result[1]);
        }

        List<WeatherDataDto> weatherDataDto = openApiConnection.getWeatherDataByTransLoc(result[0], result[1]);


//        for (int i = 0 ; i < 10; i++){
//            log.info("check");
//            tempAndRainService.sendTempNotification(getTempInfo(weatherDataDto));
//            if (!getRainInfo(weatherDataDto).isEmpty()) tempAndRainService.sendRainNotification();
//            Thread.sleep(1000);
//        }

        MainScreenInfoDto info = makeMainScreenInfo(weatherDataDto);
        String returnValue = objectMapper.writeValueAsString(info);

        if (targetSession != null && targetSession.isOpen()) {
            targetSession.sendMessage(new TextMessage(returnValue));
        }
    }


    public MainScreenInfoDto makeMainScreenInfo(List<WeatherDataDto> data){
        List<WeatherDataDto> rainDataList = tempAndRainService.getRainInfo(data);
        MainScreenInfoDto dto;
        if (rainDataList.isEmpty()){
            dto = new MainScreenInfoDto("🔆", "맑음", "오늘은 우산 없는 날");
        }
        else{
            dto = new MainScreenInfoDto("☔", "비", "우산을 챙겨주세요");
        }
        return dto;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결 끊긴 세션 정리
        deviceSessions.values().removeIf(s -> s.getId().equals(session.getId()));
    }

}