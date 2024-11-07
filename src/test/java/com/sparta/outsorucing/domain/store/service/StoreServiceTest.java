package com.sparta.outsorucing.domain.store.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.domain.favorites.entity.Favorites;
import com.sparta.outsorucing.domain.store.dto.StoreRequestDto;
import com.sparta.outsorucing.domain.store.dto.StoreResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreServiceTest {

    @Autowired
    StoreService storeService;


    StoreResponseDto createStore = null; // 가게 생성
    String close = null; // 가게 폐업
    Favorites favorites = null; // 즐겨찾기 생성
    Long delete = null; // 즐겨찾기 삭제

    @Test
    @Order(1)
    @DisplayName("가게 생성")
    void test1(){
        // given
        String storeName = "테스트 떡볶이집";
        String openTime = "10:00";
        String closeTime = "22:00";
        int minPrice = 14000;
        String status = "ACTIVE";
        Long memberId = 8L;
        StoreRequestDto requestDto = new StoreRequestDto(
            storeName,
            status,
            openTime,
            closeTime,
            minPrice,
            memberId
        );

        String memberRole = "OWNER";

        // when
        StoreResponseDto storeResponseDto = storeService.createStore(requestDto,memberId,memberRole);

        // then
        createStore = storeResponseDto;

    }

    @Test
    @Order(2)
    @DisplayName("가게 폐업")
    void test2(){
        // given
        Long storeId = 19L;
        Long memberId = 8L;
        String memberRole = "OWNER";

        // when
        String storeClose = storeService.deleteStore(storeId,memberId,memberRole);

        // then
        close = storeClose;

    }

    @Test
    @Order(3)
    @DisplayName("즐겨찾기 생성")
    void test3(){
        // given
        Long storeId = 19L;
        Long memberId = 7L;
        String memberRole = "USER";

        // when
        Favorites createFavorites = storeService.createFavorites(storeId,memberId,memberRole);

        // then
        favorites = createFavorites;

    }

    @Test
    @Order(4)
    @DisplayName("즐겨찾기 삭제")
    void test4(){
        // given
        Long favoritesId = 16L;
        Long memberId = 7L;
        String memberRole = "USER";

        // when
        Long favoritesDelete = storeService.deleteFavorites(favoritesId,memberId,memberRole);

        // then
        delete = favoritesDelete;

    }


}
