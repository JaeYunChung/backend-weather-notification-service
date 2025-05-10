package com.example.publicdatanotification.open_api.member.repository;

import com.example.publicdatanotification.open_api.domain.Zone;
import com.example.publicdatanotification.open_api.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {
}
