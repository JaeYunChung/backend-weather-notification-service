package com.example.publicdatanotification.open_api.domain.dust;

import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingEntity;
import com.example.publicdatanotification.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DustSettingRepository extends JpaRepository<DustSettingEntity, String> {
    Optional<DustSettingEntity> findByMember(Member member);
}
