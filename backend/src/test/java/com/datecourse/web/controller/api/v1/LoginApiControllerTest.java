package com.datecourse.web.controller.api.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.datecourse.domain.member.Member;
import com.datecourse.service.LoginService;
import com.datecourse.service.dto.LoginForm;
import com.datecourse.support.auth.AuthService;
import com.datecourse.support.auth.MemberDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(LoginApiController.class)
@ExtendWith(RestDocumentationExtension.class)
class LoginApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoginService loginService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void signup() throws Exception {
        //given
        String json = "{\"username\":\"테스터\",\"loginId\":\"test\",\"password\":\"test!\",\"email\":\"test@test.com\",\"gender\":\"M\",\"phoneNumber\":\"010-1234-5678\"}";

        Member member = Member.createMember("테스터", "test", "test!", "test@test.com", "M", "010-1234-5678");
        ReflectionTestUtils.setField(member, "id", 1L);
        given(loginService.saveMember(any())).willReturn(member);

        //when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("member-signup",
                        requestFields(
                                fieldWithPath("username").description("사용자 이름"),
                                fieldWithPath("loginId").description("로그인 아이디"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("email").description("이메일 주소"),
                                fieldWithPath("gender").description("성별 (M/F)"),
                                fieldWithPath("phoneNumber").description("휴대폰 번호")
                        ),
                        responseFields(
                                fieldWithPath("result").description("결과 상태 (SUCCESS/ERROR)"),
                                fieldWithPath("data").description("생성된 회원의 ID (또는 null)"),
                                fieldWithPath("error").description("에러 발생 시 상세 정보 (또는 null)")
                        )
                ));
    }

    @Test
    void login() throws Exception {
        //given
        LoginForm form = new LoginForm("test", "test!");
        String json = objectMapper.writeValueAsString(form);

        Member member = Member.createMember("테스터", "test", "test!", "test@test.com", "M", "010-1234-5678");
        ReflectionTestUtils.setField(member, "id", 1L);

        MemberDetails userDetails = new MemberDetails(member);
        Authentication authentication = Mockito.mock(Authentication.class);
        given(authentication.getPrincipal()).willReturn(userDetails);
        given(authenticationManager.authenticate(any())).willReturn(authentication);

        //when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("member-login",
                        requestFields(
                                fieldWithPath("loginId").description("로그인 아이디"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("result").description("결과 상태 (SUCCESS/ERROR)"),
                                fieldWithPath("data").description("로그인 성공한 사용자의 이름"),
                                fieldWithPath("error").description("에러 발생 시 상세 정보 (또는 null)")
                        )
                ));
    }
}
