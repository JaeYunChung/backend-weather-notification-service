package com.example.publicdatanotification.open_api.domain.dust.domain;

public record DustSettingRequestDto(
        String memberId,
        String pm10Setting,
        String pm25Setting
) {
}
