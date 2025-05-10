package com.example.publicdatanotification.open_api.domain.dust;

import com.example.publicdatanotification.open_api.domain.dust.domain.DustDataDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter @Setter
public class DustDataResponse {
    Response response;
    @Getter @Setter
    public static class Response{
        String totalCount;
        Body body;
        @Getter @Setter
       public static class Body{
           List<DustDataDto> items;
       }
    }
}
