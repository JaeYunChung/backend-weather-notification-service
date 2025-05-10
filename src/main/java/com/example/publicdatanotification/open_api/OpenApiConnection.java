package com.example.publicdatanotification.open_api;

import com.example.publicdatanotification.open_api.domain.dust.domain.DustDataDto;
import com.example.publicdatanotification.open_api.domain.dust.DustDataResponse;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSizeCode;
import com.example.publicdatanotification.open_api.domain.Zone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenApiConnection {
    private final RestTemplate restTemplate;
    private final String url = "http://apis.data.go.kr";
    private final String path = "/B552584/ArpltnInforInqireSvc/getMinuDustFrcstDspth";
    private final String authKey = "GzBluLLKynops/NAPvytFXuX4vLGZwjFjZYpeUXECy0aDkLFt9ijeMlrT8v27OegZykWl8itqixMRFwxikOCMw==";
    private final String returnType = "json";
    private final String numOfRows = "1";
    private final String pageOfNo = "1";
    public List<DustDataDto> getDustData(DustSizeCode code){
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
}
