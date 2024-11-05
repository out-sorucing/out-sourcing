package com.sparta.outsorucing.domain.order.repository;

import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.order.entity.Order;
import com.sparta.outsorucing.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByMember(Member Member, Pageable pageable);

    Page<Order> findByMemberAndStore(Member member, Store store, Pageable pageable);

    Page<Order> findByStore(Store store, Pageable pageable);
}
