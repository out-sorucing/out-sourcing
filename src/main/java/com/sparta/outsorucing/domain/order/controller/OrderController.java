package com.sparta.outsorucing.domain.order.controller;

import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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


}
