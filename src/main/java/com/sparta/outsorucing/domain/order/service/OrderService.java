package com.sparta.outsorucing.domain.order.service;

import static com.sparta.outsorucing.common.enums.MemberRole.OWNER;
import static com.sparta.outsorucing.common.enums.OrderStatus.CANCELED;
import static com.sparta.outsorucing.common.enums.OrderStatus.ORDERED;
import static com.sparta.outsorucing.common.enums.Status.DELETE;
import static com.sparta.outsorucing.common.enums.Status.VIP;

import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.common.exception.InvalidRequestException;
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

    @Transactional
    public String requestOrder(AuthMember authMember, Long menusId) {

        Member member = findMember(authMember);
        Menu menu = menuRepository.findById(menusId)
            .orElseThrow(() -> new InvalidRequestException("Menus not found"));
        Store store = findStore(menu.getStore().getId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        if (dateFormat.format(new Date()).compareTo(store.getOpenTime()) < 0 ||
            dateFormat.format(new Date()).compareTo(store.getCloseTime()) > 0) {
            throw new InvalidRequestException("오픈시간이 아닙니다.");
        }
        if (menu.getStatus().equals(DELETE)) {
            throw new InvalidRequestException("삭제된 메뉴입니다.");
        }
        // 최소 주문금액
        if (menu.getPrice() < store.getMinPrice()) {
            throw new InvalidRequestException("최소주문금액이 부족합니다.");
        }
        int price = menu.getPrice();
        if (member.getStatus().equals(VIP)) {
            price *= 0.95;
        }

        Order order = Order.builder().status(ORDERED)
            .member(member)
            .price(price)
            .menu(menu)
            .store(store)
            .build();

        orderRepository.save(order);

        int countOrder = orderRepository.countByMember(member);
        if (countOrder == 10) {
            member.update(VIP);
        }

        return "주문이 완료되었습니다.";
    }

    public String requestReOrder(AuthMember authMember, Long ordersId) {
        Order order = findOrder(ordersId);

        if (!order.getMember().getId().equals(authMember.getId())) {
            throw new InvalidRequestException("내 주문이 아닙니다.");
        }

        return requestOrder(authMember, order.getMenu().getId());
    }

    @Transactional
    public String changeOrderStatus(AuthMember authMember, Long ordersId,
        ChangeOrderStatusDto changeOrderStatusDto) {
        Order order = findOrder(ordersId);

        if (authMember.getId().equals(order.getMember().getId())
            && changeOrderStatusDto.getOrderStatus().equals(CANCELED)) {
            order.update(changeOrderStatusDto.getOrderStatus());
            return changeOrderStatusDto.getOrderStatus() + "로 변경되었습니다";
        }
        if (!authMember.getId().equals(order.getStore().getMember().getId())) {
            throw new InvalidRequestException("권한이 없습니다.");
        }
        if (order.getStatus().equals(changeOrderStatusDto.getOrderStatus())) {
            throw new InvalidRequestException(
                "이미" + changeOrderStatusDto.getOrderStatus() + "상태 입니다");
        }

        order.update(changeOrderStatusDto.getOrderStatus());

        return changeOrderStatusDto.getOrderStatus() + "로 변경되었습니다";
    }

    public Page<OrdersResponseDto> retrieveOrders(AuthMember authMember, int page, int size) {
        Member member = findMember(authMember);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> order = orderRepository.findByMember(member, pageable);
        return order.map(OrdersResponseDto::new);
    }

    public Page<OrdersResponseDto> retrieveOrdersByStore(AuthMember authMember, Long storesId,
        int page, int size) {
        Member member = findMember(authMember);
        Store store = findStore(storesId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> order = orderRepository.findByMemberAndStore(member, store, pageable);

        if (member.getRole().equals(OWNER)) {
            order = orderRepository.findByStore(store, pageable);
        }

        return order.map(OrdersResponseDto::new);
    }

    public OrdersResponseDto retrieveOrdersById(AuthMember authMember, Long ordersId) {
        Member member = findMember(authMember);
        Order order = findOrder(ordersId);

        if (!member.getId().equals(order.getMember().getId()) && !member.getId()
            .equals(order.getStore().getMember().getId())) {
            throw new InvalidRequestException("내 주문이 아닙니다.");
        }

        return OrdersResponseDto.of(order);
    }

    private Member findMember(AuthMember authMember) {
        return memberRepository.findById(authMember.getId())
            .orElseThrow(() -> new InvalidRequestException("user not found"));
    }

    private Store findStore(Long storesId) {
        return storeRepository.findById(storesId)
            .orElseThrow(() -> new InvalidRequestException("store not found"));
    }

    private Order findOrder(Long ordersId) {
        return orderRepository.findById(ordersId)
            .orElseThrow(() -> new InvalidRequestException("order not found"));
    }


}
