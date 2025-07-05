package com.demo.store.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
}
