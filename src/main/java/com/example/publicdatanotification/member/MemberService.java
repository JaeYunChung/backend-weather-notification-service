package com.example.publicdatanotification.member;

import com.example.publicdatanotification.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    @Transactional
    public Member findMemberById(String memberId){
       return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 아이디가 없습니다."));
    }
    @Transactional
    public void saveChangedRepetitionStatus(Member member){
        memberRepository.save(member);
    }
}
