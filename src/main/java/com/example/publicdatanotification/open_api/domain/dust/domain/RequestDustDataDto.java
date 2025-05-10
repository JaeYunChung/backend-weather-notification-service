package com.example.publicdatanotification.open_api.domain.dust.domain;

import lombok.Builder;

@Builder
public record RequestDustDataDto(
        String serviceKey,
        String returnType,
        String numOfRows,
        String pageNo,
        String searchDate,
        String InformCode) {
}
