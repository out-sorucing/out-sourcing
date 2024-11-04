package com.sparta.outsorucing.common.enums;



public enum OrderStatus {
    CANCELED("CANCELED"),
    ORDERED("ORDERED"),
    COOKING("COOKING"),
    DELIVERING("DELIVERING"),
    COMPLETED("COMPLETED");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
