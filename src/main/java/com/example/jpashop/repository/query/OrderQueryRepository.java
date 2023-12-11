package com.example.jpashop.repository.query;

import com.example.jpashop.domain.OrderItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;


    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> orders = findOrders();
        orders.forEach(o ->{
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return orders;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new com.example.jpashop.repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                "from OrderItem oi " +
                "join oi.item i " +
                "where oi.order.id = :orderId",OrderItemQueryDto.class).setParameter("orderId",orderId).getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery("select new com.example.jpashop.repository.query.OrderQueryDto(o.id, m.name, o.date, o.status, d.address)" +
                " from Order o " +
                "join o.member m " +
                "join o.delivery d", OrderQueryDto.class).getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> orders = findOrders();
        List<Long> orderIds = orders.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
        List<OrderItemQueryDto> orderItems = em.createQuery("select new com.example.jpashop.repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.id in :orderIds"  //Dto반환시 1+N문제를 해결하기 위해 in절을 사용
                , OrderItemQueryDto.class).setParameter("orderIds", orderIds).getResultList();
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        //여기까지 하니 드는생각 굳이? 개별룬데?

        orders.forEach(o->{
            o.setOrderItems(orderItemMap.get(o.getOrderId()));
        });

        return orders;
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery("select new com.example.jpashop.repository.query.OrderFlatDto(o.id,m.name,o.date,o.status,d.address,i.name,oi.orderPrice,oi.count) " +
                "from Order o " +
                "join o.member m " +
                "join o.delivery d " +
                "join o.orderItems oi " +
                "join oi.item i", OrderFlatDto.class).getResultList();
    }
}
