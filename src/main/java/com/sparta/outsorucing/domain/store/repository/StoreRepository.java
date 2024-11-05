package com.sparta.outsorucing.domain.store.repository;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.domain.store.entity.Store;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    //  가게 검색
    List<Store> findAllByStoreNameContainsOrderByIdDesc(String storeName);

    // 가게 3개 개수 체크
    int countByMemberIdAndStatus(Long memberId, Status status);

    String menuQuery = "SELECT a.member_id, a.min_price, a.status, a.store_name, a.close_time, a.open_time, b.id, b.menu_name, b.content, b.price "
        + "FROM out_store a LEFT JOIN out_menu b ON a.id = b.store_id WHERE a.id = :id";
    @Query(value=menuQuery, nativeQuery = true)
    List<Store> findOneStoreAndMenu(Long id);

}
