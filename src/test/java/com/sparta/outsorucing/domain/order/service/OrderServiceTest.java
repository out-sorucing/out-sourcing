package com.sparta.outsorucing.domain.order.service;


import static com.sparta.outsorucing.common.enums.MemberRole.USER;
import static com.sparta.outsorucing.common.enums.Status.ACTIVE;
import static com.sparta.outsorucing.common.enums.Status.DELETE;
import static com.sparta.outsorucing.common.enums.Status.VIP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.common.exception.InvalidRequestException;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.member.repository.MemberRepository;
import com.sparta.outsorucing.domain.menu.entity.Menu;
import com.sparta.outsorucing.domain.menu.repository.MenuRepository;
import com.sparta.outsorucing.domain.order.entity.Order;
import com.sparta.outsorucing.domain.order.repository.OrderRepository;
import com.sparta.outsorucing.domain.store.entity.Store;
import com.sparta.outsorucing.domain.store.repository.StoreRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private OrderService orderService;

    private AuthMember authMember;
    private Member member;
    private Menu menu;
    private Store store;

    @BeforeEach
    void setUp() {

        authMember = new AuthMember(1L, "test1", "test@test.com", USER, ACTIVE);
        member = new Member(authMember.getNickName(), authMember.getEmail(), "P@ssword",
            authMember.getMemberRole());
        store = new Store(1L, "Sample Store", "09:00", "22:00", 5000, ACTIVE, member);

        menu = new Menu("Sample Menu", 10000, "AVAILABLE", ACTIVE, store);
        menu.setId(1L);
    }

    @Test
    @DisplayName("성공적으로 주문 요청 시 주문이 완료된다.")
    void requestOrder_SuccessfulOrder() {
        // given
        when(memberRepository.findById(authMember.getId())).thenReturn(Optional.of(member));
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        // when
        String result = orderService.requestOrder(authMember, menu.getId());

        // then
        assertEquals("주문이 완료되었습니다.", result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("운영 시간 외 주문 시 예외가 발생한다.")
    void requestOrder_StoreClosed_ExceptionThrown() {
        // given
        store.setOpenTime("23:00");
        store.setCloseTime("23:59");
        when(memberRepository.findById(authMember.getId())).thenReturn(Optional.of(member));
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
        System.out.println(store.getOpenTime());

        // when & then
        assertThrows(InvalidRequestException.class,
            () -> orderService.requestOrder(authMember, menu.getId()));
    }

    @Test
    @DisplayName("삭제된 메뉴 주문 시 예외가 발생한다.")
    void requestOrder_DeletedMenu_ExceptionThrown() {
        // given
        menu.setStatus(DELETE);
        when(memberRepository.findById(authMember.getId())).thenReturn(Optional.of(member));
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        // when & then
        assertThrows(InvalidRequestException.class,
            () -> orderService.requestOrder(authMember, menu.getId()));
    }

    @Test
    @DisplayName("VIP 회원의 주문 시 할인 적용 확인")
    void requestOrder_VIPMemberDiscount() {
        // given
        member.setStatus(VIP);
        when(memberRepository.findById(authMember.getId())).thenReturn(Optional.of(member));
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        // when
        String result = orderService.requestOrder(authMember, menu.getId());

        // then
        assertEquals("주문이 완료되었습니다.", result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }


}
