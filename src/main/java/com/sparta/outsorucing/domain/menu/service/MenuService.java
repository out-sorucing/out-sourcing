package com.sparta.outsorucing.domain.menu.service;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.common.exception.InvalidRequestException;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuResponseDto;
import com.sparta.outsorucing.domain.menu.dto.UpdateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.entity.Menu;
import com.sparta.outsorucing.domain.menu.repository.MenuRepository;
import com.sparta.outsorucing.domain.store.entity.Store;
import com.sparta.outsorucing.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public CreateMenuResponseDto createMenu(
        Long storeId,
        CreateMenuRequestDto createMenuRequestDto,
        Long memberId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new InvalidRequestException("존재하지 않는 가게입니다."));
        if (!memberId.equals(store.getMember().getId())) {
            throw new InvalidRequestException("본인 가게가 아닙니다.");
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

    @Transactional
    public CreateMenuResponseDto UpdateMenu(
        Long storeId,
        Long menuId,
        UpdateMenuRequestDto updateMenuRequestDto,
        Long memberId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new InvalidRequestException("존재하지 않는 가게입니다."));
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new InvalidRequestException("존재하지 않는 메뉴입니다."));
        if (!memberId.equals(store.getMember().getId())) {
            throw new InvalidRequestException("본인 가게가 아닙니다.");
        }
        if(!menu.getStore().getId().equals(store.getId())) {
            throw new InvalidRequestException("해당 가게에 존재하지 않는 메뉴입니다.");
        }
        menu.updateMenu(updateMenuRequestDto.getMenuName(), updateMenuRequestDto.getPrice(), updateMenuRequestDto.getContent());
        return new CreateMenuResponseDto(menu);
    }

    @Transactional
    public String deleteMenu(
        Long storeId,
        Long menuId,
        Long memberId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new InvalidRequestException("존재하지 않는 가게입니다."));
        Menu menu = menuRepository.findByMenuIdAndStoreId(menuId, storeId);
        if (!memberId.equals(store.getMember().getId())) {
            throw new InvalidRequestException("본인 가게가 아닙니다.");
        }
        if (menu.checkedStatus()) {
            throw new InvalidRequestException("이미 삭제된 메뉴입니다.");
        }
        menu.updateStatus();
        return menu.getMenuName();
    }
}
