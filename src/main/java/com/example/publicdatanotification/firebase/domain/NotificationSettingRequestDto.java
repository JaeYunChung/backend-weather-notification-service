package com.example.publicdatanotification.firebase.domain;

import com.example.publicdatanotification.open_api.domain.Weather;

public record NotificationSettingRequestDto(String memberId, String weather) {
}
