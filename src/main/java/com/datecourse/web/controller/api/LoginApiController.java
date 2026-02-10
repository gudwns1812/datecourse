package com.datecourse.web.controller.api;

import com.datecourse.domain.member.Member;
import com.datecourse.service.LoginService;
import com.datecourse.service.dto.LoginForm;
import com.datecourse.web.support.response.ApiResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/datecourse/api")
public class LoginApiController {

    private final LoginService service;

    @PostMapping("/login")
    public ApiResponse<Object> login(@RequestBody LoginForm form, HttpSession session) {
        Member member = service.login(form);

        session.setAttribute("memberId", member.getId());
        return ApiResponse.success();
    }
}
