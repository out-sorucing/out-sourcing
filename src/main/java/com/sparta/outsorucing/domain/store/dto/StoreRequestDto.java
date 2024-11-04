package com.sparta.outsorucing.domain.store.dto;


import jakarta.validation.constraints.NotBlank;
import java.sql.Time;
import lombok.Getter;

@Getter
public class StoreRequestDto {

    private Long memberId; // 토큰 정보 생기기전 임시
    private String status;

    @NotBlank(message="가게명은 필수로 입력해주시길 바랍니다.")
    private String storeName;
    private String openTime;
    private String closeTime;
    private int minPrice;
}
