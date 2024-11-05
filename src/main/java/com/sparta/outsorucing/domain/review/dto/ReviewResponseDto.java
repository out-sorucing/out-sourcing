package com.sparta.outsorucing.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private Long writer;
    private String content;
    private int rating;
}
