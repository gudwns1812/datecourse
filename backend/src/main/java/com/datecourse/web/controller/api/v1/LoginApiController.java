package com.datecourse.web.controller.api.v1;

import com.datecourse.domain.member.Member;
import com.datecourse.service.LoginService;
import com.datecourse.service.dto.LoginForm;
import com.datecourse.support.auth.AuthService;
import com.datecourse.support.response.ApiResponse;
import com.datecourse.web.controller.dto.RegisterForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final LoginService loginService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<Long> signup(@RequestBody RegisterForm form) {
        Member member = loginService.saveMember(form);
        return ApiResponse.success(member.getId());
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginForm form) {
        String username = authService.login(form.loginId(), form.password());
        return ApiResponse.success(username);
    }

    @GetMapping("/check-id")
    public ApiResponse<Boolean> check(@RequestParam String loginId) {
        boolean isExist = loginService.checkLoginId(loginId);
        return ApiResponse.success(isExist);
    }
}
