package com.example.publicdatanotification.open_api.domain.dust;

import com.example.publicdatanotification.firebase.NotificationService;
import com.example.publicdatanotification.firebase.NotificationToken;
import com.example.publicdatanotification.firebase.NotificationTokenRepository;
import com.example.publicdatanotification.open_api.OpenApiConnection;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustSettingEntity;
import com.example.publicdatanotification.open_api.domain.dust.domain.DustStatus;
import com.example.publicdatanotification.open_api.member.Member;
import com.example.publicdatanotification.open_api.member.repository.MemberRepository;
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
        List<Member> members = memberRepository.findAll();
        for (Member member : members){
            String message = isOverSettingAndReturnMessage(member);
            NotificationToken token = tokenRepository.findByMember(member).orElseThrow(() -> new IllegalArgumentException("해당 토큰이 없습니다."));
            if (message != null) notificationService.send(token.getToken(), "마스크 착용!", message);
        }
    }

    public String isOverSettingAndReturnMessage( Member member){
        Optional<DustSettingEntity> dustSettingEntity = dustSettingRepository.findByMember(member);
        if (dustSettingEntity.isEmpty()) {
            return null;
        }
        String measurement = connection.getDustStatusForZone(dustSettingEntity.get().getDustSize(), member.getZone());
        if (compareStatus(measurement, dustSettingEntity.get().getDustStatus().name()))
            return connection.getDustInformOverall(dustSettingEntity.get().getDustSize());
        else return null;
    }

    public boolean compareStatus(String measurement, String settingValue){
        DustStatus measure = DustStatus.valueOf(measurement);
        DustStatus setting = DustStatus.valueOf(settingValue);
        if (setting == DustStatus.GOOD) return true;
        else if (setting == DustStatus.Common) return measure == DustStatus.Common || measure == DustStatus.Worse;
        else return measure == DustStatus.Worse;
    }
}
