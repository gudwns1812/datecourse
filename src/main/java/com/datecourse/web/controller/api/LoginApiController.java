package com.datecourse.web.controller.api;

import static com.datecourse.error.ErrorCode.NOT_FOUND_MEMBER;

import com.datecourse.domain.member.Member;
import com.datecourse.service.LoginService;
import com.datecourse.service.dto.LoginForm;
import com.datecourse.web.controller.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> login(@RequestBody LoginForm form, HttpSession session) {
        Member member = service.login(form);
        if (member == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(NOT_FOUND_MEMBER));
        }

        session.setAttribute("memberId", member.getId());
        return ResponseEntity.ok().build();
    }
}
