package com.datecourse.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import com.datecourse.support.auth.CustomAuthenticationEntryPoint;
import com.datecourse.support.auth.CustomUserDetails;
import com.datecourse.support.auth.SecurityConfig;
import com.datecourse.web.config.WebConfig;
import com.datecourse.web.controller.ssr.HomeController;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(HomeController.class)
@Import({WebConfig.class, SecurityConfig.class, CustomAuthenticationEntryPoint.class})
class HomeControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    MemberRepository memberRepository;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void argumentResolverTest() throws Exception {
        //given
        Member memberTest = Member.createMember("테스터", "test", "test!", "test123@gmail.com", "M", "010-1234-5678");
        ReflectionTestUtils.setField(memberTest, "id", 1L); // ID 강제 주입

        CustomUserDetails userDetails = new CustomUserDetails(memberTest);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        given(memberRepository.findById(any())).willReturn(Optional.of(memberTest));

        //when & then
        mockMvc.perform(get("/")
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("loginHome"))
                .andDo(print());
    }
}
