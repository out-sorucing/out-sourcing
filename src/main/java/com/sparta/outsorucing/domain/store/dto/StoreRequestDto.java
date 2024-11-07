package com.sparta.outsorucing.domain.store.dto;


import com.sparta.outsorucing.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import javax.print.DocFlavor.STRING;
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

    // 테스트용 request 생성자
    public StoreRequestDto(String storeName, String status, String openTime, String closeTime, int minPrice, Long memberId) {
        this.storeName = storeName;
        this.status = status;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minPrice = minPrice;
        this.memberId = memberId;
    }
}
