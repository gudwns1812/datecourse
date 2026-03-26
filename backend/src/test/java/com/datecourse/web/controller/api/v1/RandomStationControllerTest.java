package com.datecourse.web.controller.api.v1;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.datecourse.domain.StationService;
import com.datecourse.domain.station.Station;
import com.datecourse.storage.constant.MemberGender;
import com.datecourse.storage.entity.Member;
import com.datecourse.support.auth.CustomAuthenticationEntryPoint;
import com.datecourse.support.auth.MemberDetails;
import com.datecourse.support.auth.oauth2.KakaoOAuth2UserService;
import com.datecourse.support.auth.oauth2.OAuth2SuccessHandler;
import com.datecourse.support.utils.GeometryUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(RandomStationController.class)
@ExtendWith(RestDocumentationExtension.class)
class RandomStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StationService stationService;

    @MockitoBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @MockitoBean
    private KakaoOAuth2UserService kakaoOAuth2UserService;

    @MockitoBean
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    private Authentication auth;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();

        Member member = Member.createMember("테스터", "test", "password", "test@test.com", MemberGender.MALE,
                "010-1234-5678");
        ReflectionTestUtils.setField(member, "id", 1L); // ID 할당

        MemberDetails userDetails = new MemberDetails(member);
        auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    void getRandomStation() throws Exception {
        //given
        Station station = Station.of("2호선", "강남역", GeometryUtils.createPoint(127.001, 127.2123));
        given(stationService.getRandomStation(null, null, null)).willReturn(station);

        //when & then
        mockMvc.perform(get("/api/v1/stations/random")
                        .with(authentication(auth))) // Security 인증 객체 주입
                .andExpect(status().isOk())
                .andDo(document("station-random",
                        responseFields(
                                fieldWithPath("result").description("결과 상태 (SUCCESS/ERROR)"),
                                fieldWithPath("data").description("역 정보 데이터"),
                                fieldWithPath("data.line").description("호선 정보"),
                                fieldWithPath("data.stationName").description("지하철역 이름"),
                                fieldWithPath("data.longitude").description("경도"),
                                fieldWithPath("data.latitude").description("위도"),
                                fieldWithPath("error").description("에러 발생 시 상세 정보 (또는 null)")
                        )
                ));
    }
}
