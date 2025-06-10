package com.example.publicdatanotification.open_api.domain;

import com.example.publicdatanotification.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    protected String id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
