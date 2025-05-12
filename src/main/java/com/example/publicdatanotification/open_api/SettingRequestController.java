package com.example.publicdatanotification.open_api;

import com.example.publicdatanotification.member.MemberService;
import com.example.publicdatanotification.open_api.domain.dust.DustSettingRepository;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingEntity;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingRequestDto;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustStatus;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingEntity;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingRepository;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingRequestDto;
import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SettingRequestController {

    private final MemberService memberService;
    private final SettingService settingService;
    private final DustSettingRepository dustSettingRepository;

    @PostMapping("/temp/setting/")
    public ResponseEntity<?> saveTempSettingInfo(@RequestBody TempSettingRequestDto dto){
        Member member = memberService.findMemberById(dto.memberId());
        String id = UUID.randomUUID().toString();
        TempSettingEntity entity = TempSettingEntity.builder()
                .id(id)
                .member(member)
                .maxTemp(dto.maxTemp())
                .minTemp(dto.minTemp())
                .build();
        settingService.saveTempSetting(entity);
        log.info("온도 설정값이 정상적으로 저장되었습니다.");
        return ResponseEntity.status(HttpStatus.OK)
                .body("온도 설정값이 정상적으로 저장되었습니다.");
    }

    @PostMapping("/dust/setting/")
    public ResponseEntity<?> saveDustSettingInfo(@RequestBody DustSettingRequestDto dto){
        Member member = memberService.findMemberById(dto.memberId());
        String id = UUID.randomUUID().toString();
        DustSettingEntity entity = DustSettingEntity.builder()
                .id(id)
                .member(member)
                .pm10DustStatus(DustStatus.valueOf(dto.pm10Setting()))
                .pm25DustStatus(DustStatus.valueOf(dto.pm25Setting()))
                .build();
        settingService.saveDustSetting(entity);
        log.info("미세먼지 설정값이 정상적으로 설정되었습니다.");
        return ResponseEntity.status(HttpStatus.OK)
                .body("미세먼지 설정값이 정상적으로 저장되었습니다.");
    }
}
