package com.datecourse.support.init;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberInit {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Member memberTest = Member.createMember("테스터", "test", passwordEncoder.encode("test!"), "test123@gmail.com",
                "M", "010-1234-5678", null);
        repository.save(memberTest);
    }
}
