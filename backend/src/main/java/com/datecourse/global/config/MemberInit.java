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
        Member memberTest = Member.createMember("테스터", "test", "test!", "test123@gmail.com", "M", "010-1234-5678");
        repository.save(memberTest);
    }
}
