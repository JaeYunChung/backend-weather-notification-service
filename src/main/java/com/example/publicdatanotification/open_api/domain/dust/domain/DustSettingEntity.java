package com.example.publicdatanotification.open_api.domain.dust.domain;

import com.example.publicdatanotification.open_api.domain.WeatherSettingEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DustSettingEntity extends WeatherSettingEntity {
    @Enumerated(EnumType.STRING)
    private DustStatus pm10DustStatus;

    @Enumerated(EnumType.STRING)
    private DustStatus pm25DustStatus;

    public void setId(String id){
        super.id = id;
    }
}
