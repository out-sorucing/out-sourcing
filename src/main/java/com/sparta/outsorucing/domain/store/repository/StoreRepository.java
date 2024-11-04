package com.sparta.outsorucing.domain.store.repository;

import com.sparta.outsorucing.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StoreRepository extends JpaRepository<Store, Integer> {

}
