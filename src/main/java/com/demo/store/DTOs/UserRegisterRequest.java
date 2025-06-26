package com.demo.store.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserRegisterRequest {
    private String name;
    private String email;
    private String password;
}
