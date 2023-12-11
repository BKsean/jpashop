package com.example.jpashop.domain;

import com.example.jpashop.domain.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;
    private int orderPrice;

    protected OrderItem() {  //빈생성자 선언과 동시에 protected로 접근제어를 하면서 생성을 제한 lombok으로는 @NoArgscont...(access = 옵션으로 가능)

    }

    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setCount(count);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setItem(item);
        item.reduceStock(count);
        return orderItem;
    }
    //주문취소시 item로직
    public void cancle() {
        getItem().addStock(count);
    }

    public int getTotalPrice() {
        return orderPrice*count;
    }
}
