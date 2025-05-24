package com.example.publicdatanotification.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class WebSocketControllerTest {

    //37.284700, 127.044098

    @Autowired
    RestTemplate restTemplate;
//    @Test
//    void testHandleText(){
//        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8000/location")
//                .queryParam("nx", 127.044098)
//                .queryParam("ny", 37.284700)
//                .build().toUri();
//        ResponseEntity<LocationDataResponse> response = restTemplate.getForEntity(uri, LocationDataResponse.class);
//        System.out.println(response.getBody());
//    }
}