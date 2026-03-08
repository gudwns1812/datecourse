package com.datecourse.web.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import com.datecourse.support.auth.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class HomeControllerTestWithSpringBootTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void springBootControllerTest() throws Exception {
        //given
        Member member = Member.createMember("테스터", "test", "password", "test@test.com", "M", "010-1234-5678");
        Member savedMember = memberRepository.save(member);

        CustomUserDetails userDetails = new CustomUserDetails(savedMember);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        //when & then
        mockMvc.perform(get("/")
                        .with(authentication(auth))) // Security 인증 객체 주입
                .andExpect(status().isOk())
                .andExpect(view().name("loginHome")); // 결과 뷰 이름 확인
    }
}
