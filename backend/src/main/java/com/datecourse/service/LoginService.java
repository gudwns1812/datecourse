package com.datecourse.service;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import com.datecourse.service.dto.LoginForm;
import com.datecourse.support.error.CoreException;
import com.datecourse.support.error.ErrorType;
import com.datecourse.web.controller.dto.RegisterForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member saveMember(RegisterForm form) {
        if (memberRepository.existsByLoginId(form.getLoginId())) {
            throw new CoreException(ErrorType.INTERNAL_SERVER_ERROR, "이미 존재하는 아이디입니다."); // or a more specific error type
        }

        String encryptedPassword = passwordEncoder.encode(form.getPassword());
        
        Member member = Member.createMember(
                form.getUsername(),
                form.getLoginId(),
                encryptedPassword,
                form.getEmail(),
                form.getGender(),
                form.getPhoneNumber()
        );

        return memberRepository.save(member);
    }

    public Member login(LoginForm form) {
        var loginId = form.loginId();
        var password = form.password();

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND_MEMBER, List.of(loginId)));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CoreException(ErrorType.NOT_FOUND_MEMBER, List.of(loginId));
        }

        return member;
    }

    public Member findMemberById(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND_MEMBER, memberId));
    }
}
