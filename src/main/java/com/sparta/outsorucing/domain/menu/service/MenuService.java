package com.sparta.outsorucing.domain.menu.service;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuResponseDto;
import com.sparta.outsorucing.domain.menu.entity.Menu;
import com.sparta.outsorucing.domain.menu.repository.MenuRepository;
import com.sparta.outsorucing.domain.store.entity.Store;
import com.sparta.outsorucing.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public CreateMenuResponseDto createMenu(
        Long storeId,
        CreateMenuRequestDto createMenuRequestDto,
        Member member) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));
        if (!member.getId().equals(store.getMember().getId())) {
            throw new IllegalArgumentException("본인 가게가 아닙니다.");
        }
        Menu menu = Menu.builder()
            .menuName(createMenuRequestDto.getMenuName())
            .price(createMenuRequestDto.getPrice())
            .content(createMenuRequestDto.getContent())
            .status(Status.ACTIVE)
            .store(store)
            .build();
        return new CreateMenuResponseDto(menuRepository.save(menu));
    }
}
