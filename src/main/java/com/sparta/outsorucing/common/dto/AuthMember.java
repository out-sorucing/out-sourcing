package com.sparta.outsorucing.common.dto;

import com.sparta.outsorucing.common.enums.MemberRole;
import com.sparta.outsorucing.common.enums.Status;
import lombok.Getter;

@Getter
public class AuthMember {

    private final Long id;
    private final String nickName;
    private final String email;
    private final MemberRole memberRole;
    private final Status status;

    public AuthMember(Long id, String nickName, String email, MemberRole memberRole, Status status) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.memberRole = memberRole;
        this.status = status;
    }
}
