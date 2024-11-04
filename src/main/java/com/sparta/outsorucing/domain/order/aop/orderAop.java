package com.sparta.outsorucing.domain.order.aop;

import com.sparta.outsorucing.domain.menu.entity.Menu;
import com.sparta.outsorucing.domain.menu.repository.MenuRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class orderAop {
//    private MenuRepository menuRepository;
//
//    // 생성자를 통한 의존성 주입
//    @Autowired
//    public void OrderLoggingAspect(MenuRepository menuRepository) {
//        this.menuRepository = menuRepository;
//    }
//
//    // 주문 생성 시 적용할 Pointcut 정의
//    @Pointcut("execution(* com.sparta.outsorucing.domain.order.service.OrderService.requestOrder(..))")
//    public void orderRequestPointcut() {}
//
//    // 주문 요청 후 로그를 남기는 Advice
//    @AfterReturning(pointcut = "orderRequestPointcut()", returning = "result")
//    public void logOrderRequest(JoinPoint joinPoint, Object result) {
//        // 요청 시각 설정
//        String requestTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//
//        // JoinPoint를 통해 메소드 인자 정보 가져오기
//        Object[] args = joinPoint.getArgs();
//        Member member = (Member) args[0];
//        Long menusId = (Long) args[1];
//
//        // 메뉴와 가게 정보 가져오기
//        Menu menu = menuRepository.findById(menusId)
//            .orElseThrow(() -> new IllegalStateException("Menus not found"));
//        Long storeId = menu.getStore().getId();
//
//        // 로그 출력 (이 예제에서는 콘솔에 출력)
//        System.out.println("Order Log:");
//        System.out.println("요청 시각: " + requestTime);
//        System.out.println("가게 ID: " + storeId);
//        System.out.println("주문 ID: " + result); // `result`가 주문 ID일 경우
//    }

    private MenuRepository menuRepository;

    @Autowired
    public void OrderLoggingAspect(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    // 주문 요청 시 적용할 Pointcut 정의
    @Pointcut("execution(* com.sparta.outsorucing.domain.order.service.OrderService.requestOrderTest(..))")
    public void orderRequestPointcut() {}

    // 주문 요청 후 로그를 남기는 Advice
    @AfterReturning(pointcut = "orderRequestPointcut()", returning = "result")
    public void logOrderRequest(JoinPoint joinPoint, Object result) {
        // 요청 시각 설정
        String requestTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // JoinPoint를 통해 메소드 인자 정보 가져오기
        Object[] args = joinPoint.getArgs();
        Long menusId = (Long) args[1]; // 두 번째 인자가 menusId

        // Menu와 Store 정보 가져오기
        Menu menu = menuRepository.findById(menusId)
            .orElseThrow(() -> new IllegalStateException("Menus not found"));
        Long storeId = menu.getStore().getId();

        // 로그 출력
        System.out.println("Order Log:");
        System.out.println("요청 시각: " + requestTime);
        System.out.println("가게 ID: " + storeId);
        System.out.println("주문 ID: " + result); // `result`가 주문 ID일 경우
    }


}
