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
import com.example.publicdatanotification.open_api.domain.custom.CustomSettingRepository;
import com.example.publicdatanotification.open_api.domain.dust.DustSettingRepository;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingEntity;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSizeCode;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustStatus;
import com.example.publicdatanotification.time.TimeSettingEntity;
import com.example.publicdatanotification.time.TimeSettingEntityRepository;
import com.example.publicdatanotification.time.TimeSettingService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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
    private final TimeSettingEntityRepository timeSettingEntityRepository;
    private final TimeSettingService timeSettingService;
    private final CustomSettingRepository customSettingRepository;
    private final DustSettingRepository dustSettingRepository;
    private final OpenApiConnection connection;


    @Scheduled(fixedRate = 5000)
    public void sendNotification() throws FirebaseMessagingException {
        List<Member> Members = memberRepository.findAll();
        LocalTime now = LocalTime.now();
        for (Member member : Members) {
            List<TimeSettingEntity> timeSettings = timeSettingEntityRepository.findAllByMember(member);
            log.info("계속 실행중:");
            TimeSettingEntity temp = null;
            boolean flag = false;
            for (TimeSettingEntity entity : timeSettings){
                boolean repeat = timeSettingService.isAfter(entity, now);
                log.info("계속 실행여부 : {}",repeat);
                if (repeat){
                    flag = true;
                    temp = entity;
                    break;
                }
            }
            if(flag) {
                log.info("실행중 시간 설정 디버깅");
                List<WeatherDataDto> responses = openApiConnection.getWeatherDataByTransLoc(member.getLongitude(), member.getLatitude());
                if(customSettingRepository.existsByMemberAndWeather(member, Weather.TEMP)) {
                    sendTempNotification(getTempInfo(responses), member);
                }
                if (customSettingRepository.existsByMemberAndWeather(member, Weather.RAIN)){
                    if (!getRainInfo(responses).isEmpty()) sendRainNotification(member);
                }
                if (customSettingRepository.existsByMemberAndWeather(member, Weather.DUST)){
                    String message1 = isOverSettingAndReturnMessage(member, DustSizeCode.PM10);
                    String message2 = isOverSettingAndReturnMessage(member, DustSizeCode.PM25);
                    sendDustNotification(member, message1, message2);
                }
                if (!member.isRepetition()) temp.setComplete(true);
                else{
                    if (temp.getMinute() + 20 < 60){
                        temp.setMinute(temp.getMinute() + 20);
                    }
                    else{
                        temp.setHour(temp.getHour() + 1);
                        temp.setMinute(temp.getMinute() - 40);
                    }
                };
                timeSettingEntityRepository.save(temp);
            }
        }
    }
    public void sendTempNotification(List<WeatherDataDto> temps, Member member)throws FirebaseMessagingException {
        Optional<TempSettingEntity> setting = tempSettingRepository.findByMember(member);
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
    public void sendDustNotification(Member member, String message1, String message2) throws FirebaseMessagingException {
        NotificationToken token = notificationTokenRepository.findByMember(member).orElseThrow(() -> new IllegalArgumentException("해당 토큰이 없습니다."));
        if (message1 != null || message2 != null) notificationService.send(token.getToken(), "마스크 착용!", message1 + "/n" + message2);
        log.info("마스크 착용 메시지를 보냈습니다.");
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

    public String isOverSettingAndReturnMessage(Member member, DustSizeCode size){
        Optional<DustSettingEntity> dustSettingEntity = dustSettingRepository.findByMember(member);
        if (dustSettingEntity.isEmpty()) {
            return null;
        }
        String measurement = connection.getDustStatusForZone(size, member.getZone());
        if (size == DustSizeCode.PM10 && compareStatus(DustStatus.getLabel(measurement), dustSettingEntity.get().getPm10DustStatus()))
            return connection.getDustInformOverall(DustSizeCode.PM10);
        else if (size == DustSizeCode.PM25 && compareStatus(DustStatus.getLabel(measurement), dustSettingEntity.get().getPm10DustStatus()))
            return connection.getDustInformOverall(DustSizeCode.PM25);
        else return null;
    }

    public boolean compareStatus(DustStatus measurement, DustStatus settingValue){
        if (settingValue == DustStatus.GOOD) return true;
        else if (settingValue == DustStatus.Common) return measurement == DustStatus.Common || measurement == DustStatus.Worse;
        else return measurement == DustStatus.Worse;
    }
}
