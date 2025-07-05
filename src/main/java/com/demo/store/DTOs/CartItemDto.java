package com.demo.store.DTOs;

import lombok.Value;

import java.io.Serializable;


@Value
public class CartItemDto implements Serializable {
    Long id;
}