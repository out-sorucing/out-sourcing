package com.sparta.outsorucing.domain.order.controller;

import com.sparta.outsorucing.common.annotation.Auth;
import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.domain.order.dto.ChangeOrderStatusDto;
import com.sparta.outsorucing.domain.order.dto.OrdersResponseDto;
import com.sparta.outsorucing.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public String requestOrder(@Auth AuthMember authMember, @RequestParam("menusId") Long menusId) {
        return orderService.requestOrder(authMember, menusId);
    }

    @PutMapping("/{ordersId}")
    public String changeOrderStatus(@Auth AuthMember authMember,
        @PathVariable("ordersId") Long ordersId, @RequestBody
    ChangeOrderStatusDto changeOrderStatusDto) {
        return orderService.changeOrderStatus(authMember, ordersId, changeOrderStatusDto);
    }

    @GetMapping
    public ResponseEntity<Page<OrdersResponseDto>> retrieveOrders(@Auth AuthMember authMember,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "5") int size) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(orderService.retrieveOrders(authMember, page, size));
    }

    @GetMapping("/stores")
    public ResponseEntity<Page<OrdersResponseDto>> retrieveOrdersById(@Auth AuthMember authMember,
        @RequestParam("storesId") Long storesId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "5") int size) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(orderService.retrieveOrdersById(authMember, storesId, page, size));
    }


}
