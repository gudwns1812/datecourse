package com.datecourse.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.datecourse.domain.MemberService;
import com.datecourse.storage.constant.MemberGender;
import com.datecourse.support.error.CoreException;
import com.datecourse.web.controller.dto.RegisterForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    MemberService service;

    @Test
    void serviceDuplicateTest() {
        //given
        RegisterForm form = new RegisterForm();
        form.setUsername("테스터");
        form.setLoginId("unique_test_id");
        form.setPassword("password");
        form.setEmail("test@test.com");
        form.setGender(MemberGender.MALE);
        form.setPhoneNumber("010-1234-5678");

        service.saveMember(form);

        //when & then
        assertThatThrownBy(() -> service.saveMember(form))
                .isInstanceOf(CoreException.class);
    }
}
