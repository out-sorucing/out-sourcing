package com.sparta.outsorucing.common.enums;

public enum Status {
    DELETE("DELETE"),
    ACTIVE("ACTIVE"),
    VIP("VIP");

    private String status;

    Status(String status) {
        this.status = status;
    }
}
