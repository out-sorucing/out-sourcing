package com.sparta.outsorucing.domain.member.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String email;
    private String password;
    private String nickName;
    private String memberRole;
}
