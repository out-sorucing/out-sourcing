package com.sparta.outsorucing.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {
    private String content;
    private int rating;
}