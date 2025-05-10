package com.example.publicdatanotification.open_api.domain;

import com.example.publicdatanotification.open_api.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherSettingEntity {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
