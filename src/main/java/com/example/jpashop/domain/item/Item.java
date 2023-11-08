package com.example.jpashop.domain.item;

import com.example.jpashop.domain.CategoryItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int stockQuantity;
    private int price;

    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItemList;




}
