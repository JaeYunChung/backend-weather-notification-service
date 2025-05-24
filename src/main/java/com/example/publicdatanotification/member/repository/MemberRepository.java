package com.example.publicdatanotification.member.repository;

import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.open_api.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {
    @Modifying
    @Query("select m from Member m where m in (select n.member " +
            "from CustomSettingEntity n where n.weather = :weather and n.isSetting is true)")
    List<Member> findAllSettingWeatherNotification(Weather weather);

    @Modifying
    @Query("update Member m set m.longitude=:nx, m.latitude=:ny where m.id=:id")
    void updateMemberLocation(String id, int longitude, int latitude);
}
