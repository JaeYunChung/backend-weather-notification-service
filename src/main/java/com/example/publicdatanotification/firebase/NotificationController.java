package com.example.publicdatanotification.firebase;


import com.example.publicdatanotification.member.MemberService;
import com.example.publicdatanotification.open_api.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/new/notification")
    public ResponseEntity<Void> saveToken(@RequestBody NotificationTokenDto dto, @RequestParam String memberId) {
        notificationService.saveDeviceToken(memberId, dto.token());
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/setting/notification")
//    public ResponseEntity<?> saveNotificationSetting(@RequestBody NotificationSettingRequestDto dto){
//        Member member = memberService.findMemberById(dto.memberId());
//        String id = settingService.getRandomId();
//        NotificationSettingEntity entity = NotificationSettingEntity.builder()
//                .id(id)
//                .member(member)
//                .weather(Weather.valueOf(dto.weather()))
//                .build();
//        notificationService.saveNotificationSetting(entity);
//        return ResponseEntity.ok().build();
//    }
}

