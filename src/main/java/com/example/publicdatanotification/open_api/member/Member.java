package com.example.publicdatanotification.open_api.member;

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

    @Enumerated(EnumType.STRING)
    private Zone zone;
}
