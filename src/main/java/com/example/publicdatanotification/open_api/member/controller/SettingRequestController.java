package com.example.publicdatanotification.open_api.member.controller;

import com.example.publicdatanotification.open_api.domain.temp.TempSettingEntity;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingRepository;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingRequestDto;
import com.example.publicdatanotification.open_api.member.Member;
import com.example.publicdatanotification.open_api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SettingRequestController {

    private final MemberRepository memberRepository;
    private final TempSettingRepository tempSettingRepository;

    @PostMapping("/temp/setting/")
    public ResponseEntity<?> saveTempSettingInfo(@RequestBody TempSettingRequestDto dto){
        List<Member> members = memberRepository.findAll();
        log.info("memberId : {}", members.get(0).getId());
        log.info("memberId : {}", dto.memberId());
        Member member = memberRepository.findById(dto.memberId()).orElseThrow(() -> new IllegalArgumentException("해당 아이디가 없습니다."));
        String id = UUID.randomUUID().toString();
        TempSettingEntity entity = TempSettingEntity.builder()
                .id(id)
                .member(member)
                .maxTemp(dto.maxTemp())
                .minTemp(dto.minTemp())
                .build();
        tempSettingRepository.save(entity);
        log.info("정상적으로 저장되었습니다.");
        return ResponseEntity.status(HttpStatus.OK)
                .body("정상적으로 저장되었습니다.");
    }
}
