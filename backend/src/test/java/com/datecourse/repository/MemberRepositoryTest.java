package com.datecourse.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.datecourse.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository repository;

    @Test
    void repositoryTest() {
        //given
        Member member = Member.createMember("테스터", "test_repo", "test!", "test123@gmail.com", "M", "010-1234-5678");
        //when
        Member savedMember = repository.save(member);
        //then
        assertThat(savedMember.getId())
                .isNotNull();
    }
}
