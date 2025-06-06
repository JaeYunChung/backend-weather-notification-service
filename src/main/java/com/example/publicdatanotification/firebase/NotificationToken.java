package com.example.publicdatanotification.firebase;

import com.example.publicdatanotification.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationToken {
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String token;
}