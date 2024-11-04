package com.sparta.outsorucing.domain.order.service;

import static com.sparta.outsorucing.common.enums.MemberRole.USER;
import static com.sparta.outsorucing.common.enums.OrderStatus.CANCELED;
import static com.sparta.outsorucing.common.enums.OrderStatus.ORDERED;

import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.member.repository.MemberRepository;
import com.sparta.outsorucing.domain.menu.entity.Menu;
import com.sparta.outsorucing.domain.menu.repository.MenuRepository;
import com.sparta.outsorucing.domain.order.dto.ChangeOrderStatusDto;
import com.sparta.outsorucing.domain.order.dto.OrdersResponseDto;
import com.sparta.outsorucing.domain.order.entity.Order;
import com.sparta.outsorucing.domain.order.repository.OrderRepository;
import com.sparta.outsorucing.domain.store.entity.Store;
import com.sparta.outsorucing.domain.store.repository.StoreRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public String requestOrder(Long membersId, Long menusId) {

        Member member = memberRepository.findById(membersId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Menu menu = menuRepository.findById(menusId)
            .orElseThrow(() -> new IllegalStateException("Menus not found"));
        Store store = storeRepository.findById(menu.getStore().getId())
            .orElseThrow(() -> new IllegalStateException("Store not found"));
        // 최소 주문금액
        if (menu.getPrice() < store.getMinPrice()) {
            throw new IllegalStateException("최소주문금액이 부족합니다.");
        }
        // 오픈/마감시간 비교
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        if (dateFormat.format(new Date()).compareTo(store.getOpenTime()) < 0 ||
            dateFormat.format(new Date()).compareTo(store.getCloseTime()) > 0) {
            throw new IllegalStateException("오픈시간이 아닙니다.");
        }

        Order order = Order.builder().status(ORDERED)
            .member(member)
            .menu(menu)
            .store(store)
            .build();

        orderRepository.save(order);

        return "주문이 완료되었습니다.";
    }

    @Transactional
    public String changeOrderStatus(AuthMember authMember, Long ordersId,
        ChangeOrderStatusDto changeOrderStatusDto) {
        Order order = orderRepository.findById(ordersId)
            .orElseThrow(() -> new IllegalStateException("Order not found"));

        if (authMember.getId().equals(order.getMember().getId())
            && changeOrderStatusDto.getOrderStatus().equals(CANCELED)) {
            throw new IllegalStateException("주문 취소 되었습니다.");
        }
        if (authMember.getMemberRole().equals(USER)) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        if (order.getStatus().equals(changeOrderStatusDto.getOrderStatus())) {
            throw new IllegalStateException(
                "이미" + changeOrderStatusDto.getOrderStatus() + "상태 입니다");
        }
        order.update(changeOrderStatusDto.getOrderStatus());

        return changeOrderStatusDto.getOrderStatus() + "로 변경되었습니다";
    }


    public Page<OrdersResponseDto> retrieveOrders(AuthMember authMember, int page, int size) {
        Member member = memberRepository.findById(authMember.getId())
            .orElseThrow(() -> new IllegalStateException("user not found"));
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> order = orderRepository.findByMember(member, pageable);
        return order.map(OrdersResponseDto::new);
    }
}
