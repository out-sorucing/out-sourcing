package com.sparta.outsorucing.domain.menu.controller;

import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuResponseDto;
import com.sparta.outsorucing.domain.menu.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/{storeId}/menus")
    public ResponseEntity<CreateMenuResponseDto> createMenu(
        @PathVariable Long storeId,
        @Valid @RequestBody CreateMenuRequestDto createMenuRequestDto,
        HttpServletRequest request) {
        return ResponseEntity.ok().body(menuService.createMenu(storeId,
                                                               createMenuRequestDto,
                                                               (Member) request.getAttribute("member")));
    }
}

