package com.example.publicdatanotification.firebase;

import com.example.publicdatanotification.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationToken {
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String token;
}