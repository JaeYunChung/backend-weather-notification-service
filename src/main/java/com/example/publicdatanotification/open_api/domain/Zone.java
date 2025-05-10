package com.example.publicdatanotification.open_api.domain;

public enum Zone {

    SEOUL("서울"),
    JEJU("제주"),
    JEONNAM("전남"),
    JEONBUK("전북"),
    GWANGJU("광주"),
    GYEONGNAM("경남"),
    GYEONGBUK("경북"),
    ULSAN("울산"),
    DAEGU("대구"),
    BUSAN("부산"),
    CHUNGNAM("충남"),
    CHUNGBUK("충북"),
    SEJONG("세종"),
    DAEJEON("대전"),
    YEONGDONG("영동"),
    YEONGSEO("영서"),
    INCHEON("인천");

    private final String zone;

    Zone(String zone){
        this.zone = zone;
    }

    public String getZone(){
        return zone;
    }
}
