package com.example.publicdatanotification.open_api.domain.custom;

import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.open_api.domain.Weather;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomSettingEntity {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    private Weather weather;
    private boolean isSetting;
}
