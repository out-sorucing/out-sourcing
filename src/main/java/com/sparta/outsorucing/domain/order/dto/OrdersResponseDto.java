package com.sparta.outsorucing.domain.order.dto;

import com.sparta.outsorucing.common.enums.OrderStatus;
import com.sparta.outsorucing.domain.order.entity.Order;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrdersResponseDto {

    private int price;
    private OrderStatus status;
    private String menuName;
    private String storeName;
    private LocalDateTime createdAt;

    public OrdersResponseDto(Order order) {
        this.price = order.getMenu().getPrice();
        this.status = order.getStatus();
        this.menuName = order.getMenu().getMenuName();
        this.storeName = order.getStore().getStoreName();
        this.createdAt = order.getCreatedAt();
    }

}
