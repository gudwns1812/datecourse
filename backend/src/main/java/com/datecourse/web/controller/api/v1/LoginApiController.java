package com.datecourse.web.controller.api.v1;

import com.datecourse.domain.MemberService;
import com.datecourse.domain.dto.LoginForm;
import com.datecourse.storage.entity.Member;
import com.datecourse.support.auth.AuthService;
import com.datecourse.support.response.ApiResponse;
import com.datecourse.web.controller.dto.RegisterForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class LoginApiController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<Long> signup(@Valid @RequestBody RegisterForm form,
                                    @AuthenticationPrincipal OAuth2User oAuth2User) {
        Member member = memberService.saveMember(form);
        if (oAuth2User == null) {
            return ApiResponse.success(member.getId());
        }

        String providerId = oAuth2User.getAttribute("id").toString();
        form.setProviderId(providerId);

        authService.upgradeAuth();
        return ApiResponse.success(member.getId());
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginForm form) {
        String username = authService.login(form.loginId(), form.password());
        return ApiResponse.success(username);
    }

    @GetMapping("/check-id")
    public ApiResponse<Boolean> check(@RequestParam String loginId) {
        boolean isExist = memberService.checkLoginId(loginId);
        return ApiResponse.success(isExist);
    }
}
