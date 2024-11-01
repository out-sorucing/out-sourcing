package com.sparta.outsorucing;

public enum MemberRole {
    OWNER("OWNER"),
    USER("USER");

    private final String authority;

    MemberRole(String authority) {
        this.authority = authority;
    }
}
