package com.datecourse.service;

import com.datecourse.domain.member.Member;
import com.datecourse.web.controller.dto.RegisterForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    LoginService service;

    @Test
    void serviceDuplicateTest() {
        //given
        RegisterForm form = new RegisterForm();
        form.setLoginId("test");
        //when
        Member member = service.saveMember(form);
        //then
        Assertions.assertThat(member)
                .isNull();
    }
}
