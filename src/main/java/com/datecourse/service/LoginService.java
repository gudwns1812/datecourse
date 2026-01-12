package com.datecourse.service;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import com.datecourse.web.controller.dto.LoginForm;
import com.datecourse.web.controller.dto.RegisterForm;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public void saveMember(RegisterForm form) {
        Member member = form.toMember();

        Member savedMember = memberRepository.save(member);
    }

    public Member login(LoginForm form) {
        String loginId = form.getLoginId();
        String password = form.getPassword();

        Optional<Member> loginMember = memberRepository.findByLoginId(loginId, password);

        return loginMember.orElse(null);
    }
}
