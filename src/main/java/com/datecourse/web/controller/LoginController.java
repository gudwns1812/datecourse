package com.datecourse.web.controller;

import com.datecourse.domain.member.Member;
import com.datecourse.service.LoginService;
import com.datecourse.web.controller.dto.LoginForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/datecourse")
public class LoginController {

    private static final String LOGIN_ERROR_CODE = "Login";
    private final LoginService service;

    @GetMapping("/login")
    public String loginForm(Model model) {
        LoginForm form = new LoginForm(null, null);
        model.addAttribute(form);

        return "login/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm dto, BindingResult result,
                        HttpSession session, @RequestParam(defaultValue = "/") String redirectURL) {

        log.info("redirectURL={}", redirectURL);
        Member loginMember = service.login(dto);

        if (loginMember == null) {
            log.info("로그인에 실패했습니다. id={}", dto.getLoginId());
            result.reject(LOGIN_ERROR_CODE, "아이디 혹은 비밀번호가 일치하지 않습니다.");
            return "login/login";
        }

        session.setAttribute("memberId", loginMember.getId());
        return "redirect:" + redirectURL;
    }
}
