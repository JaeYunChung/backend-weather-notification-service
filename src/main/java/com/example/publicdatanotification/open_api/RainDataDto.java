package com.example.publicdatanotification.open_api;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class RainDataDto {
    private String category;
    private String fcstTime;
    private String fcstValue;
    @Override
    public String toString(){
        return category + " : " + fcstTime + " : " + fcstValue;
    }
}
