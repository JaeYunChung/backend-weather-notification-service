package com.example.publicdatanotification.open_api;

import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.open_api.domain.custom.CustomSettingEntity;
import com.example.publicdatanotification.open_api.domain.custom.CustomSettingRepository;
import com.example.publicdatanotification.open_api.domain.dust.DustSettingRepository;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingEntity;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingEntity;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SettingService {
    private final TempSettingRepository tempSettingRepository;
    private final DustSettingRepository dustSettingRepository;
    private final CustomSettingRepository customSettingRepository;

    @Transactional
    public void saveTempSetting(TempSettingEntity entity){
        tempSettingRepository.save(entity);
    }
    @Transactional
    public void saveDustSetting(DustSettingEntity entity){
        dustSettingRepository.save(entity);
    }
    @Transactional
    public void saveCustomSetting(CustomSettingEntity entity){customSettingRepository.save(entity);}
    @Transactional
    public List<CustomSettingEntity> findCustomSettings(Member member){return customSettingRepository.findAllByMember(member);}

    public String getRandomId(){
        return UUID.randomUUID().toString();
    }
}
