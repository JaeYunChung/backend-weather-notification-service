package com.example.publicdatanotification.time;

import com.example.publicdatanotification.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(name = "hour_value")
    private int hour;
    @Column(name = "minute_value")
    private int minute;

    private boolean complete;
}
