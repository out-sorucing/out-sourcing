package com.sparta.outsorucing.domain.menu.dto;

import com.sparta.outsorucing.domain.menu.entity.Menu;
import lombok.Getter;

@Getter
public class CreateMenuResponseDto {
    private final Long id;
    private final String menuName;
    private final int price;
    private final String content;

    public CreateMenuResponseDto(Menu menu) {
        this.id = menu.getId();
        this.menuName = menu.getMenuName();
        this.price = menu.getPrice();
        this.content = menu.getContent();
    }
}
