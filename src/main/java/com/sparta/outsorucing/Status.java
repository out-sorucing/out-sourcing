package com.sparta.outsorucing;

public enum Status {
    DELETE("DELETE"),
    ACTIVE("ACTIVE");

    private String status;

    Status(String status) {
        this.status = status;
    }
}
