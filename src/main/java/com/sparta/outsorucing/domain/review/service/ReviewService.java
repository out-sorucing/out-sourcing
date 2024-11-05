package com.sparta.outsorucing.domain.review.service;

import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.member.repository.MemberRepository;
import com.sparta.outsorucing.domain.order.entity.Order;
import com.sparta.outsorucing.domain.order.repository.OrderRepository;
import com.sparta.outsorucing.domain.review.dto.ReviewRequestDto;
import com.sparta.outsorucing.domain.review.dto.ReviewResponseDto;
import com.sparta.outsorucing.domain.review.entity.Review;
import com.sparta.outsorucing.domain.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.outsorucing.common.enums.OrderStatus.COMPLETED;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    public ReviewResponseDto createReview(Long id, Long orderId, ReviewRequestDto reviewRequestDto) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Member not found")
        );
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new IllegalArgumentException("Order not found")
        );

        if(!member.getId().equals(order.getMember().getId())) {
            throw new IllegalArgumentException("자신의 주문에만 리뷰를 작성할 수 있습니다.");
        }

        if(!order.getStatus().equals(COMPLETED)) {
            throw new IllegalArgumentException("완료된 주문에만 리뷰를 작성할 수 있습니다.");
        }

        if(reviewRequestDto.getRating()<1||reviewRequestDto.getRating()>5) {
            throw new IllegalArgumentException("1-5 사이의 정수로 별점을 부여할 수 있습니다");
        }

        if(reviewRepository.existsByOrderId(orderId)){
            throw new IllegalArgumentException("한 주문당 하나의 리뷰만 작성할 수 있습니다");
        }

        int rating = (Integer)reviewRequestDto.getRating();

        Review review = reviewRepository.save(Review.createReview(order, reviewRequestDto));

        return new ReviewResponseDto(review.getId(), id, reviewRequestDto.getContent(), rating);
    }

    @Transactional
    public List<ReviewResponseDto> findReviewsByCreatedAt(Long storeId) {
        List<Review> reviews = reviewRepository.findByStoreIdOrderByCreatedAtDesc(storeId);

        return reviews.stream().map(review ->
                new ReviewResponseDto(
                        review.getId(),
                        review.getOrder().getMember().getId(),
                        review.getContent(),
                        review.getRating()
                )
        ).collect(Collectors.toList());
    }

    public List<ReviewResponseDto> findReviewsByRating(Long storeId, int rating) {
        List<Review> reviews = reviewRepository.findByStoreIdAndRating(storeId, rating);
        return reviews.stream().map(review ->
                new ReviewResponseDto(
                        review.getId(),
                        review.getOrder().getMember().getId(),
                        review.getContent(),
                        review.getRating()
                )
        ).collect(Collectors.toList());
    }
}
