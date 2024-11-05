package com.sparta.outsorucing.domain.favorites.dto;

import com.sparta.outsorucing.domain.favorites.entity.Favorites;
import com.sparta.outsorucing.domain.store.entity.Store;

public class FavoritesResponseDto {
    private final Long id;
    private final Long memberId;
    private final Long storeId;
    private final String storeName;
    private final String openTime;
    private final String closeTime;
    private final int minPrice;

    public FavoritesResponseDto(Favorites favorites) {
        this.id = favorites.getId();
        this.memberId = favorites.getMemberId();
        this.storeId = favorites.getStoreId();
        this.storeName = favorites.getStoreName();
        this.openTime = favorites.getOpenTime();
        this.closeTime = favorites.getCloseTime();
        this.minPrice = favorites.getMinPrice();
    }
}
