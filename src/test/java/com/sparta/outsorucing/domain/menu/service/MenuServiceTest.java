package com.sparta.outsorucing.domain.menu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.common.exception.InvalidRequestException;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.dto.MenuResponseDto;
import com.sparta.outsorucing.domain.menu.dto.UpdateMenuRequestDto;
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
    @DisplayName("메뉴 생성 - 메뉴 추가 성공 테스트")
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
        MenuResponseDto create = menuService.createMenu(1L, createMenuRequestDto, member.getId());
        //then
        assertEquals(create.getMenuName(), "createMenuRequest");
        verify(menuRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("메뉴 생성 - 존재하지 않는 가게일 시 예외처리")
    void test1(){
        //given
        Member member = new Member();
        CreateMenuRequestDto createMenuRequestDto = new CreateMenuRequestDto();

        //when
        InvalidRequestException ex  = assertThrows(InvalidRequestException.class, ()->menuService.createMenu(1L, createMenuRequestDto, member.getId()));
        //then
        assertEquals(ex.getMessage(),"존재하지 않는 가게입니다.");
        verify(menuRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("메뉴 생성 - 본인의 가게가 아닐 시 예외처리")
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
        InvalidRequestException ex  = assertThrows(InvalidRequestException.class, ()->menuService.createMenu(1L, createMenuRequestDto, member.getId()));
        //then
        assertEquals(ex.getMessage(),"본인 가게가 아닙니다.");
        verify(menuRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("메뉴 수정 - 본인의 가게가 아닐 시 예외처리")
    void test3(){
        //given
        Store store = new Store();
        Member member = new Member();
        Member member1 = new Member();
        UpdateMenuRequestDto updateMenuRequestDto = new UpdateMenuRequestDto();
        ReflectionTestUtils.setField(store, "id", 2L);
        ReflectionTestUtils.setField(store, "member", member1);
        ReflectionTestUtils.setField(member, "id", 2L);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class, ()-> menuService.UpdateMenu(1L,1L, updateMenuRequestDto, member.getId()));
        //then
        assertEquals(ex.getMessage(),"본인 가게가 아닙니다.");
    }

    @Test
    @DisplayName("메뉴 수정 - 삭제된 메뉴 수정 요청 시 예외처리")
    void test4(){
        //given
        Store store = new Store();
        Member member = new Member();
        Menu menu = Menu.builder()
            .menuName("createMenuRequest")
            .price(1234)
            .content("createMe")
            .status(Status.DELETE)
            .store(store)
            .build();
        UpdateMenuRequestDto updateMenuRequestDto = new UpdateMenuRequestDto();
        ReflectionTestUtils.setField(store, "id", 1L);
        ReflectionTestUtils.setField(store, "member", member);
        ReflectionTestUtils.setField(member, "id", 1L);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findByIdAndStoreId(any(),any())).thenReturn(Optional.of(menu));

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class, ()-> menuService.UpdateMenu(1L,1L, updateMenuRequestDto, member.getId()));
        //then
        assertEquals(ex.getMessage(),"삭제된 메뉴입니다.");
    }

    @Test
    @DisplayName("메뉴 수정 - 존재하지 않는 가게일 시 예외처리")
    void test5(){
        //given
        UpdateMenuRequestDto updateMenuRequestDto = new UpdateMenuRequestDto();


        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class, ()-> menuService.UpdateMenu(1L,1L, updateMenuRequestDto, 1L));
        //then
        assertEquals(ex.getMessage(),"존재하지 않는 가게입니다.");
    }

    @Test
    @DisplayName("메뉴 수정 - 가게에 존재하지 않는 메뉴 수정 요청 시 예외처리")
    void test6(){
        //given
        UpdateMenuRequestDto updateMenuRequestDto = new UpdateMenuRequestDto();
        Store store = new Store();
        Member member = new Member();
        ReflectionTestUtils.setField(store, "id", 1L);
        ReflectionTestUtils.setField(store, "member", member);
        ReflectionTestUtils.setField(member, "id", 1L);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findByIdAndStoreId(1L,1L)).thenReturn(Optional.empty());

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class, ()-> menuService.UpdateMenu(1L,1L, updateMenuRequestDto, 1L));
        //then
        assertEquals(ex.getMessage(),"가게에 존재하지 않는 메뉴입니다.");
    }

    @Test
    @DisplayName("메뉴 수정 - 수정 성공")
    void test7(){
        //given
        UpdateMenuRequestDto updateMenuRequestDto = new UpdateMenuRequestDto();
        Store store = new Store();
        Member member = new Member();
        Menu menu = Menu.builder()
            .menuName("createMenuRequest")
            .price(1234)
            .content("createMe")
            .status(Status.ACTIVE)
            .store(store)
            .build();
        ReflectionTestUtils.setField(store, "id", 1L);
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(menu, "id", 1L);
        ReflectionTestUtils.setField(store, "member", member);
        ReflectionTestUtils.setField(updateMenuRequestDto, "menuName", "update");
        ReflectionTestUtils.setField(updateMenuRequestDto, "price", 20000);
        ReflectionTestUtils.setField(updateMenuRequestDto, "content", "upupd");

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findByIdAndStoreId(1L,1L)).thenReturn(Optional.of(menu));

        //when
        MenuResponseDto menuResponseDto =  menuService.UpdateMenu(1L,1L, updateMenuRequestDto, 1L);
        //then
        assertEquals(menuResponseDto.getMenuName(),"update");
        assertEquals(menuResponseDto.getPrice(),20000);
        assertEquals(menuResponseDto.getContent(),"upupd");
    }
}
