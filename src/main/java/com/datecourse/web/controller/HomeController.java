package com.datecourse.web.controller;

import static com.datecourse.web.constrant.SessionConst.MEMBER_ID;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping
    public String home(@SessionAttribute(value = MEMBER_ID, required = false) Long memberId, Model model) {
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
