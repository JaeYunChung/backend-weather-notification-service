package com.example.publicdatanotification.member;

import com.example.publicdatanotification.open_api.domain.Zone;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Member {
    @Id
    private String id;

    private String name;
    private int age;

    private boolean repetition;

    private int longitude;
    private int latitude;

    @Enumerated(EnumType.STRING)
    private Zone zone;
}
