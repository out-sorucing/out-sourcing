package com.sparta.outsorucing.domain.review.service;

import com.sparta.outsorucing.common.enums.OrderStatus;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.member.repository.MemberRepository;
import com.sparta.outsorucing.domain.menu.entity.Menu;
import com.sparta.outsorucing.domain.order.entity.Order;
import com.sparta.outsorucing.domain.order.repository.OrderRepository;
import com.sparta.outsorucing.domain.review.dto.ReviewRequestDto;
import com.sparta.outsorucing.domain.review.dto.ReviewResponseDto;
import com.sparta.outsorucing.domain.review.entity.Review;
import com.sparta.outsorucing.domain.review.repository.ReviewRepository;
import com.sparta.outsorucing.domain.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private ReviewService reviewService;


    @Test
    @DisplayName("리뷰 생성 - 성공 테스트")
    void createReview() {
        //given
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        ReflectionTestUtils.setField(reviewRequestDto, "content", "맛있네용");
        ReflectionTestUtils.setField(reviewRequestDto, "rating", 1);

        Member member = new Member();
        ReflectionTestUtils.setField(member,"id", 1L);

        Menu menu = Menu.builder().build();
        Store store = new Store();

        Order order = Order.builder()
                .id(1L)
                .member(member)
                .menu(menu)
                .store(store)
                .price(12000)
                .status(OrderStatus.COMPLETED)
                .build();

        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        //when
        ReviewResponseDto response = reviewService.createReview(member.getId(), review.getId(), reviewRequestDto);

        //then
        assertEquals(response.getContent(), "맛있네용");
        assertEquals(response.getRating(), 1);
        assertEquals(response.getId(), member.getId());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("리뷰 생성 - 실패 테스트(자신의 주문이 아닌 경우)")
    void createReviewByWrongOrder() {
        //given
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        ReflectionTestUtils.setField(reviewRequestDto, "content", "맛있네용");
        ReflectionTestUtils.setField(reviewRequestDto, "rating", 1);

        Member orderMember = new Member();
        ReflectionTestUtils.setField(orderMember, "id", 1L);

        Member otherMember = new Member();
        ReflectionTestUtils.setField(otherMember, "id", 2L);

        Menu menu = Menu.builder().build();
        Store store = new Store();

        Order order = Order.builder()
                .id(1L)
                .member(orderMember)
                .menu(menu)
                .store(store)
                .price(12000)
                .status(OrderStatus.COMPLETED)
                .build();

        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 1L);

        when(memberRepository.findById(2L)).thenReturn(Optional.of(otherMember));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        //when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reviewService.createReview(otherMember.getId(), order.getId(), reviewRequestDto));

        //then
        assertEquals(ex.getMessage(), "자신의 주문에만 리뷰를 작성할 수 있습니다.");
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    @DisplayName("리뷰 생성 - 실패 테스트(한 주문에 여러 개의 리뷰를 생성할 경우)")
    void createManyReviewByOrder() {
        //given
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        ReflectionTestUtils.setField(reviewRequestDto, "content", "맛있네용");
        ReflectionTestUtils.setField(reviewRequestDto, "rating", 1);

        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L);

        Menu menu = Menu.builder().build();
        Store store = new Store();

        Order order = Order.builder()
                .id(1L)
                .member(member)
                .menu(menu)
                .store(store)
                .price(12000)
                .status(OrderStatus.COMPLETED)
                .build();

        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(reviewRepository.existsByOrderId(1L)).thenReturn(true);

        //when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reviewService.createReview(member.getId(), order.getId(), reviewRequestDto));

        //then
        assertEquals(ex.getMessage(), "한 주문당 하나의 리뷰만 작성할 수 있습니다");
        verify(reviewRepository, times(0)).save(any(Review.class));
    }


}
