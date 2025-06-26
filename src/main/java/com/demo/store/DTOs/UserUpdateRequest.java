package com.demo.store.DTOs;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String email;
}
