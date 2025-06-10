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
    public void setId(String id){
        super.id = id;
    }
    private float minTemp;
    private float maxTemp;
}
