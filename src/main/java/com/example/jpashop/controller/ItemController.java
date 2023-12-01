package com.example.jpashop.controller;

import com.example.jpashop.domain.item.Book;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.repository.ItemRepository;
import com.example.jpashop.service.ItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    //상품등록 폼
    @GetMapping("/items/new")
    public String newItem(Model model){
        model.addAttribute("form",new ItemForm());
        return "/items/createItemForm";
    }

    //상품등록 로직
    @PostMapping("/items/new")
    public String createItem(@Valid ItemForm itemForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/items/createItemForm";
        }
         Book book = new Book();
        book.setStockQuantity(itemForm.getStockQuantity());
        book.setPrice(itemForm.getPrice());
        book.setName(itemForm.getName());
        book.setAuthor(itemForm.getAuthor());
        book.setIsbn(itemForm.getIsbn());
        itemRepository.save(book);
        return "redirect:/items";
    }
    //상품 목록
    @GetMapping("/items")
    public String itemList(Model model){
        model.addAttribute("items",itemRepository.findAll());
        return "/items/itemList";
    }

    @GetMapping("/items/{id}/edit")
    public String updateFormItem(@PathVariable("id") Long id, Model model){
        Book book = (Book)itemRepository.findOne(id);
        ItemForm form = new ItemForm();
        form.setId(book.getId());
        form.setAuthor(book.getAuthor());
        form.setIsbn(book.getIsbn());
        form.setName(book.getName());
        form.setPrice(book.getPrice());
        form.setStockQuantity(book.getStockQuantity());
        model.addAttribute("form",form);
        return "/items/updateItemForm";
    }

    @PostMapping("items/{id}/edit")
    public String updateFormItem(ItemForm itemForm){
        Book book = new Book();

        book.setId(itemForm.getId());
        book.setName(itemForm.getName());
        book.setPrice(itemForm.getPrice());
        book.setStockQuantity(itemForm.getStockQuantity());
        book.setAuthor(itemForm.getAuthor());
        book.setIsbn(itemForm.getIsbn());

        itemRepository.save(book);

        return "redirect:/items";
    }
}
