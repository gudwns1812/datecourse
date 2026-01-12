package com.datecourse.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

@RequiredArgsConstructor
@With
@Getter
@Builder
public class Member {
    private final Long id;
    private final String username;
    private final String loginId;
    private final String password;
    private final String email;
    private final String gender;
    private final String phoneNumber;

    public boolean isValidIdAndPassword(String loginId, String password) {
        return this.loginId.equals(loginId) && this.password.equals(password);
    }
}
