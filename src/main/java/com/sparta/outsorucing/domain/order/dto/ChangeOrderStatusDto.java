package com.sparta.outsorucing.domain.order.dto;

import com.sparta.outsorucing.common.enums.OrderStatus;
import com.sparta.outsorucing.domain.order.entity.Order;
import lombok.Getter;

@Getter
public class ChangeOrderStatusDto {
    private OrderStatus orderStatus;

}
