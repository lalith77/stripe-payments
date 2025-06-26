package com.demo.store.Mappers;

import com.demo.store.DTOs.UserDto;
import com.demo.store.DTOs.UserRegisterRequest;
import com.demo.store.DTOs.UserUpdateRequest;
import com.demo.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    UserDto toDto(User user);

    User toEntity(UserRegisterRequest userRegisterRequest);

    void toUser(UserUpdateRequest userUpdateRequest, @MappingTarget User user);
}

