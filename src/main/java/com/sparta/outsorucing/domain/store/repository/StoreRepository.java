package com.sparta.outsorucing.domain.store.repository;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.domain.store.entity.Store;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByStatus(Status status);

    //  가게 검색
    List<Store> findAllByStoreNameContainsOrderByIdDesc(String storeName);

    // 가게 3개 개수 체크
    int countByMemberIdAndStatus(Long memberId, Status status);

    // 가게 단건조회
    Store findByIdAndStatus(Long id, Status status);

    @Query("select s from Store s left join Menu m on s.id = m.store.id where m.menuName like %:menuName%")
    List<Store> findAllByMenuName(String menuName);

}
