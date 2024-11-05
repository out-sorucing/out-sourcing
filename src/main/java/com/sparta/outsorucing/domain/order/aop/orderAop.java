//package com.sparta.outsorucing.domain.order.aop;
//
//import com.sparta.outsorucing.common.dto.AuthMember;
//import com.sparta.outsorucing.common.enums.MemberRole;
//import com.sparta.outsorucing.domain.member.entity.Member;
//import com.sparta.outsorucing.domain.menu.entity.Menu;
//import com.sparta.outsorucing.domain.menu.repository.MenuRepository;
//import com.sparta.outsorucing.domain.order.dto.ChangeOrderStatusDto;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class orderAop {
//    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    // 주문 생성 시 로깅
//    @AfterReturning(value = "execution(* com.sparta.outsorucing.domain.order.service.OrderService.requestOrder(..))", returning = "result")
//    public void logRequestOrder(JoinPoint joinPoint, Object result) {
//        Object[] args = joinPoint.getArgs();
//        Long memberId = (Long) args[0];
//        Long menuId = (Long) args[1];
//
//        // 가게 ID 및 주문 ID를 확인하여 로깅
//        String timestamp = dateFormat.format(new Date());
//        System.out.println("주문 생성 로그: " + timestamp + " | 메뉴 ID: " + menuId + " | 멤버 ID: " + memberId);
//        System.out.println("결과: " + result);
//    }
//
//    // 주문 상태 변경 시 로깅
//    @Before("execution(* com.sparta.outsorucing.domain.order.service.OrderService.changeOrderStatus(..)) && args(authMember, ordersId, changeOrderStatusDto)")
//    public void logChangeOrderStatus(JoinPoint joinPoint, AuthMember authMember, Long ordersId, ChangeOrderStatusDto changeOrderStatusDto) {
//        String timestamp = dateFormat.format(new Date());
//
//        System.out.println("주문 상태 변경 로그: " + timestamp + " | 주문 ID: " + ordersId + " | 새로운 상태: " + changeOrderStatusDto.getOrderStatus());
//    }
//
//
//
//
//}
