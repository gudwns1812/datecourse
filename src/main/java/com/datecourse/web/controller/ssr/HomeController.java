package com.datecourse.web.controller.ssr;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import com.datecourse.web.annotation.Login;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping
    public String home(@Login Long memberId, Model model) {
        if (memberId == null) {
            return "index";
        }

        Optional<Member> findMember = memberRepository.findById(memberId);

        if (findMember.isEmpty()) {
            return "index";
        }

        model.addAttribute("member", findMember.get());

        return "loginHome";
    }
}
