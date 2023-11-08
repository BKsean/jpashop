package com.example.jpashop.domain;

import com.example.jpashop.common.Address;
import com.example.jpashop.common.DeliveryStatus;
import com.example.jpashop.domain.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)//이거 몰랐다
    private DeliveryStatus status;
}
