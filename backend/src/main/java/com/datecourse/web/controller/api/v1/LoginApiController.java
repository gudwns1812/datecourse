package com.datecourse.web.controller.api.v1;

import com.datecourse.domain.member.Member;
import com.datecourse.service.LoginService;
import com.datecourse.service.dto.LoginForm;
import com.datecourse.support.auth.CustomUserDetails;
import com.datecourse.support.response.ApiResponse;
import com.datecourse.web.annotation.Login;
import com.datecourse.web.controller.dto.RegisterForm;
import com.datecourse.web.controller.dto.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class LoginApiController {

    private final LoginService service;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ApiResponse<Long> signup(@RequestBody RegisterForm form) {
        Member member = service.saveMember(form);
        return ApiResponse.success(member.getId());
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginForm form) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(form.loginId(), form.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ApiResponse.success(userDetails.getMember().getUsername());
    }

    @GetMapping("/status")
    public ApiResponse<?> checkLogin(@Login Long memberId) {
        boolean isLogin = memberId != null;

        log.info("isLogin = {}", isLogin);
        if (!isLogin) {
            return ApiResponse.success(new StatusResponseDto(false, null));
        }

        Member member = service.findMemberById(memberId);
        return ApiResponse.success(new StatusResponseDto(true, member.getUsername()));
    }

    // logout은 SecurityConfig에서 처리하므로 여기서 제거하거나 필요 시 남겨둠
}
