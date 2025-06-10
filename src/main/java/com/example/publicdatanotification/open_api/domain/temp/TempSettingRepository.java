package com.example.publicdatanotification.open_api.domain.temp;
import com.example.publicdatanotification.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TempSettingRepository extends JpaRepository<TempSettingEntity, String> {
    Optional<TempSettingEntity> findByMember(Member member);
}
