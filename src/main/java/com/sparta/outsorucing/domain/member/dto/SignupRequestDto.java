package com.sparta.outsorucing.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "올바른 이메일 형식이 아닙니다")
    private String email;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$", message = "영문과 숫자를 포함한 8자리 비밀번호를 설정해주십시오")
    private String password;
    @NotBlank
    private String nickName;
    @NotBlank
    private String memberRole;
}
