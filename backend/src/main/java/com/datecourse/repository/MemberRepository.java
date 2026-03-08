package com.datecourse.repository;

import com.datecourse.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginIdAndPassword(String loginId, String password);

    Optional<Member> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
}
