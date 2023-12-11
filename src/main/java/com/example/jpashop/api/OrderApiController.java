package com.example.jpashop.api;

import com.example.jpashop.common.Address;
import com.example.jpashop.common.OrderStatus;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderItem;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.OrderSearch;
import com.example.jpashop.repository.query.OrderFlatDto;
import com.example.jpashop.repository.query.OrderQueryDto;
import com.example.jpashop.repository.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();
            }
        }
        return orders;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        List<OrderDto> collect = orders.stream().map(order -> new OrderDto(order)).collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem();
        // orderItem 이 총4개이기 때문에 order도 4개로 중복데이터가 발생한다.  --> 예방 fetch 조인에서 distinct를 적어주면된다 hibernate 6.0에서는 자동 적용된다  1:N fetch조인을 하면 페이징쿼리가 안된다? --> 설정해도 페이징쿼리가 안나간다
        // 패치조인으로 데이터가 중복되는데 거기다가 페이징을 하려고 해서.. --> 페이징처리는 메모리에서 진행
        //*** 컬렉션 패치조인은 1개만 사용할 수 있다. 컬렉션 둘 이상의 페치조인은 사용하면 안된다. 데이터가 부정합해진다.

        List<OrderDto> collect = orders.stream().map(order -> new OrderDto(order)).collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page (@RequestParam(value = "offset",defaultValue = "0") int offset,
                                         @RequestParam(value = "limit",defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,limit);//TOMany인 orderItems는 fetch하지않고 지연로딩으로 가져오고 Batch로 해결
        List<OrderDto> collect = orders.stream().map(order -> new OrderDto(order)).collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> orderV4(){
        return orderQueryRepository.findOrderQueryDtos(); //Dto로 조회해오기
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> orderV5(){
        return orderQueryRepository.findAllByDto_optimization();  //1+N문제 발생 최적화하기
    }

    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> orderV6(){
        return orderQueryRepository.findAllByDto_flat();
        //결과값이 orderItems기준으로 중복 데이터가 존재하며 의도했던 api스팩인 OrderQUeryDto형식도 아니다
        //여기서 다시 OrderFlatDto를 OrderQueryDto로 스트림으로 어찌어찌 잘돌면서 중복도제거하고 스펙에 맞게 OrderQueryDto로 변환해서 반환해주어야한다.
    }
    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;//orderItem도 엔티티이기 때문에 Dto를 만들어준다.

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream().map(orderItem -> new OrderItemDto(orderItem)).collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {
        private Long itemId;
        private String itemName;
        private int orderPrice;
        private int count;
        public OrderItemDto(OrderItem orderItem) {
            itemId = orderItem.getItem().getId();
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
