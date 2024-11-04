package com.sparta.outsorucing.domain.store.service;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.domain.store.dto.StoreRequestDto;
import com.sparta.outsorucing.domain.store.dto.StoreResponseDto;
import com.sparta.outsorucing.domain.store.entity.Store;
import com.sparta.outsorucing.domain.store.repository.StoreRepository;
import java.util.List;
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

    public List<StoreResponseDto> findStore(){
        return storeRepository.findAll().stream().map(StoreResponseDto::new).toList();
    }

    public List<StoreResponseDto> findStoreByName(String keyword){
        return storeRepository.findAllByStoreNameContainsOrderByIdDesc(keyword).stream().map(StoreResponseDto::new).toList();
    }

    public List<StoreResponseDto> findOneStore(Long id){
        return storeRepository.findById(id).stream().map(StoreResponseDto::new).toList();
    }

}
