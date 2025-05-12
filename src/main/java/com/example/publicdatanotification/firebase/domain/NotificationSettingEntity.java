package com.example.publicdatanotification.firebase.domain;

import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.open_api.domain.Weather;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSettingEntity {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Weather weather;
}
