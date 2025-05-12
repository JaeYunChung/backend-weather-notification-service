package com.example.publicdatanotification.open_api.domain.temp;

import com.example.publicdatanotification.open_api.domain.WeatherSettingEntity;
import com.example.publicdatanotification.member.Member;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TempSettingEntity extends WeatherSettingEntity {
    public TempSettingEntity(String id, Member member, float minTemp, float maxTemp){
        super(id, member);
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }
    private float minTemp;
    private float maxTemp;
}
