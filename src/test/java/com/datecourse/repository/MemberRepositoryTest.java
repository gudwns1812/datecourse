package com.datecourse.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.datecourse.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository repository;

    @Test
    void repositoryTest() {
        //given
        Member member = Member.builder()
                .build();
        //when
        Member savedMember = repository.save(member);
        //then
        assertThat(savedMember.getId())
                .isEqualTo(2);
    }
}
