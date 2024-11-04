package com.sparta.outsorucing.domain.store.dto;

import com.sparta.outsorucing.domain.store.entity.Store;
import java.sql.Time;
import lombok.Getter;

@Getter
public class StoreResponseDto {
    private Long id;
    private Long memberId; // 토큰 정보 생기기전 임시
    private String storeName;
    private String status;
    private String openTime;
    private String closeTime;
    private int minPrice;

    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.memberId = store.getMemberId();
        this.storeName = store.getStoreName();
        this.status = store.getStatus().toString();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minPrice = store.getMinPrice();
    }

}
