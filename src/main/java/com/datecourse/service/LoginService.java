package com.datecourse.service;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import com.datecourse.service.dto.LoginForm;
import com.datecourse.web.controller.dto.RegisterForm;
import com.datecourse.web.support.error.CoreException;
import com.datecourse.web.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member saveMember(RegisterForm form) {
        var member = form.toMember();

        if (memberRepository.hasLoginId(member.getLoginId())) {
            return null;
        }

        return memberRepository.save(member);
    }

    public Member login(LoginForm form) {
        var loginId = form.loginId();
        var password = form.password();

        return memberRepository
                .findByLoginId(loginId, password)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND_MEMBER, null));
    }
}
