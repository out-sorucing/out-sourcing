package com.sparta.outsorucing.domain.store.controller;

import com.sparta.outsorucing.common.annotation.Auth;
import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.domain.favorites.entity.Favorites;
import com.sparta.outsorucing.domain.store.dto.StoreOneResponseDto;
import com.sparta.outsorucing.domain.store.dto.StoreRequestDto;
import com.sparta.outsorucing.domain.store.dto.StoreResponseDto;
import com.sparta.outsorucing.domain.store.service.StoreService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody @Valid StoreRequestDto requestDto, @Auth AuthMember authMember) {
        StoreResponseDto store = storeService.createStore(requestDto, authMember.getId(), String.valueOf(authMember.getMemberRole()));
        return ResponseEntity.status(HttpStatus.OK).body(store);
    }

    // 전체 가게목록 조회(소비자 입장 화면)
    @GetMapping
    public List<StoreResponseDto> findStore(@Auth AuthMember authMember){
        return storeService.findStore(String.valueOf(authMember.getMemberRole()));
    }

    // 가게 검색(소비자 입장 화면)
    @GetMapping("/search")
    public List<StoreResponseDto> findStoreByName(@RequestParam String keyword, @Auth AuthMember authMember){
        return storeService.findStoreByName(keyword, String.valueOf(authMember.getMemberRole()));
    }
  
    // 메뉴 검색
    @GetMapping("/menus/search")
    public ResponseEntity<List<StoreResponseDto>> findByMenuName(@RequestParam String keyword) {
        return ResponseEntity.ok(storeService.findByMenuName(keyword));
    }

    @GetMapping("/{storeId}")
    public List<StoreOneResponseDto> findOneStore(@PathVariable Long storeId) {
        return storeService.findOneStore(storeId);
    }
  
    @PutMapping("/{storeId}")
    public Long updateStore(@PathVariable Long storeId, @RequestBody @Valid StoreRequestDto requestDto, @Auth AuthMember authMember) {
        return storeService.updateStore(storeId,requestDto, authMember.getId(), String.valueOf(authMember.getMemberRole()));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable Long storeId, @Auth AuthMember authMember) {
        String storeName = String.valueOf(storeService.deleteStore(storeId, authMember.getId(), String.valueOf(authMember.getMemberRole())));
        return ResponseEntity.status(HttpStatus.OK).body(storeName + "이 폐업되었습니다.");
    }

    // [즐겨찾기]
    // 생성
    @GetMapping("/favorites/{storeId}")
    public Favorites createFavorites(@PathVariable Long storeId, @Auth AuthMember authMember) {
        return storeService.createFavorites(storeId, authMember.getId(), String.valueOf(authMember.getMemberRole()));
    }

    // 삭제
    @DeleteMapping("/favorites/{id}")
    public Long deleteFavorites(@PathVariable Long id, @Auth AuthMember authMember) {
        return storeService.deleteFavorites(id, authMember.getId(), String.valueOf(authMember.getMemberRole()));
    }

}
