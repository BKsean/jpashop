package com.example.jpashop.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemForm {

    private Long id;
    @NotEmpty(message = "상품의 이름을 등록해야 합니다.")
    private String name;
    @Min(0)
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
