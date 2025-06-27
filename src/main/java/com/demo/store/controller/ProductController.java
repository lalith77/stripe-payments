package com.demo.store.controller;

import com.demo.store.DTOs.ProductDto;
import com.demo.store.DTOs.UserDto;
import com.demo.store.DTOs.UserUpdateRequest;
import com.demo.store.Mappers.ProductMapper;
import com.demo.store.entities.Category;
import com.demo.store.entities.Product;
import com.demo.store.entities.User;
import com.demo.store.repositories.CategoryRepository;
import com.demo.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @GetMapping
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

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        Product product = productMapper.toProductEntity(productDto);
        productRepository.save(product);

        productDto.setId(product.getId());
        URI uri = uriComponentsBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> updateUser(@PathVariable Long id,
                                              @RequestBody ProductDto productDto) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        //get new product data
        product = productMapper.toProductEntity(productDto);
//        Category category = new Category();
//        category.setId(productDto.getCategoryId());

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);
        productRepository.save(product);
        return ResponseEntity.ok(productMapper.toDto(product));
    }


}
