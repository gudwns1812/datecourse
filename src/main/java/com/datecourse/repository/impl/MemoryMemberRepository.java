package com.datecourse.repository.impl;

import com.datecourse.domain.member.Member;
import com.datecourse.repository.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryMemberRepository implements MemberRepository {

    private static final Map<Long, Member> store = new ConcurrentHashMap<>();
    private static Long sequence = 0L;

    @Override
    public Member save(Member member) {
        Member savedMember = member.withId(++sequence);

        store.put(savedMember.getId(), savedMember);
        return savedMember;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = store.get(id);

        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId, String password) {
        return findAll().stream()
                .filter(member -> member.isValidIdAndPassword(loginId, password))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return store.values().stream().toList();
    }


}
