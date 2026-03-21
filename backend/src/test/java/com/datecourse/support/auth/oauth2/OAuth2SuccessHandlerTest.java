package com.datecourse.support.auth.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.util.ReflectionTestUtils;

class OAuth2SuccessHandlerTest {

    private OAuth2SuccessHandler successHandler;
    private HttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        successHandler = new OAuth2SuccessHandler();
        ReflectionTestUtils.setField(successHandler, "frontendBaseUrl", "http://localhost:3000");
        ReflectionTestUtils.setField(successHandler, "signupUrl", "/signup");
        
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("신규 유저(ROLE_GUEST)인 경우 회원가입 페이지로 리다이렉트되며 providerId를 포함한다.")
    void redirect_to_signup_for_guest() throws Exception {
        // given
        Authentication authentication = mock(Authentication.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_GUEST"));
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAuthorities()).thenAnswer(invocation -> authorities);
        when(oAuth2User.getAttribute("id")).thenReturn(12345L);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        assertThat(response.getRedirectedUrl()).isEqualTo("http://localhost:3000/signup?providerId=12345");
    }

    @Test
    @DisplayName("기존 유저(ROLE_USER)인 경우 프론트엔드 베이스 URL로 리다이렉트된다.")
    void redirect_to_home_for_user() throws Exception {
        // given
        Authentication authentication = mock(Authentication.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAuthorities()).thenAnswer(invocation -> authorities);
        when(oAuth2User.getAttribute("id")).thenReturn(12345L);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        assertThat(response.getRedirectedUrl()).isEqualTo("http://localhost:3000");
    }
}
