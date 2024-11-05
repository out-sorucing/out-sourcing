package com.sparta.outsorucing.domain.store.dto;
import com.sparta.outsorucing.domain.menu.dto.MenuResponseDto;
import com.sparta.outsorucing.domain.store.entity.Store;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class StoreOneResponseDto {

    private final String storeName;
    private final String openTime;
    private final String closeTime;
    private final int minPrice;
//    private final List<MenuResponseDto> menu;

    public StoreOneResponseDto(Store store) {
        this.storeName = store.getStoreName();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minPrice = store.getMinPrice();
    }
}
