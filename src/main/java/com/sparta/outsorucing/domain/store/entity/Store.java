package com.sparta.outsorucing.domain.store.entity;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.store.dto.StoreRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="store")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public Store(StoreRequestDto requestDto, Status status, Member member) {
        this.storeName = requestDto.getStoreName();
        this.openTime = requestDto.getOpenTime();
        this.closeTime = requestDto.getCloseTime();
        this.minPrice = requestDto.getMinPrice();
        this.status = status;
        this.member = member;
    }

    public void update(StoreRequestDto requestDto){
        this.storeName = requestDto.getStoreName();
        this.openTime = requestDto.getOpenTime();
        this.closeTime = requestDto.getCloseTime();
        this.minPrice = requestDto.getMinPrice();
    }

    public void storeClose(Status status){
        this.status = status;
    }

}
