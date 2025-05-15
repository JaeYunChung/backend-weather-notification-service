package com.example.publicdatanotification.open_api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class WeatherDataResponse {
    private Response response;
    @Getter@Setter
    public static class Response{
        private Body body;
        @Getter@Setter
        public static class Body{
            private Item items;
            @Getter@Setter
            public static class Item{
                List<WeatherDataDto> item;
            }
        }
    }
}