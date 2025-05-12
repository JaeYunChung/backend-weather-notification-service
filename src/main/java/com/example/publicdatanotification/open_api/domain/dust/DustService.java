package com.example.publicdatanotification.open_api.domain.dust;

import com.example.publicdatanotification.firebase.NotificationService;
import com.example.publicdatanotification.firebase.NotificationToken;
import com.example.publicdatanotification.firebase.NotificationTokenRepository;
import com.example.publicdatanotification.open_api.OpenApiConnection;
import com.example.publicdatanotification.open_api.domain.Weather;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingEntity;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSizeCode;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustStatus;
import com.example.publicdatanotification.member.Member;
import com.example.publicdatanotification.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DustService {

    private final MemberRepository memberRepository;
    private final DustSettingRepository dustSettingRepository;
    private final OpenApiConnection connection;
    private final NotificationService notificationService;
    private final NotificationTokenRepository tokenRepository;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendNotification(){
        List<Member> members = memberRepository.findAllSettingWeatherNotification(Weather.DUST);
        for (Member member : members){
            String message1 = isOverSettingAndReturnMessage(member, DustSizeCode.PM10);
            String message2 = isOverSettingAndReturnMessage(member, DustSizeCode.PM25);
            NotificationToken token = tokenRepository.findByMember(member).orElseThrow(() -> new IllegalArgumentException("해당 토큰이 없습니다."));
            if (message1 != null || message2 != null) notificationService.send(token.getToken(), "마스크 착용!", message1 + "/n" + message2);
        }
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
