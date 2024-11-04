package com.sparta.outsorucing.domain.store.controller;

import com.sparta.outsorucing.domain.store.dto.StoreRequestDto;
import com.sparta.outsorucing.domain.store.dto.StoreResponseDto;
import com.sparta.outsorucing.domain.store.service.StoreService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public StoreResponseDto createStore(@RequestBody @Valid StoreRequestDto requestDto) {
        return storeService.createStore(requestDto);
    }

    // 전체 가게목록 조회(소비자 입장 화면)
    @GetMapping
    public List<StoreResponseDto> findStore(){
        return storeService.findStore();
    }


}
