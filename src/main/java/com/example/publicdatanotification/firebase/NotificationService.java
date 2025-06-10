package com.example.publicdatanotification.firebase;

import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.member.repository.MemberRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationTokenRepository notificationTokenRepository;

    @Transactional
    public void saveDeviceToken(String memberId, String token){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
        String tokenId = UUID.randomUUID().toString();
        Optional<NotificationToken> tokenEntity = notificationTokenRepository.findByMember(member);
        if(tokenEntity.isPresent()){
            tokenId = tokenEntity.get().getId();
        }
        log.info(token);
        NotificationToken notificationToken = NotificationToken.builder()
                .id(tokenId)
                .member(member)
                .token(token)
                .build();
        notificationTokenRepository.save(notificationToken);
    }

    public void send(String token, String title, String message) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(message)
                .build();
        Message m = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();
        String response = FirebaseMessaging.getInstance().send(m);
        log.info("정상적으로 전송되었습니다 : {}", response);
    }
//    public void saveNotificationSetting(NotificationSettingEntity entity){
//        notificationSettingRepository.save(entity);
//    }

}
