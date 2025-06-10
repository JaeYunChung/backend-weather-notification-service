package com.example.publicdatanotification.open_api.domain.custom;

import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.open_api.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomSettingRepository extends JpaRepository<CustomSettingEntity, String> {
    List<CustomSettingEntity> findAllByMember(Member member);

    Optional<CustomSettingEntity> findByMemberAndWeather(Member member, Weather weather);

    boolean existsByMemberAndWeather(Member member, Weather weather);
}
