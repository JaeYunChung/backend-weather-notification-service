package com.example.publicdatanotification.open_api;

import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.member.repository.MemberRepository;
import com.example.publicdatanotification.open_api.domain.custom.CustomSettingEntity;
import com.example.publicdatanotification.open_api.domain.custom.CustomSettingRepository;
import com.example.publicdatanotification.open_api.domain.dust.DustSettingRepository;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingEntity;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingEntity;
import com.example.publicdatanotification.open_api.domain.temp.TempSettingRepository;
import com.example.publicdatanotification.time.TimeSettingEntity;
import com.example.publicdatanotification.time.TimeSettingEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SettingService {
    private final TempSettingRepository tempSettingRepository;
    private final DustSettingRepository dustSettingRepository;
    private final CustomSettingRepository customSettingRepository;
    private final MemberRepository memberRepository;
    private final TimeSettingEntityRepository timeSettingEntityRepository;

    @Transactional
    public void saveTempSetting(TempSettingEntity entity){
        Optional<TempSettingEntity> tempSettingEntity = tempSettingRepository.findByMember(entity.getMember());
        tempSettingEntity.ifPresent(settingEntity -> entity.setId(settingEntity.getId()));
        tempSettingRepository.save(entity);
    }
    @Transactional
    public void saveDustSetting(DustSettingEntity entity){
        Optional<DustSettingEntity> dustSettingEntity = dustSettingRepository.findByMember(entity.getMember());
        dustSettingEntity.ifPresent(settingEntity -> entity.setId(settingEntity.getId()));
        dustSettingRepository.save(entity);
    }
    @Transactional
    public void saveCustomSetting(CustomSettingEntity entity){
        Optional<CustomSettingEntity> customSettingEntity = customSettingRepository
                .findByMemberAndWeather(entity.getMember(), entity.getWeather());
        customSettingEntity.ifPresent(settingEntity -> entity.setId(settingEntity.getId()));
        customSettingRepository.save(entity);}
    @Transactional
    public List<CustomSettingEntity> findCustomSettings(Member member) {
        return customSettingRepository.findAllByMember(member)
                .stream()
                .filter(CustomSettingEntity::isSetting)
                .toList();
    }

    @Transactional
    public void saveTimeSetting(TimeSettingEntity time){
        timeSettingEntityRepository.save(time);
    }


    public String getRandomId(){
        return UUID.randomUUID().toString();
    }
}
