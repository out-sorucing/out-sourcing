package com.sparta.outsorucing.domain.favorites.repository;

import com.sparta.outsorucing.domain.favorites.entity.Favorites;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    List<Favorites> findByMemberId(Long memberId);
    int countByStoreIdAndMemberId(Long storeId, Long memberId);
}
