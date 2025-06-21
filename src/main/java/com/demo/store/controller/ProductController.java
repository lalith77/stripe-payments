package com.demo.store.controller;

import com.demo.store.DTOs.ProductDto;
import com.demo.store.Mappers.ProductMapper;
import com.demo.store.entities.Product;
import com.demo.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping("/products")
    public List<ProductDto> getAllProducts(@RequestParam(required = false) Long categoryId){
        List<Product> productList = new ArrayList<>();
        if(categoryId != null){
            productList = productRepository.findByCategoryId(categoryId);
        }
        else{
            productList =productRepository.findAll();
        }
        return productList.stream()
                .map(productMapper::toDto).toList();

    }
}
