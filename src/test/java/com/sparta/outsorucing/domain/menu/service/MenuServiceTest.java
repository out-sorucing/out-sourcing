package com.sparta.outsorucing.domain.menu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuResponseDto;
import com.sparta.outsorucing.domain.menu.entity.Menu;
import com.sparta.outsorucing.domain.menu.repository.MenuRepository;
import com.sparta.outsorucing.domain.store.entity.Store;
import com.sparta.outsorucing.domain.store.repository.StoreRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 추가 성공 테스트")
    void test(){
        //given
        Store store = new Store();
        Member member = new Member();
        Menu menu = Menu.builder()
            .menuName("createMenuRequest")
            .price(1234)
            .content("createMe")
            .status(Status.ACTIVE)
            .store(store)
            .build();
        CreateMenuRequestDto createMenuRequestDto = new CreateMenuRequestDto();
        ReflectionTestUtils.setField(store, "id", 1L);
        ReflectionTestUtils.setField(store, "member", member);
        ReflectionTestUtils.setField(member, "id", 1L);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.save(any())).thenReturn(menu);

        //when
        CreateMenuResponseDto create = menuService.createMenu(1L, createMenuRequestDto, member);
        //then
        assertEquals(create.getMenuName(), "createMenuRequest");
        verify(menuRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 가게일 시 예외처리")
    void test1(){
        //given
        Member member = new Member();
        CreateMenuRequestDto createMenuRequestDto = new CreateMenuRequestDto();

        //when
        IllegalArgumentException ex  = assertThrows(IllegalArgumentException.class, ()->menuService.createMenu(1L, createMenuRequestDto, member));
        //then
        assertEquals(ex.getMessage(),"존재하지 않는 가게입니다.");
        verify(menuRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("본인의 가게가 아닐 시 예외처리")
    void test2(){
        //given
        Store store = new Store();
        Member member = new Member();
        Member member1 = new Member();
        CreateMenuRequestDto createMenuRequestDto = new CreateMenuRequestDto();
        ReflectionTestUtils.setField(store, "id", 2L);
        ReflectionTestUtils.setField(store, "member", member1);
        ReflectionTestUtils.setField(member, "id", 2L);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        //when
        IllegalArgumentException ex  = assertThrows(IllegalArgumentException.class, ()->menuService.createMenu(1L, createMenuRequestDto, member));
        //then
        assertEquals(ex.getMessage(),"본인 가게가 아닙니다.");
        verify(menuRepository,times(0)).save(any());
    }
}
