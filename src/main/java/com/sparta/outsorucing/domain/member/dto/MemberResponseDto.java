package com.sparta.outsorucing.domain.member.dto;

import com.sparta.outsorucing.common.enums.MemberRole;
import com.sparta.outsorucing.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberResponseDto {
    private Long memberId;
    private String nickName;
    private String email;
    private MemberRole role;
    private Status status;
}
