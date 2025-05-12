package com.example.publicdatanotification.firebase;

import com.example.publicdatanotification.firebase.domain.NotificationSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSettingRepository extends JpaRepository<NotificationSettingEntity, String> {
}
