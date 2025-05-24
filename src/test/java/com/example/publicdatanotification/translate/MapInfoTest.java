package com.example.publicdatanotification.translate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapInfoTest {

    @Autowired
    MapInfo mapInfo;

    @Test
    void testTransSlatToGrid(){
        float lon = 126.929810f, lat = 37.488201f;

        int[] result = mapInfo.transSlatToGrid(lon, lat);

        assertEquals(59, result[0]);
        assertEquals(125, result[1]);

    }

}