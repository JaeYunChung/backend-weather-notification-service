package com.example.publicdatanotification.member;

import com.example.publicdatanotification.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Member findMemberById(String memberId){
       return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 아이디가 없습니다."));
    }
}
