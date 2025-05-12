package com.example.publicdatanotification.open_api.domain.dust.domain;

import java.util.Arrays;

public enum DustStatus{
    GOOD("좋음"),
    Common("보통"),
    Worse("나쁨");

    private final String status;

    DustStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public static DustStatus getLabel(String statusValue){
       return  Arrays.stream(DustStatus.values())
                .filter(value -> value.status.equals(statusValue))
                .findFirst().orElseThrow(()-> new IllegalArgumentException("해당 결과가 없습니다."));
    }
}
