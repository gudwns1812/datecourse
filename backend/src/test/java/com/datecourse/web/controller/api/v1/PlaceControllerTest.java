package com.datecourse.web.controller.api.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.datecourse.domain.place.PlaceSearchCommand;
import com.datecourse.domain.place.PlaceSearchCommandFactory;
import com.datecourse.domain.place.PlaceSearchResult;
import com.datecourse.domain.place.PlaceService;
import com.datecourse.storage.constant.MemberGender;
import com.datecourse.storage.entity.Member;
import com.datecourse.support.auth.CustomAuthenticationEntryPoint;
import com.datecourse.support.auth.MemberDetails;
import com.datecourse.support.auth.oauth2.KakaoOAuth2UserService;
import com.datecourse.support.auth.oauth2.OAuth2SuccessHandler;
import java.util.List;
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

@WebMvcTest(PlaceController.class)
@ExtendWith(RestDocumentationExtension.class)
class PlaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlaceService placeService;

    @MockitoBean
    private PlaceSearchCommandFactory placeSearchCommandFactory;

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

        Member member = Member.createMember("tester", "test", "password", "test@test.com", MemberGender.MALE,
                "010-1234-5678");
        ReflectionTestUtils.setField(member, "id", 1L);

        MemberDetails userDetails = new MemberDetails(member);
        auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    void getPlaces() throws Exception {
        // given
        given(placeSearchCommandFactory.create(anyDouble(), anyDouble(), any(), any(), anyDouble(), anyInt(), any()))
                .willReturn(new PlaceSearchCommand(37.5665, 126.9780, null, null, 2000d, 20));
        given(placeService.getPlaces(any())).willReturn(List.of(
                new PlaceSearchResult(
                        1L,
                        "City Cafe",
                        "Cafe",
                        "Cozy coffee spot",
                        37.5665,
                        126.9780,
                        12.3,
                        178.9,
                        List.of("coffee", "dessert")
                )
        ));

        // when & then
        mockMvc.perform(get("/api/v1/places")
                        .with(authentication(auth))
                        .param("latitude", "37.5665")
                        .param("longitude", "126.9780"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data[0].name").value("City Cafe"))
                .andExpect(jsonPath("$.data[0].recommendationScore").value(178.9))
                .andDo(document("place-search",
                        responseFields(
                                fieldWithPath("result").description("결과 상태"),
                                fieldWithPath("data[].id").description("장소 ID"),
                                fieldWithPath("data[].name").description("장소명"),
                                fieldWithPath("data[].category").description("카테고리"),
                                fieldWithPath("data[].description").description("장소 설명"),
                                fieldWithPath("data[].latitude").description("위도"),
                                fieldWithPath("data[].longitude").description("경도"),
                                fieldWithPath("data[].distanceMeters").description("기준 위치로부터의 거리(m)"),
                                fieldWithPath("data[].recommendationScore").description("추천 점수"),
                                fieldWithPath("data[].tags").description("연결된 태그 목록"),
                                fieldWithPath("error").description("에러 정보")
                        )
                ));
    }

    @Test
    void getPlaces_withBounds() throws Exception {
        given(placeSearchCommandFactory.create(anyDouble(), anyDouble(), any(), any(), anyDouble(), anyInt(), any()))
                .willReturn(new PlaceSearchCommand(37.5665, 126.9780, null, null, 200d, 200));
        given(placeService.getPlaces(any())).willReturn(List.of());

        mockMvc.perform(get("/api/v1/places")
                        .with(authentication(auth))
                        .param("latitude", "37.5665")
                        .param("longitude", "126.9780")
                        .param("southWestLat", "37.5660")
                        .param("southWestLng", "126.9775")
                        .param("northEastLat", "37.5670")
                        .param("northEastLng", "126.9785")
                        .param("size", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));
    }
}
