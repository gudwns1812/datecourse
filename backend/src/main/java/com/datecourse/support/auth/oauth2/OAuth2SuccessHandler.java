package com.datecourse.support.auth.oauth2;

import static com.datecourse.support.auth.USER_ROLE.ROLE_GUEST;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.frontend.base-url:http://localhost:3000}")
    private String frontendBaseUrl;

    @Value("${app.frontend.signup:/signup}")
    private String signupUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2User = (OAuth2AuthenticationToken) authentication;

        boolean isGuest = oAuth2User.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ROLE_GUEST.name()));

        if (isGuest) {
            String providerName = oAuth2User.getAuthorizedClientRegistrationId();
            String url = UriComponentsBuilder.fromUriString(frontendBaseUrl + signupUrl)
                    .build().toString();

            Cookie loginTypeCookie = new Cookie("loginType", providerName);
            loginTypeCookie.setPath("/signup");
            loginTypeCookie.setHttpOnly(false);
            loginTypeCookie.setMaxAge(60);
            response.addCookie(loginTypeCookie);

            getRedirectStrategy().sendRedirect(request, response, url);
        } else {
            getRedirectStrategy().sendRedirect(request, response, frontendBaseUrl);
        }
    }
}
