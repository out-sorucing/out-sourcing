package com.sparta.outsorucing.domain.order.aop;

import com.sparta.outsorucing.domain.menu.repository.MenuRepository;
import com.sparta.outsorucing.domain.order.entity.Order;
import com.sparta.outsorucing.domain.order.repository.OrderRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class orderAop {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;


    @Before(
        "execution(* com.sparta.outsorucing.domain.order.service.OrderService.requestOrder(..)) || "
            +
            "execution(* com.sparta.outsorucing.domain.order.service.OrderService.requestReOrder(..)) || "
            +
            "execution(* com.sparta.outsorucing.domain.order.service.OrderService.changeOrderStatus(..))")
    public void logOrderActions(JoinPoint joinPoint) {
        // 요청 시각
        String requestTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 메서드 이름에 따라 필요한 ID 값들을 설정
        String methodName = joinPoint.getSignature().getName();
        Long storeId = null;
        Long orderId = null;

        Object[] args = joinPoint.getArgs();

        if ("requestOrder".equals(methodName) && args.length > 1) {
            // 메뉴 ID를 사용하여 가게 ID 조회
            Long menuId = (Long) args[1];
            storeId = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalStateException("Menu not found"))
                .getStore().getId();
        } else if ("requestReOrder".equals(methodName) && args.length > 1) {
            // 주문 ID로부터 가게 ID와 주문 ID 조회
            orderId = (Long) args[1];
            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found"));
            storeId = order.getStore().getId();
        } else if ("changeOrderStatus".equals(methodName) && args.length > 1) {
            // 주문 ID로부터 가게 ID와 주문 ID 조회
            orderId = (Long) args[1];
            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found"));
            storeId = order.getStore().getId();
        }
        
        // 로그 기록
        log.info("요청 시각: {}, 가게 ID: {}, 주문 ID: {}", requestTime, storeId, orderId);
    }

}
