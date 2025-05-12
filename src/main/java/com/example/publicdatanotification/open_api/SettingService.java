package com.example.publicdatanotification.open_api;

import com.example.publicdatanotification.open_api.domain.dust.DustSettingRepository;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingEntity;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingEntity;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SettingService {
    private final TempSettingRepository tempSettingRepository;
    private final DustSettingRepository dustSettingRepository;
    @Transactional
    public void saveTempSetting(TempSettingEntity entity){
        tempSettingRepository.save(entity);
    }
    @Transactional
    public void saveDustSetting(DustSettingEntity entity){
        dustSettingRepository.save(entity);
    }

    public String getRandomId(){
        return UUID.randomUUID().toString();
    }
}
