package com.datecourse.global.config;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberInit {

    private final MemberRepository repository;

    @PostConstruct
    public void init() {
        Member memberTest = Member.builder()
                .loginId("test")
                .password("test!")
                .email("test123@gmail.com")
                .gender("M")
                .username("테스터")
                .phoneNumber("010-1234-5678")
                .build();

        repository.save(memberTest);
    }
}
