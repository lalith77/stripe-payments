package com.demo.store.Mappers;

import com.demo.store.DTOs.CartDto;
import com.demo.store.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
