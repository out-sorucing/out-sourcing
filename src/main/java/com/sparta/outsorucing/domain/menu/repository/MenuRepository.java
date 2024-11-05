package com.sparta.outsorucing.domain.menu.repository;

import com.sparta.outsorucing.domain.menu.dto.MenuResponseDto;
import com.sparta.outsorucing.domain.menu.entity.Menu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findByIdAndStoreId(
        Long menuId,
        Long storeId);

    // 가게단건에서 메뉴 목록조회
    List<Menu> findByStoreId(Long storeId);

    @Query("select m from Menu m where m.store.id = :storeId and m.status = 'ACTIVE'")
    List<MenuResponseDto> findByStoreIdAndStatus(Long storeId);

    @Query("select m from Menu m where replace(m.menuName,' ','') = replace(:menuName,' ','') and m.store.id = :storeId")
    Optional<Menu> findByMenuNameAndStoreId(String menuName, Long storeId);
}
