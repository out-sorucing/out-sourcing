package com.sparta.outsorucing.domain.order.controller;

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
    public String requestOrder(HttpServletRequest request, @RequestParam("menusId") Long menusId)
    {
        Member member = (Member)request.getAttribute("member");
        return orderService.requestOrder(member,menusId);
    }

    @PutMapping("/{ordersId}")
    public String changeOrderStatus(HttpServletRequest request, @PathVariable("ordersId") Long ordersId,@RequestBody
    ChangeOrderStatusDto changeOrderStatusDto)
    {
        Member member = (Member)request.getAttribute("member");
        return orderService.changeOrderStatus(member,ordersId,changeOrderStatusDto);
    }


}
