package com.example.publicdatanotification.websocket;

public record LocationInfoDto(String memberId, String deviceId, double latitude, double longitude) {
}
