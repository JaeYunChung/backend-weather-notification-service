package com.example.publicdatanotification.time;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeSettingService {

    private final TimeSettingEntityRepository timeSettingEntityRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void resetTimeSettingCompletion(){
        List<TimeSettingEntity> times = timeSettingEntityRepository.findAll();
        times.forEach(t -> t.setComplete(false));
        timeSettingEntityRepository.saveAll(times);
    }

    public boolean isAfter(TimeSettingEntity entity, LocalTime now){
        if (entity.isComplete()){
            log.info("엔티티의 complete가 바뀌었습니다");
        }
        return ((entity.getHour() < now.getHour()) || (entity.getHour() == now.getHour() && entity.getMinute() < now.getMinute()))
                &&(!entity.isComplete());
    }
}
