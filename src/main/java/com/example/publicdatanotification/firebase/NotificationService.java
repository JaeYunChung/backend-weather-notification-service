package com.example.publicdatanotification.firebase;


import com.example.publicdatanotification.open_api.member.Member;
import com.example.publicdatanotification.open_api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationTokenRepository notificationTokenRepository;
    private final RestTemplate restTemplate;
    private final String fcmUrl = "https://fcm.googleapis.com/fcm/send";

    @Value("${firebase.secretKey}")
    private String secretKey;


    public void saveDeviceToken(String memberId, String token){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
        String tokenId = UUID.randomUUID().toString();
        NotificationToken notificationToken = new NotificationToken(tokenId, member, token);
        notificationTokenRepository.save(notificationToken);
    }
    @Scheduled(cron = "0 0 9 * * *")
    public void send(String token, String title, String message){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("key", secretKey);
        headers.setAccessControlRequestMethod(HttpMethod.POST);

        NotificationDto dto = new NotificationDto(token);
        dto.setNotification(NotificationDto.Notification
                .builder()
                .title(title)
                .body(message)
                .mutable_content("true")
                .build());

        HttpEntity<NotificationDto> entity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(fcmUrl, entity, String.class);
        if (response.getStatusCode().equals("200")){
            log.info("푸시 알림 전송 성공");
        }
        else{
            log.error("푸시 알림 전송 실패");
        }
    }

}
