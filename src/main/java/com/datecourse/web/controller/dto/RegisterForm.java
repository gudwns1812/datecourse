package com.datecourse.web.controller.dto;

import com.datecourse.domain.member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class RegisterForm {

    @NotEmpty
    private final String username;

    @NotEmpty(message = "ID를 입력해주세요.")
    @Size(min = 4, message = "최소 4글자 이상 입력해야합니다.")
    private final String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private final String password;

    @NotNull(message = "성별을 클릭해주세요")
    @Pattern(regexp = "[MF]")
    private final String gender;

    @NotNull(message = "생년월일을 선택해주세요.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private final LocalDate birth;

    @Email
    private final String email;

    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
            message = "올바른 휴대폰 번호 형식이어야 합니다. (예: 010-1234-5678)")
    private final String phoneNumber;

    public Member toMember() {
        return Member.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .email(email)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .build();
    }
}
