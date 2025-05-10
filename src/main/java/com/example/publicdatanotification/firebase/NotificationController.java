package com.example.publicdatanotification.firebase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/new/notification")
    public ResponseEntity<Void> saveToken(@RequestBody String token, @RequestParam String memberId) {
        notificationService.saveDeviceToken(memberId, token);
        return ResponseEntity.ok().build();
    }
}

