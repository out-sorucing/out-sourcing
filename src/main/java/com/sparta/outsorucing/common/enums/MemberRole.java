package com.sparta.outsorucing.common.enums;

import com.sparta.outsorucing.common.exception.InvalidRequestException;

import java.util.Arrays;

public enum MemberRole {
    OWNER("OWNER"),
    USER("USER");

    private final String authority;

    MemberRole(String authority) {
        this.authority = authority;
    }

    public static MemberRole of(String role) {
        return Arrays.stream(MemberRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 MemberRole 입니다"));
    }
}
