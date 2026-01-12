package com.datecourse.repository;

import com.datecourse.domain.member.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByLoginId(String loginId, String password);

    List<Member> findAll();
}
