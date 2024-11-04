package com.sparta.outsorucing.domain.store.entity;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.domain.store.dto.StoreRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name="out_store")
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "가게 고유번호")
    private Long id;

    @Column
    @Comment(value = "가게명")
    private String storeName;

    @Column
    private String openTime;

    @Column
    private String closeTime;

    @Column
    private int minPrice;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column
    private Long memberId;

    // [ 로그인 기능 끝나면 가져다 쓰기 ]
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Member member;


    public Store(StoreRequestDto requestDto, Status status) {
        this.storeName = requestDto.getStoreName();
        this.memberId = requestDto.getMemberId();
        this.openTime = requestDto.getOpenTime();
        this.closeTime = requestDto.getCloseTime();
        this.minPrice = requestDto.getMinPrice();
        this.status = status;
    }
}
