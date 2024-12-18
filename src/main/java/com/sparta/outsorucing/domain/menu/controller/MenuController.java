package com.sparta.outsorucing.domain.menu.controller;

import com.sparta.outsorucing.common.annotation.Auth;
import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.dto.MenuResponseDto;
import com.sparta.outsorucing.domain.menu.dto.UpdateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/{storeId}/menus")
    public ResponseEntity<MenuResponseDto> createMenu(
        @PathVariable Long storeId,
        @Valid @RequestPart CreateMenuRequestDto createMenuRequestDto,
        @RequestPart(required = false) MultipartFile file,
        @Auth AuthMember authMember) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.createMenu(storeId,
                                                               createMenuRequestDto,
                                                               file,
                                                               authMember.getId()));
    }

    @PutMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
        @PathVariable("storeId") Long storeId,
        @PathVariable("menuId") Long menuId,
        @Valid @RequestPart UpdateMenuRequestDto updateMenuRequestDto,
        @RequestPart(required = false) MultipartFile file,
        @Auth AuthMember authMember) {
        return ResponseEntity.status(HttpStatus.OK).body(menuService.UpdateMenu(storeId,
                                                               menuId,
                                                               updateMenuRequestDto,
                                                               file,
                                                               authMember.getId()));
    }

    @DeleteMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<String> deleteMenu(
        @PathVariable("storeId") Long storeId,
        @PathVariable("menuId") Long menuId,
        @Auth AuthMember authMember) {
        String menuName = menuService.deleteMenu(storeId,
                                                 menuId,
                                                 authMember.getId());
        return ResponseEntity.status(HttpStatus.OK).body(menuName + "이(가) 메뉴에서 삭제되었습니다.");
    }
}

