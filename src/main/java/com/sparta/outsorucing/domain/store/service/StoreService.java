package com.sparta.outsorucing.domain.store.service;

import com.sparta.outsorucing.common.config.JwtUtil;
import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.common.exception.InvalidRequestException;
import com.sparta.outsorucing.domain.favorites.entity.Favorites;
import com.sparta.outsorucing.domain.favorites.repository.FavoritesRepository;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.member.repository.MemberRepository;
import com.sparta.outsorucing.domain.store.dto.StoreOneResponseDto;
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
    private final FavoritesRepository favoritesRepository;

    public StoreResponseDto createStore(StoreRequestDto requestDto, Long memberId, String memberRole) {
        if(memberRole.equals("USER")) {
            throw new IllegalArgumentException("사장님 회원만 가게 생성이 가능합니다.");
        }

        int limitCount = storeRepository.countByMemberIdAndStatus(memberId,Status.ACTIVE);
        if(limitCount >= 3) {
            throw new IllegalArgumentException("가게는 최대 3개까지만 생성이 가능합니다.");
        }

        Member member = findMemberId(memberId);
        Store savedStore = storeRepository.save(new Store(requestDto, Status.ACTIVE, member));
        return new StoreResponseDto(savedStore);
    }

    // 전체 가게목록 조회(소비자 입장 화면)
    public List<StoreResponseDto> findStore(String memberRole){
        if(memberRole.equals("OWNER")) {
            throw new IllegalArgumentException("일반회원들만 전체 가게를 조회할 수 있습니다.");
        }
        return storeRepository.findAll().stream().map(StoreResponseDto::new).toList();
    }

    // 가게 검색(소비자 입장 화면)
    public List<StoreResponseDto> findStoreByName(String keyword, String memberRole){
        if(memberRole.equals("OWNER")) {
            throw new IllegalArgumentException("일반회원들만 가게를 검색할 수 있습니다.");
        }
        return storeRepository.findAllByStoreNameContainsOrderByIdDesc(keyword).stream().map(StoreResponseDto::new).toList();
    }

    public List<StoreOneResponseDto> findOneStore(Long storeId){
        return storeRepository.findOneStoreAndMenu(storeId).stream().map(StoreOneResponseDto::new).toList();
    }

    public List<StoreResponseDto> findByMenuName(String keyword) {
        return storeRepository.findAllByMenuName(keyword).stream().map(StoreResponseDto::new).toList();
    }

    @Transactional
    public Long updateStore(Long storeId, StoreRequestDto requestDto, Long memberId, String memberRole) {
        if(memberRole.equals("USER")) {
            throw new IllegalArgumentException("사장님 회원만 가게 수정이 가능합니다.");
        }
        Store store = findOneStoreId(storeId);
        if (!memberId.equals(store.getMember().getId())) {
            throw new InvalidRequestException("본인 가게만 수정할 수 있습니다.");
        }
        store.update(requestDto);
        return storeId;
    }

    @Transactional
    public String deleteStore(Long storeId, Long memberId, String memberRole){
        if(memberRole.equals("USER")) {
            throw new IllegalArgumentException("사장님 회원만 폐업 처리가 가능합니다.");
        }
        Store store = findOneStoreId(storeId);
        if (!memberId.equals(store.getMember().getId())) {
            throw new InvalidRequestException("본인 가게만 폐업할 수 있습니다.");
        }
        if(store.getStatus().equals(Status.DELETE)) {
            throw new InvalidRequestException("이미 폐업된 가게입니다.");
        }
        store.storeClose(Status.DELETE);
        return store.getStoreName();
    }

    public Favorites createFavorites(Long storeId, Long memberId, String memberRole) {
        findMemberId(memberId);

        System.out.println("가게 아이디 : "+storeId);
        System.out.println("회원 아이디 : "+memberId);
        System.out.println("회원 권한 : "+memberRole);

        if(memberRole.equals("OWNER")) {
            throw new IllegalArgumentException("일반회원들만 즐겨찾기를 추가할 수 있습니다.");
        }

        Store store = findOneStoreId(storeId);
        if(store.getStatus().equals(Status.DELETE)) {
            throw new IllegalArgumentException("운영중인 가게만 즐겨찾기를 추가할 수 있습니다.");
        }

        String storeName = store.getStoreName();
        String openTime = store.getOpenTime();
        String closeTime = store.getCloseTime();
        int minPrice = store.getMinPrice();
        return favoritesRepository.save(new Favorites(storeId, memberId, storeName, openTime, closeTime, minPrice));
    }

    public Store findOneStoreId(Long storeId){
        return storeRepository.findById(storeId).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 가게 입니다.")
        );
    }

    public Member findMemberId(Long storeId){
        return memberRepository.findById(storeId).orElseThrow(() ->
            new IllegalArgumentException("비정상적인 접근")
        );
    }
}
