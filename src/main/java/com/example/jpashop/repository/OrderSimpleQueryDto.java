package com.example.jpashop.repository;

import com.example.jpashop.common.Address;
import com.example.jpashop.common.OrderStatus;
import com.example.jpashop.domain.Order;
import lombok.Data;

import java.time.LocalDateTime;

    @Data
    public class OrderSimpleQueryDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleQueryDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }