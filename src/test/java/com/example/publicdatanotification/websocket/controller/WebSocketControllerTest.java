package com.example.publicdatanotification.websocket.controller;

import com.example.publicdatanotification.websocket.LocationDataResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebSocketControllerTest {

    //37.284700, 127.044098

    @Autowired
    RestTemplate restTemplate;
    @Test
    void testHandleText(){
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8000/location")
                .queryParam("nx", 127.044098)
                .queryParam("ny", 37.284700)
                .build().toUri();
        ResponseEntity<LocationDataResponse> response = restTemplate.getForEntity(uri, LocationDataResponse.class);
        System.out.println(response.getBody());
    }
}