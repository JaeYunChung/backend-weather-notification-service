package com.example.publicdatanotification.open_api;

import com.example.publicdatanotification.open_api.domain.dust.domain.DustDataDto;
import com.example.publicdatanotification.open_api.domain.dust.DustDataResponse;
import com.example.publicdatanotification.open_api.domain.Zone;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSizeCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenApiConnection {
    private final RestTemplate restTemplate;
    private final String returnType = "json";
    private final String numOfRows = "1";
    private final String pageOfNo = "1";
    public List<WeatherDataDto> getWeatherDataByTransLoc(int longitude, int latitude){
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Void> entity = new HttpEntity(headers);
        String authkey = "GzBluLLKynops/NAPvytFXuX4vLGZwjFjZYpeUXECy0aDkLFt9ijeMlrT8v27OegZykWl8itqixMRFwxikOCMw==";
        String url = "https://apis.data.go.kr";
        String path = "/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String nowDate = formatter.format(new Date());
        String nowTime = getBaseTime();
        log.info("위도 : {}, 경도: {}", String.valueOf(longitude), String.valueOf(latitude));
        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .path(path)
                .queryParam("serviceKey", authkey)
                .queryParam("pageNo", pageOfNo)
                .queryParam("numOfRows", Collections.singleton(60))
                .queryParam("dataType", "JSON")
                .queryParam("base_date", nowDate)
                .queryParam("base_time", nowTime)
                .queryParam("nx", Collections.singleton(55))
                .queryParam("ny", Collections.singleton(127))
                .build().toUri();
        log.info(nowTime);
        log.info(restTemplate.getForEntity(uri, String.class).getBody());
        ResponseEntity<WeatherDataResponse> response = restTemplate.exchange(uri, HttpMethod.GET, entity, WeatherDataResponse.class);
        List<WeatherDataDto> data = response.getBody().getResponse().getBody().getItems().getItem();
//        for (WeatherDataDto e : data) {
//            log.info("출력값 : {}", e.toString());
//        }
        return data;
    }


    public List<DustDataDto> getDustData(DustSizeCode code){
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Void> entity = new HttpEntity(headers);
        String authKey = "GzBluLLKynops/NAPvytFXuX4vLGZwjFjZYpeUXECy0aDkLFt9ijeMlrT8v27OegZykWl8itqixMRFwxikOCMw==";
        String url = "http://apis.data.go.kr";
        String path = "/B552584/ArpltnInforInqireSvc/getMinuDustFrcstDspth";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = formatter.format(new Date());
        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .path(path)
                .queryParam("serviceKey", authKey)
                .queryParam("returnType", returnType)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageOfNo", pageOfNo)
                .queryParam("searchDate", nowDate)
                .queryParam("InformCode", code.toString())
                .build().toUri();
        ResponseEntity<DustDataResponse> response = restTemplate.exchange(uri, HttpMethod.GET, entity, DustDataResponse.class);
        List<DustDataDto> result = response.getBody().getResponse().getBody().getItems();
        return result;
    }

//    public List<WeatherDataDto> getWeatherDataByLocation(LocationInfoDto dto){
//        String url = "http://localhost:8000";
//        String path = "/location";
//        URI uri = UriComponentsBuilder
//                .fromUriString(url)
//                .path(path)
//                .queryParam("nx", dto.longitude())
//                .queryParam("ny", dto.latitude())
//                .build().toUri();
//        ResponseEntity<LocationDataResponse> response = restTemplate.getForEntity(uri, LocationDataResponse.class);
//        if (response.getBody() == null){
//            return null;
//        }
//        //log.info("Received location data: " + response.getBody());
//        return  getWeatherDataByTransLoc(response.getBody());
//    }

    public String getDustStatusForZone(DustSizeCode sizeCode, Zone zone){
        String dustInfo = getDustData(sizeCode).get(0).getInformGrade();
        String infoForZone = Arrays.stream(dustInfo.split(","))
                .filter(value ->value.contains(zone.getZone()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 지역의 정보가 없습니다."));
        String[] status = infoForZone.split(" : ");
        return status[1];
    }

    public String getDustInformOverall(DustSizeCode sizeCode){
        String overall = getDustData(sizeCode).get(0).getInformOverall();
        return overall;
    }
    public String getBaseTime(){
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        if (minute < 30) calendar.add(Calendar.HOUR_OF_DAY, -1);
        if(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).length() == 1)
            return "0" + calendar.get(Calendar.HOUR_OF_DAY) + "30";
        return calendar.get(Calendar.HOUR_OF_DAY) + "30";
    }
}
