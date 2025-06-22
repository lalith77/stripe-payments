package com.demo.store.controller;

import com.demo.store.DTOs.ProductDto;
import com.demo.store.Mappers.ProductMapper;
import com.demo.store.entities.Product;
import com.demo.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestHeader(name = "x-auth-token", required = false) String authToken) {
        // sample token
        System.out.println(authToken);
        List<Product> productList = new ArrayList<>();
        if (categoryId != null) {
            productList = productRepository.findByCategoryId(categoryId);
        } else {
            productList = productRepository.findAllWithCategory();
        }
        if (productList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ProductDto> productDtoList = productList.stream()
                .map(productMapper::toDto).toList();
        return ResponseEntity.ok(productDtoList);
    }


}
