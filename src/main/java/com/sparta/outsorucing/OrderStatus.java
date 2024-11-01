package com.sparta.outsorucing;

public enum OrderStatus {
    ORDERED("ORDERED"),
    COOKING("COOKING"),
    DELIVERING("DELIVERING"),
    COMPLETED("COMPLETED");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
