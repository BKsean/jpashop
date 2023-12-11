package com.example.jpashop.api;

import com.example.jpashop.common.Address;
import com.example.jpashop.common.OrderStatus;
import com.example.jpashop.domain.Order;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xTOONE 관계
 * order -> Member
 * Order -> Delivery
 * 관계를 다루는 예제
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController{
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1(){
        List<Order> all = orderRepository.findAll(new OrderSearch());

        //1. Order를 반환하려니 Member가 있고 Member에 가보니 Order가있어서 양방향 참조 문제로 무한루프가 돌아간다. 따라서 둘중 하나는 @JsonIgnore로 끊어주어야한다.
        //2. Order에서 Member는 지연로딩이다. 하지만 null을 반환할수 없으니 proxy개체로 임시로 반환하는데 그것이 bytebuddy 라이브러리를 이용한 객체를 임시로 반환한다.
        // --> 하지만 이 개체는 순수한 자바개체가아니기 때문에 json변환이 안되는듯

        // 지연로딩 강제 초기화 시켜서 내보내기
        all.forEach(order -> {
            order.getMember().getName();
            order.getDelivery().getAddress();
        });
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<SimpleOrderDto> collect = orders.stream().map(order -> new SimpleOrderDto(order)).collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(); //패치조인으로 order를 가져올떄 member,delivery객체까지 다가져온다 lazy로딩같은 것 신경쓰지 않아도댐
        return orders.stream().map(order -> new SimpleOrderDto(order)).collect(Collectors.toList());
    }

//    @GetMapping("/api/v4/simple-orders")
//    public List<SimpleOrderDto> orderV4(){
//        orderRepository.findOrderDtos();
//
//    }
    @Data
    private class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
