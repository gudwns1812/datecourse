package com.datecourse.domain.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "members")
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

    /**
     * 회원 가입을 위한 정적 팩토리 메서드
     */
    public static Member createMember(String username, String loginId, String password, String email, String gender, String phoneNumber) {
        return Member.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .email(email)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .build();
    }

    public boolean isValidIdAndPassword(String loginId, String password) {
        return this.loginId.equals(loginId) && this.password.equals(password);
    }

    public boolean isSameId(String loginId) {
        return this.loginId.equals(loginId);
    }
}
