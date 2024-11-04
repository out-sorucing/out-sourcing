package com.sparta.outsorucing.domain.store.service;

import com.sparta.outsorucing.common.config.JwtUtil;
import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.common.exception.InvalidRequestException;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.member.repository.MemberRepository;
import com.sparta.outsorucing.domain.store.dto.StoreRequestDto;
import com.sparta.outsorucing.domain.store.dto.StoreResponseDto;
import com.sparta.outsorucing.domain.store.entity.Store;
import com.sparta.outsorucing.domain.store.repository.StoreRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public StoreResponseDto createStore(StoreRequestDto requestDto, Long memberId) {
        int limitCount = storeRepository.countByMemberId(memberId);
        if(limitCount >= 3) {
            throw new IllegalArgumentException("가게는 최대 3개까지만 생성이 가능합니다.");
        }

        Member member = findMemberId(memberId);
        Store savedStore = storeRepository.save(new Store(requestDto, Status.ACTIVE, member));
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

    @Transactional
    public Long updateStore(Long id, StoreRequestDto requestDto, Long memberId) {
        Store store = findOneStoreId(id);
        if (!memberId.equals(store.getMember().getId())) {
            throw new InvalidRequestException("본인 가게만 수정할 수 있습니다.");
        }
        store.update(requestDto);
        return id;
    }

    @Transactional
    public Long deleteStore(Long id, Long memberId){
        Store store = findOneStoreId(id);
        if (!memberId.equals(store.getMember().getId())) {
            throw new InvalidRequestException("본인 가게만 폐업할 수 있습니다.");
        }
        store.storeClose(Status.DELETE);
        String storeName = store.getStoreName();
        return Long.valueOf(storeName);
    }

    public Store findOneStoreId(Long id){
        return storeRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 가게 입니다.")
        );
    }

    public Member findMemberId(Long id){
        return memberRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("비정상적인 접근")
        );
    }



}
