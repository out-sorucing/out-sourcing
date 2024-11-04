package com.sparta.outsorucing.domain.menu.repository;

import com.sparta.outsorucing.common.exception.InvalidRequestException;
import com.sparta.outsorucing.domain.menu.entity.Menu;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu findByMenuIdAndStoreId(
        Long menuId,
        Long storeId) {
        return findByIdAndStoreId(menuId,
                                  storeId).orElseThrow(() -> new InvalidRequestException("가게에 존재하지 않는 메뉴입니다."));
    }

    Optional<Menu> findByIdAndStoreId(
        Long menuId,
        Long storeId);
}
