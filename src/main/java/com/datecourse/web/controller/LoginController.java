package com.datecourse.web.controller;

import static com.datecourse.web.constrant.SessionConst.MEMBER_ID;

import com.datecourse.domain.member.Member;
import com.datecourse.service.LoginService;
import com.datecourse.web.controller.dto.LoginForm;
import com.datecourse.web.controller.dto.RegisterForm;
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
    private static final String DUPLICATE_ERROR_CODE = "duplicate";

    private final LoginService service;

    @GetMapping("/login")
    public String loginForm(Model model) {
        LoginForm form = new LoginForm(null, null);
        model.addAttribute(form);

        return "loginpage/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm dto, BindingResult result,
                        HttpSession session, @RequestParam(defaultValue = "/") String redirectURL) {

        if (result.hasErrors()) {
            return "loginpage/login";
        }

        Member loginMember = service.login(dto);

        if (loginMember == null) {
            log.info("로그인에 실패했습니다. id={}", dto.getLoginId());
            result.reject(LOGIN_ERROR_CODE, "아이디 혹은 비밀번호가 일치하지 않습니다.");
            return "loginpage/login";
        }

        session.setAttribute(MEMBER_ID, loginMember.getId());
        return "redirect:" + redirectURL;
    }

    @GetMapping("/signup")
    public String registerForm(Model model) {
        RegisterForm form = new RegisterForm();
        model.addAttribute("registerForm", form);

        return "loginpage/register";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute RegisterForm registerForm, BindingResult result,
                           HttpSession session) {

        if (result.hasErrors()) {
            return "loginpage/register";
        }

        Member member = service.saveMember(registerForm);

        if (member == null) {
            result.reject(DUPLICATE_ERROR_CODE);
            return "loginpage/register";
        }

        session.setAttribute(MEMBER_ID, member.getId());

        return "redirect:/";
    }
}
