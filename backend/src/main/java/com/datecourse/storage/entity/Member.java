package com.datecourse.domain.member;

import com.datecourse.support.auth.USER_ROLE;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder(access = AccessLevel.PRIVATE)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String loginId;
    private String password;
    private String email;
    private String gender;
    private String phoneNumber;
    private USER_ROLE role;
    private String providerId;

    /**
     * OAuth2 회원 가입을 위한 정적 팩토리 메서드 (providerId 포함)
     */
    public static Member createMember(String username, String loginId, String password, String email, String gender,
                                      String phoneNumber, String providerId) {
        return Member.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .email(email)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .providerId(providerId)
                .role(USER_ROLE.ROLE_USER)
                .build();
    }

    public static Member createDefaultMember() {
        return Member.builder()
                .role(USER_ROLE.ROLE_GUEST)
                .build();
    }
}
