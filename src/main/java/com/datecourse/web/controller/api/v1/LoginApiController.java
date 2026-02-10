package com.datecourse.web.controller.api.v1;

import static com.datecourse.web.constrant.SessionConst.MEMBER_ID;

import com.datecourse.domain.member.Member;
import com.datecourse.service.LoginService;
import com.datecourse.service.dto.LoginForm;
import com.datecourse.web.annotation.Login;
import com.datecourse.web.controller.dto.StatusResponseDto;
import com.datecourse.web.support.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/login")
    public ApiResponse<Void> login(@RequestBody LoginForm form, HttpSession session) {
        Member member = service.login(form);

        session.setAttribute(MEMBER_ID, member.getId());
        return ApiResponse.success();
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

    @PostMapping("v1/auth/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ApiResponse.success();
    }
}
