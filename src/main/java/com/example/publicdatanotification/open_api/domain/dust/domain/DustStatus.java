package com.example.publicdatanotification.open_api.domain.dust.domain;

public enum DustStatus{
    GOOD("종음"),
    Common("보통"),
    Worse("나쁨");

    private final String status;

    DustStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
