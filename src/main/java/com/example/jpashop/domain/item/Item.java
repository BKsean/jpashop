package com.example.jpashop.domain.item;

import com.example.jpashop.domain.CategoryItem;
import com.example.jpashop.exception.NoEnoughStockQuantityException;
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

    //비즈니스 로직 stock을 setter로 변경하는 것이 아니라 아래의 로직으로 변경하는것
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    public void reduceStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NoEnoughStockQuantityException("줄이려는 수량이 남은 수량보다 많습니다.");
        }else{
            this.stockQuantity -= quantity;
        }

    }


    public void removeStock(int count) {
        stockQuantity -= count;
    }
}
