package com.example.jpashop.service;

import com.example.jpashop.common.Address;
import com.example.jpashop.common.OrderStatus;
import com.example.jpashop.domain.Member;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.item.Book;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.exception.NoEnoughStockQuantityException;
import com.example.jpashop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void ItemOrder() throws Exception{
        Member member = new Member();
        member.setUsername("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        Item book = new Book();
        book.setName("책이름");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;
        Long orderId = orderService.Order(member.getId(),book.getId(),orderCount);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER,order.getStatus() );
        assertEquals(8,book.getStockQuantity());
        assertEquals(10000*orderCount, order.totalPrice());
    }

    @Test
    public void cancelOrder() throws Exception{
        Member member = new Member();
        member.setUsername("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        Item book = new Book();
        book.setName("책이름");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;
        Long orderId = orderService.Order(member.getId(),book.getId(),orderCount);

        //then
        Order order = orderRepository.findOne(orderId);
        order.cancel();
        assertEquals(OrderStatus.CANCEL,order.getStatus() );
        assertEquals(10,book.getStockQuantity());
    }

    @Test
    public void orderCountBiggerThenStock() throws Exception{
        Member member = new Member();
        member.setUsername("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        Item book = new Book();
        book.setName("책이름");
        book.setPrice(10000);
        book.setStockQuantity(10);
        int orderCount = 12;
        em.persist(book);
        assertThrows(NoEnoughStockQuantityException.class,()->{
            orderService.Order(member.getId(),book.getId(),orderCount);
        });
    }
}