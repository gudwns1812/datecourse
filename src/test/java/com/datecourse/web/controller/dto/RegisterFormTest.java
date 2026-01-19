package com.datecourse.web.controller.dto;

import com.datecourse.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RegisterFormTest {

    @Test
    void registerToMember() {
        //given
        RegisterForm form = new RegisterForm();
        //when
        Member member = form.toMember();
        //then
        Assertions.assertThat(member.getId())
                .isNull();
    }
}
