package com.sparta.outsorucing.domain.menu.service;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.common.exception.InvalidRequestException;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.dto.MenuResponseDto;
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

    public MenuResponseDto createMenu(
        Long storeId,
        CreateMenuRequestDto createMenuRequestDto,
        Long memberId) {
        Store store = getStore(storeId);
        validateStoreOwner(memberId, store);
        if(menuRepository.existsByMenuNameAndStoreId(createMenuRequestDto.getMenuName(), storeId)){
            throw new InvalidRequestException("이미 가게에 추가되어있는 메뉴입니다.");
        }
        Menu menu = Menu.builder()
            .menuName(createMenuRequestDto.getMenuName())
            .price(createMenuRequestDto.getPrice())
            .content(createMenuRequestDto.getContent())
            .status(Status.ACTIVE)
            .store(store)
            .build();
        return new MenuResponseDto(menuRepository.save(menu));
    }

    @Transactional
    public MenuResponseDto UpdateMenu(
        Long storeId,
        Long menuId,
        UpdateMenuRequestDto updateMenuRequestDto,
        Long memberId) {
        validateStoreOwner(memberId, getStore(storeId));
        if(menuRepository.existsByMenuNameAndStoreId(updateMenuRequestDto.getMenuName(),storeId)){
            throw new InvalidRequestException("이미 가게에 추가되어있는 메뉴명과 같은 이름으로 수정할 수 없습니다.");
        }
        Menu menu = getMenu(menuId, storeId);
        if (menu.checkedStatus()) {
            throw new InvalidRequestException("삭제된 메뉴입니다.");
        }
        menu.updateMenu(updateMenuRequestDto.getMenuName(), updateMenuRequestDto.getPrice(), updateMenuRequestDto.getContent());
        return new MenuResponseDto(menu);
    }

    @Transactional
    public String deleteMenu(
        Long storeId,
        Long menuId,
        Long memberId) {
        validateStoreOwner(memberId, getStore(storeId));
        Menu menu = getMenu(menuId, storeId);
        if (menu.checkedStatus()) {
            throw new InvalidRequestException("이미 삭제된 메뉴입니다.");
        }
        menu.updateStatus();
        return menu.getMenuName();
    }

    private Store getStore(Long storeId) {
       return storeRepository.findById(storeId).orElseThrow(() -> new InvalidRequestException("존재하지 않는 가게입니다."));
    }

    private Menu getMenu(Long menuId, Long storeId){
        return menuRepository.findByIdAndStoreId(menuId, storeId).orElseThrow(() -> new InvalidRequestException("가게에 존재하지 않는 메뉴입니다."));
    }

    private void validateStoreOwner(
        Long memberId,
        Store store) {
        if (!memberId.equals(store.getMember().getId())) {
            throw new InvalidRequestException("본인 가게가 아닙니다.");
        }
    }
}
