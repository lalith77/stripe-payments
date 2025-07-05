package com.demo.store.Mappers;

import com.demo.store.DTOs.CartDto;
import com.demo.store.DTOs.CartItemDto;
import com.demo.store.entities.Cart;
import com.demo.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
