package com.sparta.outsorucing.common.dto;

import com.sparta.outsorucing.common.enums.MemberRole;
import lombok.Getter;

@Getter
public class AuthMember {

    private final Long id;
    private final String nickName;
    private final String email;
    private final MemberRole memberRole;

    public AuthMember(Long id, String nickName, String email, MemberRole memberRole) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.memberRole = memberRole;
    }
}
