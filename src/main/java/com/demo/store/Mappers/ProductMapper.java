package com.demo.store.Mappers;

import com.demo.store.DTOs.ProductDto;
import com.demo.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface ProductMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);
}
