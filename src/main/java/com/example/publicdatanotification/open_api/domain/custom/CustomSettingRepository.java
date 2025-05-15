package com.example.publicdatanotification.open_api.domain.custom;

import com.example.publicdatanotification.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomSettingRepository extends JpaRepository<CustomSettingEntity, String> {
    List<CustomSettingEntity> findAllByMember(Member member);
}
