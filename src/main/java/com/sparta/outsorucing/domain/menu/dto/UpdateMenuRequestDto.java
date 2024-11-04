package com.sparta.outsorucing.domain.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class UpdateMenuRequestDto {
    @NotBlank(message = "메뉴 이름을 입력해주세요.")
    private String menuName;
    @Positive(message = "제대로된 가격을 입력해주세요.")
    private int price;
    @NotBlank(message = "메뉴 설명을 입력해주세요.")
    private String content;
}
