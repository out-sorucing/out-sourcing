package com.sparta.outsorucing.domain.store.service;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.domain.store.dto.StoreRequestDto;
import com.sparta.outsorucing.domain.store.dto.StoreResponseDto;
import com.sparta.outsorucing.domain.store.entity.Store;
import com.sparta.outsorucing.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreResponseDto createStore(StoreRequestDto requestDto) {
        Store savedStore = storeRepository.save(new Store(requestDto, Status.ACTIVE));
        return new StoreResponseDto(savedStore);
    }

}
