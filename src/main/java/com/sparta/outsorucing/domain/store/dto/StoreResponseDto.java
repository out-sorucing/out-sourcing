package com.sparta.outsorucing.domain.store.dto;

import com.sparta.outsorucing.domain.member.dto.MemberResponseDto;
import com.sparta.outsorucing.domain.store.entity.Store;
import lombok.Getter;

@Getter
public class StoreResponseDto {
    private final Long id;
    private final Long memberId;
    private final String storeName;
    private final String status;
    private final String openTime;
    private final String closeTime;
    private final int minPrice;

    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.memberId = store.getMember().getId();
        this.storeName = store.getStoreName();
        this.status = store.getStatus().toString();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minPrice = store.getMinPrice();
    }

}
