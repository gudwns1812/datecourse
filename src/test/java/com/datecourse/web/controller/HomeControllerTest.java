package com.datecourse.web.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import com.datecourse.web.constrant.SessionConst;
import com.datecourse.web.controller.ssr.HomeController;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean // 가짜 빈으로 등록하여 의존성 해결!
    MemberRepository memberRepository;

    @Test
    void argumentResolverTest() throws Exception {
        //given
        Member memberTest = Member.builder()
                .loginId("test")
                .password("test!")
                .email("test123@gmail.com")
                .gender("M")
                .username("테스터")
                .phoneNumber("010-1234-5678")
                .build();

        given(memberRepository.findById(1L)).willReturn(Optional.of(memberTest));
        //when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.MEMBER_ID, 1L);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);
        //then
        mockMvc.perform(get("/")
                        .session(session)) // 생성한 가짜 세션을 요청에 포함
                .andExpect(status().isOk())
                .andExpect(view().name("loginHome"))
                .andDo(print());
    }
}
