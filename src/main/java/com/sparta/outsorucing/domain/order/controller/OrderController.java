package com.sparta.outsorucing.domain.order.controller;

import com.sparta.outsorucing.common.annotation.Auth;
import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.common.enums.OrderStatus;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.order.dto.ChangeOrderStatusDto;
import com.sparta.outsorucing.domain.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    public String requestOrder(@Auth AuthMember authMember, @RequestParam("menusId") Long menusId)
    {

        return orderService.requestOrder(authMember.getId(),menusId);
    }

    @PutMapping("/{ordersId}")
    public String changeOrderStatus(@Auth AuthMember authMember, @PathVariable("ordersId") Long ordersId,@RequestBody
    ChangeOrderStatusDto changeOrderStatusDto)
    {
        return orderService.changeOrderStatus(authMember,ordersId,changeOrderStatusDto);
    }


}
