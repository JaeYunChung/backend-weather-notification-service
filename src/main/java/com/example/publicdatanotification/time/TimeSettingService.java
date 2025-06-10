package com.example.publicdatanotification.time;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSettingService {

    private final TimeSettingEntityRepository timeSettingEntityRepository;

    @Scheduled
    public void resetTimeSettingCompletion(){
        List<TimeSettingEntity> times = timeSettingEntityRepository.findAll();
        times.forEach(t -> t.setComplete(false));
        timeSettingEntityRepository.saveAll(times);
    }

    public boolean isAfter(TimeSettingEntity entity, LocalTime now){
        return ((entity.getHour() < now.getHour()) || (entity.getHour() == now.getHour() && entity.getMinute() < now.getMinute()))
                &&(!entity.isComplete());
    }
}
