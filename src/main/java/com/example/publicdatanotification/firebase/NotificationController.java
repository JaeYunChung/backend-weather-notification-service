package com.example.publicdatanotification.firebase;

import com.example.publicdatanotification.firebase.domain.NotificationSettingEntity;
import com.example.publicdatanotification.firebase.domain.NotificationSettingRequestDto;
import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.member.MemberService;
import com.example.publicdatanotification.open_api.SettingService;
import com.example.publicdatanotification.open_api.domain.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MemberService memberService;
    private final SettingService settingService;

    @PostMapping("/new/notification")
    public ResponseEntity<Void> saveToken(@RequestBody String token, @RequestParam String memberId) {
        notificationService.saveDeviceToken(memberId, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/setting/notification")
    public ResponseEntity<?> saveNotificationSetting(@RequestBody NotificationSettingRequestDto dto){
        Member member = memberService.findMemberById(dto.memberId());
        String id = settingService.getRandomId();
        NotificationSettingEntity entity = NotificationSettingEntity.builder()
                .id(id)
                .member(member)
                .weather(Weather.valueOf(dto.weather()))
                .build();
        notificationService.saveNotificationSetting(entity);
        return ResponseEntity.ok().build();
    }
}

