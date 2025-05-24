package com.example.publicdatanotification.open_api.domain.temp;

import com.example.publicdatanotification.firebase.NotificationService;
import com.example.publicdatanotification.firebase.NotificationToken;
import com.example.publicdatanotification.firebase.NotificationTokenRepository;
import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.member.repository.MemberRepository;
import com.example.publicdatanotification.open_api.OpenApiConnection;
import com.example.publicdatanotification.open_api.WeatherCategory;
import com.example.publicdatanotification.open_api.WeatherDataDto;
import com.example.publicdatanotification.open_api.domain.Weather;
import com.example.publicdatanotification.translate.MapInfo;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TempAndRainService {

    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final TempSettingRepository tempSettingRepository;
    private final NotificationTokenRepository notificationTokenRepository;
    private final OpenApiConnection openApiConnection;
    private final MapInfo mapInfo;


    @Scheduled(fixedRate = 5000)
    public void sendNotification() throws FirebaseMessagingException {
        List<Member> tempMembers = memberRepository.findAllSettingWeatherNotification(Weather.TEMP);
        for (Member member : tempMembers) {
            List<WeatherDataDto> responses = openApiConnection.getWeatherDataByTransLoc(member.getLongitude(), member.getLatitude());
            sendTempNotification(getTempInfo(responses), member);
        }
        List<Member> rainMembers = memberRepository.findAllSettingWeatherNotification(Weather.RAIN);
        for (Member member : rainMembers){
            List<WeatherDataDto> responses = openApiConnection.getWeatherDataByTransLoc(member.getLongitude(), member.getLatitude());
            if(getRainInfo(responses).isEmpty()) sendRainNotification(member);
        }
    }
    public void sendTempNotification(List<WeatherDataDto> temps, Member member)throws FirebaseMessagingException {
        Optional<TempSettingEntity> setting = tempSettingRepository.findAllByMember(member);
        Optional<NotificationToken> token = notificationTokenRepository.findByMember(member);
        if (setting.isEmpty()) return;
        if (token.isEmpty()) return;
        for (WeatherDataDto dto : temps) {
            int temp = Integer.parseInt(dto.getFcstValue());
            if (temp >= setting.get().getMaxTemp()) {
                notificationService.send(token.get().getToken(), "날씨가 덥습니다.", "옷을 가볍게 입고 나가세요");
                log.info("온도 메시지를 보냈습니다.");
                break;
            } else if (temp <= setting.get().getMinTemp()) {
                notificationService.send(token.get().getToken(), "날씨가 춥습니다.", "옷을 두껍게 입고 나가세요");
                log.info("온도 메시지를 보냈습니다.");
                break;
            }
        }
    }
    public void sendRainNotification(Member member) throws FirebaseMessagingException {
        Optional<NotificationToken> token = notificationTokenRepository.findByMember(member);
        if (token.isEmpty()) throw new IllegalArgumentException("토큰이 없어 값을 보낼 수 없습니다.");
        notificationService.send(token.get().getToken(), "비가 옵니다.", "우산을 챙겨주세요");
        log.info("우산 메시지를 보냈습니다.");
    }

    public List<WeatherDataDto> getTempInfo(List<WeatherDataDto> data){
        return data.stream()
                .filter(it -> WeatherCategory.T1H.name().equals(it.getCategory()))
                .toList();
    }

    public List<WeatherDataDto> getRainInfo(List<WeatherDataDto> data){
        return data.stream()
                .filter(it -> WeatherCategory.PTY.name().equals(it.getCategory()))
                .filter(it -> !it.getFcstValue().equals("0"))
                .toList();
    }

}
