package com.example.publicdatanotification.time;

import com.example.publicdatanotification.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSettingEntityRepository extends JpaRepository<TimeSettingEntity, Long> {

    List<TimeSettingEntity> findAllByMember(Member member);
}
