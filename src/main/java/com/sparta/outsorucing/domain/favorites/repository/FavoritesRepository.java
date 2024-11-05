package com.sparta.outsorucing.domain.favorites.repository;

import com.sparta.outsorucing.domain.favorites.entity.Favorites;
import com.sparta.outsorucing.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

}
