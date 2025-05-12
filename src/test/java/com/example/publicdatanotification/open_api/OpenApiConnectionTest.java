package com.example.publicdatanotification.open_api;

import com.example.publicdatanotification.open_api.domain.dust.domain.DustDataDto;
import com.example.publicdatanotification.open_api.domain.Zone;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSizeCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OpenApiConnectionTest {
    @Autowired
    OpenApiConnection connection;

    @Test
    void testOpenApiConnection(){
        List<DustDataDto> result = connection.getDustData(DustSizeCode.PM10);
        System.out.println("testOpenApiConnection 결과: " + result);
    }

    @Test
    void testDustStatusForZone(){
        String result = connection.getDustStatusForZone(DustSizeCode.PM10, Zone.BUSAN);
        System.out.println("testDustStatusForZone 결과: " + result);
    }
}