package com.datecourse;

import com.datecourse.storage.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class DatecourseApplicationTests {

    @MockitoBean
    MemberRepository memberRepository;

    @Test
    void contextLoads() {
    }

}
