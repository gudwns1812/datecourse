package com.datecourse.storage.entity;

import static com.datecourse.support.auth.UserRole.ROLE_GUEST;
import static com.datecourse.support.auth.UserRole.ROLE_USER;

import com.datecourse.storage.constant.MemberGender;
import com.datecourse.support.auth.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
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
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String username;
    private String nickname;
    private String loginId;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private MemberGender gender;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private UserRole role;
    private String providerId;
    private LocalDateTime deletedAt;

    /**
     * OAuth2 회원 가입을 위한 정적 팩토리 메서드 (providerId 포함)
     */
    public static Member createMember(String username, String loginId, String password, String email,
                                      MemberGender gender, String phoneNumber) {
        return Member.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .email(email)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .role(ROLE_USER)
                .build();
    }

    public static Member createMember(String username, String loginId, String password, String email,
                                      MemberGender gender,
                                      String phoneNumber, String providerId) {
        return Member.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .email(email)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .providerId(providerId)
                .role(ROLE_USER)
                .build();
    }

    public static Member createDefaultMember() {
        return Member.builder()
                .role(ROLE_GUEST)
                .build();
    }
}
