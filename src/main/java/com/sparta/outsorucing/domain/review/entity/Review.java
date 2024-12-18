package com.sparta.outsorucing.domain.review.entity;

import com.sparta.outsorucing.common.AuditingDate;
import com.sparta.outsorucing.domain.order.entity.Order;
import com.sparta.outsorucing.domain.review.dto.ReviewRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name="review")
@NoArgsConstructor
public class Review extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "리뷰 고유번호")
    private Long id;

    @Column
    private String content;
    @Column
    private int rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    public static Review createReview(Order order, ReviewRequestDto reviewRequestDto) {
        Review review = new Review();
        review.content = reviewRequestDto.getContent();
        review.rating = reviewRequestDto.getRating();
        review.order = order;

        return review;
    }
}
