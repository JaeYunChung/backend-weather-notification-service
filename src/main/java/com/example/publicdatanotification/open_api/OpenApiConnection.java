package com.example.publicdatanotification.open_api;

import com.example.publicdatanotification.open_api.domain.dust.domain.DustDataDto;
import com.example.publicdatanotification.open_api.domain.dust.DustDataResponse;
import com.example.publicdatanotification.open_api.domain.Zone;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSizeCode;
import com.example.publicdatanotification.websocket.LocationDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenApiConnection {
    private final RestTemplate restTemplate;
    private final String returnType = "json";
    private final String numOfRows = "1";
    private final String pageOfNo = "1";
    public List<RainDataDto> getRainData(LocationDataResponse locationData){
        String authkey = "GzBluLLKynops/NAPvytFXuX4vLGZwjFjZYpeUXECy0aDkLFt9ijeMlrT8v27OegZykWl8itqixMRFwxikOCMw==";
        String url = "https://apis.data.go.kr";
        String path = "/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String nowDate = formatter.format(new Date());
        String nowTime = getBaseTime();
        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .path(path)
                .queryParam("serviceKey", authkey)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 6)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", nowDate)
                .queryParam("base_time", nowTime)
                .queryParam("nx", "60")
                .queryParam("ny", "127")
                .build().toUri();
        log.info(nowTime);
        log.info(restTemplate.getForEntity(uri, String.class).getBody());
        ResponseEntity<RainDataResponse> response = restTemplate.getForEntity(uri, RainDataResponse.class);
        List<RainDataDto> data = response.getBody().getResponse().getBody().getItems().getItem();
        for (RainDataDto e : data) {
            log.info("출력값 : {}", e.toString());
        }
        return data;
    }

    public List<DustDataDto> getDustData(DustSizeCode code){
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
        ResponseEntity<DustDataResponse> response = restTemplate.getForEntity(uri, DustDataResponse.class);
        List<DustDataDto> result = response.getBody().getResponse().getBody().getItems();
        return result;
    }

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
