package com.datecourse.web.controller.api.v1;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.datecourse.domain.member.Member;
import com.datecourse.domain.station.Station;
import com.datecourse.service.DateCourseService;
import com.datecourse.support.auth.CustomUserDetails;
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
    private DateCourseService dateCourseService;

    private Authentication auth;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        Member member = Member.createMember("테스터", "test", "password", "test@test.com", "M", "010-1234-5678");
        ReflectionTestUtils.setField(member, "id", 1L); // ID 할당

        CustomUserDetails userDetails = new CustomUserDetails(member);
        auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    void getRandomStation() throws Exception {
        //given
        Station station = Station.of("2호선", "강남역", "서울특별시 강남구 강남대로 396");
        given(dateCourseService.getRandomStation()).willReturn(station);

        //when & then
        mockMvc.perform(get("/v1/stations/random")
                        .with(authentication(auth))) // Security 인증 객체 주입
                .andExpect(status().isOk())
                .andDo(document("station-random",
                        responseFields(
                                fieldWithPath("result").description("결과 상태 (SUCCESS/ERROR)"),
                                fieldWithPath("data").description("역 정보 데이터"),
                                fieldWithPath("data.lineNumbers").description("호선 정보 리스트"),
                                fieldWithPath("data.stationName").description("지하철역 이름"),
                                fieldWithPath("data.stationAddress").description("상세 주소"),
                                fieldWithPath("error").description("에러 발생 시 상세 정보 (또는 null)")
                        )
                ));
    }
}
