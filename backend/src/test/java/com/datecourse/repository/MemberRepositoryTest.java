package com.datecourse.repository;

import static com.datecourse.storage.constant.MemberGender.MALE;
import static org.assertj.core.api.Assertions.assertThat;

import com.datecourse.storage.entity.Member;
import com.datecourse.storage.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    MemberRepository repository;

    @Test
    void repositoryTest() {
        //given
        Member member = Member.createMember("테스터", "test_repo", "test!", "test123@gmail.com", MALE, "010-1234-5678",
                "test_provider");
        //when
        Member savedMember = repository.save(member);
        //then
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getCreatedAt()).isNotNull();
        assertThat(savedMember.getLastModifiedAt()).isNotNull();
    }
}
