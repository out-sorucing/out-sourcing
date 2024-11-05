package com.sparta.outsorucing.domain.review.controller;

import com.sparta.outsorucing.common.annotation.Auth;
import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.domain.review.dto.ReviewRequestDto;
import com.sparta.outsorucing.domain.review.dto.ReviewResponseDto;
import com.sparta.outsorucing.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@Auth AuthMember authMember, @PathVariable Long orderId, @RequestBody ReviewRequestDto reviewRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reviewService.createReview(authMember.getId(), orderId, reviewRequestDto));
    }

    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> findReviewsByCreatedAt(@PathVariable Long storeId) {
        List<ReviewResponseDto> reviews = reviewService.findReviewsByCreatedAt(storeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reviews);
    }

    @GetMapping("/stores/{storeId}/reviews/rating")
    public ResponseEntity<List<ReviewResponseDto>> findReviewsByRating(@PathVariable Long storeId, @RequestParam int rating) {
        List<ReviewResponseDto> reviews = reviewService.findReviewsByRating(storeId, rating);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reviews);
    }

}