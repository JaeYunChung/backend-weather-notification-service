package com.example.publicdatanotification.firebase;

import com.example.publicdatanotification.open_api.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTokenRepository extends JpaRepository<NotificationToken, String> {
    Optional<NotificationToken> findByMember(Member member);
}
