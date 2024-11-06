package com.sparta.outsorucing.domain.menu.dto;

import com.sparta.outsorucing.domain.menu.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {
    private final Long id;
    private final String menuName;
    private final int price;
    private final String content;
    private final String imageUri;

    public MenuResponseDto(Menu menu) {
        this.id = menu.getId();
        this.menuName = menu.getMenuName();
        this.price = menu.getPrice();
        this.content = menu.getContent();
        this.imageUri = menu.getImageUri();
    }
}
