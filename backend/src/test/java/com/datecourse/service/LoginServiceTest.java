package com.datecourse.service;

import com.datecourse.domain.member.Member;
import com.datecourse.support.error.CoreException;
import com.datecourse.web.controller.dto.RegisterForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LoginServiceTest {

    @Autowired
    LoginService service;

    @Test
    void serviceDuplicateTest() {
        //given
        RegisterForm form = new RegisterForm();
        form.setUsername("테스터");
        form.setLoginId("unique_test_id");
        form.setPassword("password");
        form.setEmail("test@test.com");
        form.setGender("M");
        form.setPhoneNumber("010-1234-5678");

        service.saveMember(form);

        //when & then
        assertThatThrownBy(() -> service.saveMember(form))
                .isInstanceOf(CoreException.class);
    }
}
