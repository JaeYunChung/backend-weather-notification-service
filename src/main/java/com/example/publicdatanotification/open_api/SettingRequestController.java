package com.example.publicdatanotification.open_api;

import com.example.publicdatanotification.member.MemberService;
import com.example.publicdatanotification.open_api.domain.Weather;
import com.example.publicdatanotification.open_api.domain.custom.CustomSettingEntity;
import com.example.publicdatanotification.open_api.domain.custom.CustomSettingRequestDto;
import com.example.publicdatanotification.open_api.domain.custom.CustomSettingResponseDto;
import com.example.publicdatanotification.open_api.domain.dust.DustSettingRepository;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingEntity;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingRequestDto;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustStatus;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingEntity;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingRequestDto;
import com.example.publicdatanotification.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SettingRequestController {

    private final MemberService memberService;
    private final SettingService settingService;


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
                .pm10DustStatus(DustStatus.getLabel(dto.pm10Setting()))
                .pm25DustStatus(DustStatus.getLabel(dto.pm25Setting()))
                .build();
        settingService.saveDustSetting(entity);
        log.info("미세먼지 설정값이 정상적으로 설정되었습니다.");
        return ResponseEntity.status(HttpStatus.OK)
                .body("미세먼지 설정값이 정상적으로 저장되었습니다.");
    }

    @PostMapping("custom/setting/")
    public ResponseEntity<?> saveCustomSettingInfo(@RequestBody CustomSettingRequestDto dto){
        Member member = memberService.findMemberById(dto.memberId());
        boolean settingValue = dto.setting().equals("true");
        String id = UUID.randomUUID().toString();
        CustomSettingEntity entity = CustomSettingEntity.builder()
                .id(id)
                .member(member)
                .weather(Weather.valueOf(dto.weather()))
                .isSetting(settingValue)
                .build();
        settingService.saveCustomSetting(entity);
        log.info("설정값이 정상적으로 설정되었습니다.");
        return ResponseEntity.status(HttpStatus.OK)
                .body("설정값이 정상적으로 저장되었습니다.");
    }

    @GetMapping("/custom/setting/")
    public ResponseEntity<CustomSettingResponseDto> sendCustomSettingInfo(@RequestParam String memberId){
        Member member = memberService.findMemberById(memberId);
        boolean rain = false, dust = false, temp = false;
        List<CustomSettingEntity> settings = settingService.findCustomSettings(member);
        for(CustomSettingEntity setting : settings){
            if (setting.getWeather().equals(Weather.RAIN)) rain = true;
            else if (setting.getWeather().equals(Weather.DUST)) dust = true;
            else temp = true;
        }
        CustomSettingResponseDto dto = new CustomSettingResponseDto(rain, dust, temp);
        log.info("값이 정상적으로 반환되었습니다.");
        return ResponseEntity.ok(dto);
    }
}
