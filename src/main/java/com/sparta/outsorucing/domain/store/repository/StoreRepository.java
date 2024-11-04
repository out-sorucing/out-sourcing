package com.sparta.outsorucing.domain.store.repository;

import com.sparta.outsorucing.domain.store.entity.Store;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

}
